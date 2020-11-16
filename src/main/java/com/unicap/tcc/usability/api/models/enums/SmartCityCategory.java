package com.unicap.tcc.usability.api.models.enums;

import lombok.Getter;

@Getter
public enum SmartCityCategory {
    DATA_MANAGEMENT("DataManagement"),
    APPLICATION_EXECUTION_ENV("ApplicationExecutionEnvironment"),
    SENSOR_NETWORK_MANAGEMENT("SensorNetworkManagement"),
    DATA_PROCESSING("DataProcessing"),
    DATA_ACCESS("DataAccess"),
    SERVICE_MANAGEMENT("ServiceManagement"),
    TOOLS_SOFTWARE_DEVELOPMENT("ToolsforSoftwareDevelopment"),
    DEFINING_CITY_MODEL("DefiningACityModel");

    private final String description;

    SmartCityCategory(String description){
        this.description = description;
    }
}
