package com.unicap.tcc.usability.api.models.enums;

import lombok.Getter;

@Getter
public enum CategoriesEnum {
    SC("SmartCity"),
    UA("UsabilityAttribute");

    private final String description;

    CategoriesEnum(String description){
        this.description = description;
    }
}
