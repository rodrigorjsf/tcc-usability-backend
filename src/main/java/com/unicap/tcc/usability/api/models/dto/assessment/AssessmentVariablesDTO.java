package com.unicap.tcc.usability.api.models.dto.assessment;

import com.unicap.tcc.usability.api.models.assessment.Variable;
import com.unicap.tcc.usability.api.models.assessment.answer.PlanVariableAnswers;
import com.unicap.tcc.usability.api.models.enums.ScalesEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data
class AssessmentVariablesDTO {

    private UUID assessmentUid;
    private List<VariableDTO> variables;
    private List<ScalesEnum> scale;
    private PlanVariableAnswers planVariableAnswers;

    public Set<Variable> updateVariableSet(Set<Variable> variables) {
        if (CollectionUtils.isEmpty(variables)) {
            return this.variables.stream()
                    .filter(variableDTO -> StringUtils.isNotEmpty(variableDTO.getVariables())
                            || StringUtils.isNotEmpty(variableDTO.getObtainedBy()))
                    .map(variableDTO -> Variable.builder()
                            .obtainedBy(variableDTO.getObtainedBy())
                            .variables(variableDTO.getVariables())
                            .usabilityAttribute(variableDTO.getUsabilityAttribute())
                            .build())
                    .collect(Collectors.toSet());
        }
        return variables.stream().peek(variable ->
                this.variables.forEach(variableDTO -> {
                    if (variableDTO.getUsabilityAttribute() == variable.getUsabilityAttribute()) {
                        variable.setObtainedBy(variableDTO.getObtainedBy());
                        variable.setVariables(variableDTO.getVariables());
                    }
                })).collect(Collectors.toSet());

    }
}
