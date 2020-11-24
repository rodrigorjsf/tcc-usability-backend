package com.unicap.tcc.usability.api.service;


import com.unicap.tcc.usability.api.exception.ApiException;
import com.unicap.tcc.usability.api.models.Scale;
import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.models.assessment.AssessmentUserGroup;
import com.unicap.tcc.usability.api.models.assessment.answer.PlanAnswers;
import com.unicap.tcc.usability.api.models.dto.AssessmentListDTO;
import com.unicap.tcc.usability.api.models.dto.SmartCityResponse;
import com.unicap.tcc.usability.api.models.dto.assessment.AssessmentCreationDTO;
import com.unicap.tcc.usability.api.models.dto.assessment.CollaboratorDTO;
import com.unicap.tcc.usability.api.models.dto.assessment.SmartCityQuestionnaireDTO;
import com.unicap.tcc.usability.api.models.dto.assessment.UsabilityGoalDTO;
import com.unicap.tcc.usability.api.models.enums.AssessmentState;
import com.unicap.tcc.usability.api.models.enums.UserProfileEnum;
import com.unicap.tcc.usability.api.repository.AssessmentRepository;
import com.unicap.tcc.usability.api.repository.AssessmentUserGroupRepository;
import com.unicap.tcc.usability.api.repository.ScaleRepository;
import com.unicap.tcc.usability.api.repository.UserRepository;
import com.unicap.tcc.usability.api.utils.PdfGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final UserRepository userRepository;
    private final AssessmentUserGroupRepository assessmentUserGroupRepository;
    private final ScaleRepository scaleRepository;
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

        ByteArrayOutputStream byteArrayOutputStream = PdfGenerator.generatePlanReport(assessment);

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

    public Assessment addSmartCityQuestionnaire(SmartCityQuestionnaireDTO questionnaire) {
        var optionalAssessment = assessmentRepository.findByUid(questionnaire.getAssessmentUid());
        if (optionalAssessment.isPresent()) {
            optionalAssessment.get().setSmartCityQuestionnaire(questionnaire.updateSmartCityQuestionnaire());
            var resultList = questionnaire.toSmartCityQuestionnaire().getListOfResults();
            var resultsQuantity = Long.valueOf(resultList.size());
            var positiveResults = Long.valueOf(resultList.stream().filter(aBoolean -> aBoolean.equals(true)).count());
            optionalAssessment.get().getAnswers().setPlanApplicationAnswers(questionnaire.getPlanApplicationAnswers());
            optionalAssessment.get().setSmartCityPercentage(positiveResults.doubleValue() == 0 ?
                    0 : (positiveResults.doubleValue() * 100) / resultsQuantity.doubleValue());
            if (!optionalAssessment.get().getState().equals(AssessmentState.COLLECTING_DATA))
                optionalAssessment.get().setState(AssessmentState.COLLECTING_DATA);
            return assessmentRepository.save(optionalAssessment.get());
        }
        return null;
    }

    public List<Scale> getScaleList () {
        return scaleRepository.findAll();
    }

    public Optional<Assessment> deleteAssessmentPlan(UUID uid) {
        var assessmentOptional = assessmentRepository.findByUid(uid);
        if (assessmentOptional.isPresent()){
            assessmentOptional.get().setState(AssessmentState.CANCELED);
            assessmentOptional.get().setRemovedDate(LocalDateTime.now());
            return Optional.of(assessmentRepository.save(assessmentOptional.get()));
        }
        return Optional.empty();
    }

    public Assessment addUsabilityGoals(UsabilityGoalDTO usabilityGoalDTO) {
        var optionalAssessment = assessmentRepository.findByUid(usabilityGoalDTO.getAssessmentUid());
        if (optionalAssessment.isPresent()) {
            optionalAssessment.get().setUsabilityGoals(usabilityGoalDTO.toUsabilityGoals());
            optionalAssessment.get().getAnswers().setPlanGoalsAnswers(usabilityGoalDTO.getPlanGoalsAnswers());
            if (!optionalAssessment.get().getState().equals(AssessmentState.COLLECTING_DATA))
                optionalAssessment.get().setState(AssessmentState.COLLECTING_DATA);
            return assessmentRepository.save(optionalAssessment.get());
        }
        return null;
    }


//    public Assessment addAssessmentAttributeVariables(AssessmentVariablesDTO assessmentVariablesDTO) {
//        var optionalAssessment = assessmentRepository.findByUid(assessmentVariablesDTO.getAssessmentUid());
//        if (optionalAssessment.isPresent()) {
//            optionalAssessment.get().setAttributeAssessmentVariables(usabilityGoals.toUsabilityGoals());
//            return assessmentRepository.save(optionalAssessment.get());
//        }
//        return null;
//    }


}
