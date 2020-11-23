package com.unicap.tcc.usability.api.models.assessment.answer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanAnswers {
    private PlanApplicationAnswers planApplicationAnswers;
    private PlanGoalsAnswers planGoalsAnswers;
    private PlanVariableAnswers planVariableAnswers;
    private PlanParticipantsAnswers planParticipantsAnswers;
    private PlanTasksAnswers planTasksAnswers;
    private PlanProcedureAnswers planProcedureAnswers;
    private PlanDataAnswers planDataAnswers;
    private PlanThreatsAnswers planThreatsAnswers;

}
