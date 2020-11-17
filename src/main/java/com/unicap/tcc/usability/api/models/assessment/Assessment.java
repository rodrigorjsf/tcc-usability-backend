package com.unicap.tcc.usability.api.models.assessment;

import com.unicap.tcc.usability.api.models.BaseEntity;
import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import com.unicap.tcc.usability.api.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "assessment")
public class Assessment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "uuid default uuid_generate_v4()", insertable = false)
    private UUID uid;

    @OneToOne(fetch=FetchType.LAZY)
    private User systemUser;
    ////////// GOALS
    @Column
    private String projectName;
    @Column
    private String projectDescription;
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "assessment_id", referencedColumnName = "id", nullable = false)
    private List<UsabilityGoal> usabilityGoals;
    @Column
    private Boolean isSmartCity;
    @Column
    private Double smartCityPercentage;
    @OneToOne(cascade = {CascadeType.ALL})
    private SmartCityQuestionnaire smartCityQuestionnaire;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "assessment_id", referencedColumnName = "id", nullable = false)
    private List<AttributeVariable> attributeVariables;

    @OneToOne(cascade = {CascadeType.ALL})
    private Participant participant;

    @OneToOne(cascade = {CascadeType.ALL})
    private AssessmentTools assessmentTools;

    @OneToOne(cascade = {CascadeType.ALL})
    private AssessmentProcedure assessmentProcedure;

    @OneToOne(cascade = {CascadeType.ALL})
    private AssessmentData assessmentData;

    @OneToOne(cascade = {CascadeType.ALL})
    private AssessmentThreat assessmentThreat;
}


