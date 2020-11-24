package com.unicap.tcc.usability.api.models.assessment.answer;

import com.unicap.tcc.usability.api.models.assessment.AssessmentProcedureStep;
import com.unicap.tcc.usability.api.models.assessment.answer.enums.AnswerEnum;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanProcedureAnswers {
    private AnswerEnum whenOccur;
    private AnswerEnum whereOccur;
    private AnswerEnum howOccur;
    private AnswerEnum howMuchTime;
    private AnswerEnum assessmentProcedureSteps;
    private AnswerEnum isPilotAssessment;
    private AnswerEnum questionsAllowed;
}
