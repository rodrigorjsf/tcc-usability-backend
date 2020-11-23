package com.unicap.tcc.usability.api.models.assessment;

import com.unicap.tcc.usability.api.models.BaseEntity;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "assessment_procedure")
@TypeDefs({
        @TypeDef(
                name = "list-array",
                typeClass = ListArrayType.class
        ),
})
public class AssessmentProcedure extends BaseEntity {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate occurDate;

    @Column
    private String occurLocal;

    @Column
    private String occurDetail;

    @Column
    private Integer occurTime;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "assessment_procedure_id", referencedColumnName = "id", nullable = false)
    private List<AssessmentProcedureStep> assessmentProcedureSteps;

    @Column(name = "is_pilot_assessment", columnDefinition = "boolean default false")
    @Generated(GenerationTime.ALWAYS)
    private Boolean isPilotAssessment;

    @Column
    private String pilotDescription;

    @Column(name = "questions_allowed", columnDefinition = "boolean default false")
    @Generated(GenerationTime.ALWAYS)
    private Boolean questionsAllowed;
}
