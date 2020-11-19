package com.unicap.tcc.usability.api.models.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
public class AssessmentDataDTO {

    private String dataCollectionProcedure;
    private String analysisDescription;
    private Boolean statisticalMethods;
    private String statisticalMethodsDescription;
}
