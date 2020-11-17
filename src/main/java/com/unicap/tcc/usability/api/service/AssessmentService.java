package com.unicap.tcc.usability.api.service;


import com.unicap.tcc.usability.api.exception.ApiException;
import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.models.assessment.AssessmentThreat;
import com.unicap.tcc.usability.api.models.dto.SmartCityResponse;
import com.unicap.tcc.usability.api.repository.AssessmentRepository;
import com.unicap.tcc.usability.api.utils.PdfGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;

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
}
