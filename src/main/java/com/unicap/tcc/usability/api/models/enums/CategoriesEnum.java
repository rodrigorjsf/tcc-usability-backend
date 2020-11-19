package com.unicap.tcc.usability.api.models.enums;

import lombok.Getter;

@Getter
public enum CategoriesEnum {
    AP("Application"),
    SC("SmartCity"),
    GO("Goals"),
    VM("VariablesAndMeasurement"),
    PA("Participants"),
    TM("TasksAndMaterials"),
    PR("Procedure"),
    DT("DataCollectionAndAnalysis"),
    TH("Threats");

    private final String description;

    CategoriesEnum(String description){
        this.description = description;
    }
}