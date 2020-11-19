package com.unicap.tcc.usability.api.models.enums;

import com.unicap.tcc.usability.api.models.Scale;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public enum ScalesEnum {
    SUMI,
    SUS,
    CSUQ,
    SCAUS,
    QUIS;

    public static Optional<ScalesEnum> convert(String name) {
        for (ScalesEnum scale : ScalesEnum.values()) {
            if (scale.name().equalsIgnoreCase(name)) {
                return Optional.of(scale);
            }
        }
        return Optional.empty();
    }
}
