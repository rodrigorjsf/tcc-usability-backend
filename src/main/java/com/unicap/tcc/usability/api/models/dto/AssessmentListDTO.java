package com.unicap.tcc.usability.api.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentListDTO {
    private String assessmentUid;
    private String projectName;
    private String authorName;
    private String state;
}
