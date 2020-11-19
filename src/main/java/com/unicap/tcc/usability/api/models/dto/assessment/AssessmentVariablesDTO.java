package com.unicap.tcc.usability.api.models.dto.assessment;

import com.unicap.tcc.usability.api.models.assessment.AssessmentVariables;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data
class AssessmentVariablesDTO {

    private UUID assessmentUid;
    private List<VariableDTO> variables;
    private List<String> scale;

    public AssessmentVariables toAssessmentVariables(){
        return AssessmentVariables.builder()
                .build();
    }
}
