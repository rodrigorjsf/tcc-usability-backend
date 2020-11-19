package com.unicap.tcc.usability.api.models.dto.assessment;

import com.unicap.tcc.usability.api.models.enums.UsabilityAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalDTO {

    private UsabilityAttribute attribute;
    private String goal;
}
