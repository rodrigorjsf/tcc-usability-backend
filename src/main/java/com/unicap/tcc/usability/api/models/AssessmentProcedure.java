package com.unicap.tcc.usability.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "assessment_procedure")
public class AssessmentProcedure extends BaseEntity {

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

    @Column(name = "will_have_training", columnDefinition = "boolean default false", insertable = false)
    @Generated(GenerationTime.ALWAYS)
    private Boolean willHaveTraining;

    @Column(name = "is_pilot_assessment", columnDefinition = "boolean default false", insertable = false)
    @Generated(GenerationTime.ALWAYS)
    private Boolean isPilotAssessment;

    @Column
    private String pilotDescription;
}
