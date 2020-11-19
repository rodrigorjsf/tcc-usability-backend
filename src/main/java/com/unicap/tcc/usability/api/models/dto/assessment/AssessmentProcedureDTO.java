package com.unicap.tcc.usability.api.models.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentProcedureDTO{

    private LocalDate occurDate;
    private String occurLocal;
    private String occurDetail;
    private Integer occurTime;
    private List<AssessmentProcedureStepDTO> assessmentProcedureStepDTOS;
    private Boolean isPilotAssessment;
    private String pilotDescription;
    private Boolean questionsAllowed;
}
