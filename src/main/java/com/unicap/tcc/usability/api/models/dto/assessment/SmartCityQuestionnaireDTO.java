package com.unicap.tcc.usability.api.models.dto.assessment;

import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmartCityQuestionnaireDTO {

    private Boolean hasDataManagement;
    private Boolean hasAppExecution;
    private Boolean hasSensorNetwork;
    private Boolean hasDataProcessing;
    private Boolean hasDataAccess;
    private Boolean hasServiceManagement;
    private Boolean hasSoftwareTools;
    private Boolean defineCityModel;

    public SmartCityQuestionnaire toSmartCityQuestionnaire() {
        return SmartCityQuestionnaire.builder()
                .hasDataManagement(this.hasDataManagement)
                .hasAppExecution(this.hasAppExecution)
                .hasSensorNetwork(this.hasSensorNetwork)
                .hasDataProcessing(this.hasDataProcessing)
                .hasDataAccess(this.hasDataAccess)
                .hasServiceManagement(this.hasServiceManagement)
                .hasSoftwareTools(this.hasSoftwareTools)
                .defineCityModel(this.defineCityModel)
                .build();
    }

}


