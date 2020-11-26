package com.unicap.tcc.usability.api.models.enums;

import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum SectionEnum {
    AP("Application"),
    GO("Goals"),
    VM("VariablesAndMeasurement"),
    PA("Participants"),
    TM("TasksAndMaterials"),
    PR("Procedure"),
    DT("DataCollectionAndAnalysis"),
    TH("Threats");

    private final String description;

    SectionEnum(String description) {
        this.description = description;
    }

    public static SectionEnum convert(String name) {
        for (SectionEnum value : SectionEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }

    public static Set<SectionEnum> getSectionList() {
        return Stream.of(SectionEnum.values()).collect(Collectors.toSet());
    }
}
