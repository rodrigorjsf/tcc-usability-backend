package com.unicap.tcc.usability.api.service;


import com.unicap.tcc.usability.api.exception.ApiException;
import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.models.assessment.AssessmentUserGroup;
import com.unicap.tcc.usability.api.models.dto.assessment.AssessmentVariablesDTO;
import com.unicap.tcc.usability.api.models.dto.assessment.AssessmentCreationDTO;
import com.unicap.tcc.usability.api.models.dto.SmartCityResponse;
import com.unicap.tcc.usability.api.models.dto.assessment.CollaboratorDTO;
import com.unicap.tcc.usability.api.models.dto.assessment.UsabilityGoalDTO;
import com.unicap.tcc.usability.api.models.enums.UserProfileEnum;
import com.unicap.tcc.usability.api.repository.AssessmentRepository;
import com.unicap.tcc.usability.api.repository.AssessmentUserGroupRepository;
import com.unicap.tcc.usability.api.repository.UserRepository;
import com.unicap.tcc.usability.api.utils.PdfGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final UserRepository userRepository;
    private final AssessmentUserGroupRepository assessmentUserGroupRepository;
    private final MailSender mailSender;

    public SmartCityResponse calculateSmartCityPercentage(SmartCityQuestionnaire questionnaire) {
        var resultList = questionnaire.getListOfResults();
        var resultsQuantity = Long.valueOf(resultList.size());
        var positiveResults = Long.valueOf(resultList.stream().filter(aBoolean -> aBoolean.equals(true)).count());
        return SmartCityResponse.builder()
                .smartCityPercentage(resultsQuantity.doubleValue() / positiveResults.doubleValue())
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
                .build());
        assessmentUserGroupRepository.save(AssessmentUserGroup.builder()
                .assessment(assessment)
                .profile(UserProfileEnum.AUTHOR)
                .systemUser(assessment.getSystemUser())
                .build());
        mailSender.sendCollaboratorEmail(assessment, assessmentCreationDTO.getCollaboratorsEmail());
        return assessment;
    }

    public Assessment addCollaborator(CollaboratorDTO collaboratorDTO) {
        var assessment = assessmentRepository.findByUid(collaboratorDTO.getAssessmentUid());
        if (assessment.isEmpty()) {
            return null;
        }
        mailSender.sendCollaboratorEmail(assessment.get(), collaboratorDTO.getCollaboratorsEmail());
        return assessment.get();
    }

    public Assessment addAssessmentGoals(UsabilityGoalDTO usabilityGoals) {
        var optionalAssessment = assessmentRepository.findByUid(usabilityGoals.getAssessmentUid());
        if (optionalAssessment.isPresent()) {
            optionalAssessment.get().setUsabilityGoals(usabilityGoals.toUsabilityGoals());
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
