package com.unicap.tcc.usability.api.models.enums;

import lombok.Getter;

@Getter
public enum UsabilityAttribute {
    LEARNABILITY("Learnability"),
    EFFICIENCY("Efficiency"),
    USER_RETENTION("UserRetentionOT"),
    ERROR_RATE("ErrorRate"),
    SATISFACTION("Satisfaction");

    private final String description;

    UsabilityAttribute(String description){
        this.description = description;
    }
}
