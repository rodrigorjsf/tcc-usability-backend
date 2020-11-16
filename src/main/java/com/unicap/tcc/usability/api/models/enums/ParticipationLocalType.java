package com.unicap.tcc.usability.api.models.enums;

import lombok.Getter;

@Getter
public enum ParticipationLocalType {
    P("InPerson"),
    R("Remote");

    private final String description;

    ParticipationLocalType(String description){
        this.description = description;
    }
}
