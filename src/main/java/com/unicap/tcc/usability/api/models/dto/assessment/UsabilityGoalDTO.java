package com.unicap.tcc.usability.api.models.dto.assessment;

import com.unicap.tcc.usability.api.models.assessment.UsabilityGoal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsabilityGoalDTO {

    private UUID assessmentUid;
    private List<GoalDTO> goals;

    public List<UsabilityGoal> toUsabilityGoals() {
        return goals.stream().map(goalDTO ->
                UsabilityGoal.builder()
                        .attribute(goalDTO.getAttribute())
                        .goal(goalDTO.getGoal())
                        .build())
                .collect(Collectors.toList());
    }
}
