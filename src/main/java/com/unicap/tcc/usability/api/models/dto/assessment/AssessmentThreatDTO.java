package com.unicap.tcc.usability.api.models.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentThreatDTO {

    private List<String> threats;
    private String controlMeasure;
    private List<String> limitations;
    private Boolean ethicalAspectsDefined;
    private String ethicalAspectsDescription;
    private String biasDescription;
}
