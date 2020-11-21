package com.unicap.tcc.usability.api.models.dto.assessment;

import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmartCityQuestionnaireDTO {

    private UUID assessmentUid;
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

    public void updateSmartCityQuestionnaire(SmartCityQuestionnaire cityQuestionnaire) {
        cityQuestionnaire.setHasDataManagement(this.hasDataManagement);
        cityQuestionnaire.setHasAppExecution(this.hasAppExecution);
        cityQuestionnaire.setHasSensorNetwork(this.hasSensorNetwork);
        cityQuestionnaire.setHasDataProcessing(this.hasDataProcessing);
        cityQuestionnaire.setHasDataAccess(this.hasDataAccess);
        cityQuestionnaire.setHasServiceManagement(this.hasServiceManagement);
        cityQuestionnaire.setHasSoftwareTools(this.hasSoftwareTools);
        cityQuestionnaire.setDefineCityModel(this.defineCityModel);
    }

}


