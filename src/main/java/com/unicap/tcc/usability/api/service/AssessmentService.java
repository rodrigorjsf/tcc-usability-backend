package com.unicap.tcc.usability.api.service;

import com.unicap.tcc.usability.api.exception.ApiException;
import com.unicap.tcc.usability.api.models.Scale;
import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.models.assessment.AssessmentUserGroup;
import com.unicap.tcc.usability.api.models.assessment.answer.PlanAnswers;
import com.unicap.tcc.usability.api.models.dto.AssessmentListDTO;
import com.unicap.tcc.usability.api.models.dto.SmartCityResponse;
import com.unicap.tcc.usability.api.models.dto.assessment.*;
import com.unicap.tcc.usability.api.models.enums.AssessmentState;
import com.unicap.tcc.usability.api.models.enums.UserProfileEnum;
import com.unicap.tcc.usability.api.repository.*;
import com.unicap.tcc.usability.api.utils.PdfGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final UserRepository userRepository;
    private final AssessmentUserGroupRepository assessmentUserGroupRepository;
    private final SmartCityQuestionnaireRepository smartCityQuestionnaireRepository;
    private final ParticipantRepository participantRepository;
    private final ScaleRepository scaleRepository;
    private final ProcedureRepository procedureRepository;
    private final DataRepository dataRepository;
    private final ThreatsRepository threatsRepository;
    private final MailSender mailSender;

    public SmartCityResponse calculateSmartCityPercentage(SmartCityQuestionnaire questionnaire) {
        var resultList = questionnaire.getListOfResults();
        var resultsQuantity = Long.valueOf(resultList.size());
        var positiveResults = Long.valueOf(resultList.stream().filter(aBoolean -> aBoolean.equals(true)).count());
        return SmartCityResponse.builder()
                .smartCityPercentage(positiveResults.doubleValue() == 0 ?
                        0 : (positiveResults.doubleValue() * 100) / resultsQuantity.doubleValue())
                .build();
    }

    public Assessment getAssessmentByUuid(UUID uid) {
        var assessmentOptional = assessmentRepository.findByUid(uid);
        return assessmentOptional.orElse(null);
    }

    public Response getReportStreamingOutput(String id) {
        Assessment assessment = getAssessmentByUuid(UUID.fromString(id));
        if (Objects.isNull(assessment))
            throw new ApiException(Response.Status.NOT_FOUND,
                    "Assessment not found");

        ByteArrayOutputStream byteArrayOutputStream = PdfGenerator.generatePlan(assessment);

        StreamingOutput output = out -> {
            byteArrayOutputStream.writeTo(out);
            out.flush();
            byteArrayOutputStream.close();
        };
        return Response.ok(output, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=" + assessment.getProjectName() + ".pdf").build();
    }

    public Assessment createAssessment(AssessmentCreationDTO assessmentCreationDTO) {
        var user = userRepository.findByUid(assessmentCreationDTO.getUserUid());
        if (user.isEmpty()) {
            return null;
        }
        Assessment assessment = assessmentRepository.save(Assessment.builder()
                .systemUser(user.get())
                .projectName(assessmentCreationDTO.getProjectName())
                .projectDescription(assessmentCreationDTO.getProjectDescription())
                .state(AssessmentState.CREATED)
                .answers(PlanAnswers.newPlanAnswers())
                .build());
        assessmentUserGroupRepository.save(AssessmentUserGroup.builder()
                .assessment(assessment)
                .profile(UserProfileEnum.AUTHOR)
                .systemUser(assessment.getSystemUser())
                .build());
        var optionalUid = assessmentUserGroupRepository.findUidById(assessment.getId());
        optionalUid.ifPresent(s ->
                mailSender.sendCollaboratorEmail(assessment, s, assessmentCreationDTO.getCollaboratorsEmail()));
        return assessment;
    }

    public Assessment addCollaborator(CollaboratorDTO collaboratorDTO) {
        var assessment = assessmentRepository.findByUid(collaboratorDTO.getAssessmentUid());
        if (assessment.isEmpty()) {
            return null;
        }
        var optionalUid = assessmentUserGroupRepository.findUidById(assessment.get().getId());
        optionalUid.ifPresent(s ->
                mailSender.sendCollaboratorEmail(assessment.get(), s, collaboratorDTO.getCollaboratorsEmail()));
        return assessment.get();
    }

    public List<AssessmentListDTO> findUserAssessmentList(UUID uid) {
        List<Assessment> assessmentList;
        assessmentList = assessmentRepository.findBySystemUserUidAndRemovedDateIsNullAndSystemUserRemovedDateIsNull(uid);
        if (assessmentList.isEmpty())
            assessmentList = assessmentRepository.findByCollaboratorUid(uid);
        if (assessmentList.isEmpty())
            return Collections.emptyList();
        return assessmentList.stream().map(assessment ->
                AssessmentListDTO.builder()
                        .assessmentUid(assessment.getUid().toString())
                        .authorName(assessment.getSystemUser().getName())
                        .projectName(assessment.getProjectName())
                        .state(assessment.getState().getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<Assessment> findAssessmentPlanByUid(UUID uid) {
        return assessmentRepository.findByUid(uid);
    }

    public List<Scale> getScaleList() {
        return scaleRepository.findAll();
    }

    public Optional<Assessment> deleteAssessmentPlan(UUID uid) {
        var assessmentOptional = assessmentRepository.findByUid(uid);
        if (assessmentOptional.isPresent()) {
            assessmentOptional.get().setState(AssessmentState.CANCELED);
            assessmentOptional.get().setRemovedDate(LocalDateTime.now());
            return Optional.of(assessmentRepository.save(assessmentOptional.get()));
        }
        return Optional.empty();
    }

    public Assessment addSmartCityQuestionnaire(ApplicationSectionDTO applicationSection) {
        var optionalAssessment = assessmentRepository.findByUid(applicationSection.getAssessmentUid());
        if (optionalAssessment.isPresent()) {
            var assessment = optionalAssessment.get();
            if (Objects.isNull(assessment.getSmartCityQuestionnaire()))
                assessment.setSmartCityQuestionnaire(applicationSection.updateSmartCityQuestionnaire(assessment));
            else
                assessment.getSmartCityQuestionnaire().updateValues(applicationSection);
            assessment.getAnswers().setPlanApplicationAnswers(applicationSection.getPlanApplicationAnswers());
            var resultList = applicationSection.toSmartCityQuestionnaire().getListOfResults();
            var resultsQuantity = Long.valueOf(resultList.size());
            var positiveResults = Long.valueOf(resultList.stream().filter(aBoolean -> aBoolean.equals(true)).count());
            var percentage = positiveResults.doubleValue() == 0 ?
                    0 : (positiveResults.doubleValue() * 100) / resultsQuantity.doubleValue();
            assessment = assessmentRepository.save(assessment);

            assessmentRepository.updateProjectAndAnswers(applicationSection.getProjectName(),
                    applicationSection.getProjectDescription(),
                    percentage,
                    assessment.getUid());
            smartCityQuestionnaireRepository.updateSmartCityQuestionnaire(
                    applicationSection.getDefineCityModel(),
                    applicationSection.getHasAppExecution(),
                    applicationSection.getHasDataAccess(),
                    applicationSection.getHasDataManagement(),
                    applicationSection.getHasDataProcessing(),
                    applicationSection.getHasSensorNetwork(),
                    applicationSection.getHasServiceManagement(),
                    applicationSection.getHasSoftwareTools(),
                    assessment.getId());
            if (!assessment.getState().equals(AssessmentState.COLLECTING_DATA))
                assessment.setState(AssessmentState.COLLECTING_DATA);
            return assessment;
        }
        return null;
    }

    public Assessment addUsabilityGoals(UsabilityGoalDTO usabilityGoalDTO) {
        var optionalAssessment = assessmentRepository.findByUid(usabilityGoalDTO.getAssessmentUid());
        if (optionalAssessment.isPresent()) {
            optionalAssessment.get().setUsabilityGoals(usabilityGoalDTO.toUsabilityGoals(
                    optionalAssessment.get().getUsabilityGoals()));
            optionalAssessment.get().getAnswers().setPlanGoalsAnswers(usabilityGoalDTO.getPlanGoalsAnswers());
            if (!optionalAssessment.get().getState().equals(AssessmentState.COLLECTING_DATA))
                optionalAssessment.get().setState(AssessmentState.COLLECTING_DATA);
            return assessmentRepository.save(optionalAssessment.get());
        }
        return null;
    }

    public Assessment addVariables(AssessmentVariablesDTO assessmentVariablesDTO) {
        var optionalAssessment = assessmentRepository.findByUid(assessmentVariablesDTO.getAssessmentUid());
        if (optionalAssessment.isPresent()) {
            optionalAssessment.get().setAttributes(assessmentVariablesDTO.updateVariableSet(optionalAssessment.get().getAttributes()));
            optionalAssessment.get().getAnswers().setPlanVariableAnswers(assessmentVariablesDTO.getPlanVariableAnswers());
            var scaleList = scaleRepository.findByAcronymIn(assessmentVariablesDTO.getScale());
            if (CollectionUtils.isNotEmpty(scaleList))
                optionalAssessment.get().setScale(scaleList);
            if (!optionalAssessment.get().getState().equals(AssessmentState.COLLECTING_DATA))
                optionalAssessment.get().setState(AssessmentState.COLLECTING_DATA);
            return assessmentRepository.save(optionalAssessment.get());
        }
        return null;
    }

    public Assessment addParticipant(ParticipantDTO participantDTO) {
        var optionalAssessment = assessmentRepository.findByUid(participantDTO.getAssessmentUid());
        if (optionalAssessment.isPresent()) {
            optionalAssessment.get().setParticipant(participantDTO.updateParticipant(optionalAssessment.get().getParticipant()));
            optionalAssessment.get().getAnswers().setPlanParticipantsAnswers(participantDTO.getPlanParticipantsAnswers());
            if (!optionalAssessment.get().getState().equals(AssessmentState.COLLECTING_DATA))
                optionalAssessment.get().setState(AssessmentState.COLLECTING_DATA);
            var assessment = assessmentRepository.save(optionalAssessment.get());
            participantRepository.updateHasCollectedInformation(participantDTO.getHasCollectedInformation(),
                    assessment.getParticipant().getId());
            return assessmentRepository.findByUid(assessment.getUid()).get();
        }
        return null;
    }

    public Assessment addAssessmentTools(AssessmentToolsDTO assessmentToolsDTO) {
        var optionalAssessment = assessmentRepository.findByUid(assessmentToolsDTO.getAssessmentUid());
        if (optionalAssessment.isPresent()) {
            optionalAssessment.get().setAssessmentTools(
                    assessmentToolsDTO.updateAssessmentTools(optionalAssessment.get().getAssessmentTools()));
            optionalAssessment.get().getAnswers().setPlanTasksAnswers(assessmentToolsDTO.getPlanTasksAnswers());
            if (!optionalAssessment.get().getState().equals(AssessmentState.COLLECTING_DATA))
                optionalAssessment.get().setState(AssessmentState.COLLECTING_DATA);
            return assessmentRepository.save(optionalAssessment.get());
        }
        return null;
    }

    public Assessment addAssessmentProcedure(AssessmentProcedureDTO assessmentProcedureDTO) {
        var optionalAssessment = assessmentRepository.findByUid(assessmentProcedureDTO.getAssessmentUid());
        if (optionalAssessment.isPresent()) {
            optionalAssessment.get().setAssessmentProcedure(
                    assessmentProcedureDTO.updateProcedure(optionalAssessment.get().getAssessmentProcedure()));
            optionalAssessment.get().getAnswers().setPlanProcedureAnswers(assessmentProcedureDTO.getPlanProcedureAnswers());
            if (!optionalAssessment.get().getState().equals(AssessmentState.COLLECTING_DATA))
                optionalAssessment.get().setState(AssessmentState.COLLECTING_DATA);
            var assessment = assessmentRepository.save(optionalAssessment.get());
            procedureRepository.updateIsPilotAndQuestionAllowed(assessmentProcedureDTO.getIsPilotAssessment(),
                    assessmentProcedureDTO.getQuestionsAllowed(),
                    assessment.getAssessmentProcedure().getId());
            return assessmentRepository.findByUid(assessment.getUid()).get();
        }
        return null;
    }

    public Assessment addAssessmentData(AssessmentDataDTO assessmentDataDTO) {
        var optionalAssessment = assessmentRepository.findByUid(assessmentDataDTO.getAssessmentUid());
        if (optionalAssessment.isPresent()) {
            optionalAssessment.get().setAssessmentData(
                    assessmentDataDTO.updateDataCollection(optionalAssessment.get().getAssessmentData()));
            optionalAssessment.get().getAnswers().setPlanDataAnswers(assessmentDataDTO.getPlanDataAnswers());
            if (!optionalAssessment.get().getState().equals(AssessmentState.COLLECTING_DATA))
                optionalAssessment.get().setState(AssessmentState.COLLECTING_DATA);
            var assessment = assessmentRepository.save(optionalAssessment.get());
            dataRepository.updateStatisticalMethodFlag(assessmentDataDTO.getStatisticalMethods(),
                    assessment.getAssessmentData().getId());
            return assessmentRepository.findByUid(assessment.getUid()).get();
        }
        return null;
    }

    public Assessment addAssessmentThreats(AssessmentThreatDTO assessmentThreatDTO) {
        var optionalAssessment = assessmentRepository.findByUid(assessmentThreatDTO.getAssessmentUid());
        if (optionalAssessment.isPresent()) {
            optionalAssessment.get().setAssessmentThreat(
                    assessmentThreatDTO.updateThreats(optionalAssessment.get().getAssessmentThreat()));
            optionalAssessment.get().getAnswers().setPlanThreatsAnswers(assessmentThreatDTO.getPlanThreatsAnswers());
            if (!optionalAssessment.get().getState().equals(AssessmentState.COLLECTING_DATA))
                optionalAssessment.get().setState(AssessmentState.COLLECTING_DATA);
            var assessment = assessmentRepository.save(optionalAssessment.get());
            threatsRepository.updateEthicalAspectsDefined(assessmentThreatDTO.getEthicalAspectsDefined(),
                    assessment.getAssessmentData().getId());
            return assessmentRepository.findByUid(assessment.getUid()).get();
        }
        return null;
    }

    public Optional<Assessment> finishPlanDataCollection(UUID uid) {
        var optionalAssessment = assessmentRepository.findByUid(uid);
        if (optionalAssessment.isPresent()) {
            optionalAssessment.get().setState(AssessmentState.COMPLETED);
            return Optional.of(assessmentRepository.save(optionalAssessment.get()));
        }
        return Optional.empty();
    }

    public Optional<ByteArrayOutputStream> downloadPlan(UUID uid) {
        var optionalAssessment = assessmentRepository.findByUid(uid);
        //            Path path = Paths.get(planFile.get().getAbsolutePath());
        //            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        //            try(OutputStream outputStream = new FileOutputStream("thefilename")) {
        //                fileOutputByteArray.writeTo(outputStream);
        //            } catch (IOException e) {
        //                e.printStackTrace();
        //            }
        return optionalAssessment.map(PdfGenerator::generatePlan);
    }

    public String findPlanProjectName(UUID uid) {
        var assessmentOptional = assessmentRepository.findByUid(uid);
        if (assessmentOptional.isPresent()) {
            return assessmentOptional.get().getProjectName();
        }
        return "assessmentUsabiity";
    }
}
