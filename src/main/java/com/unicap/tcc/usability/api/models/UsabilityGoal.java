package com.unicap.tcc.usability.api.models;

import com.unicap.tcc.usability.api.models.enums.UsabilityAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usabiliy_goals")
public class UsabilityGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="assessment_id", insertable = false, updatable = false)
    private Long assessmentId;
    @Column(name = "attribute", nullable = false)
    @Enumerated(EnumType.STRING)
    private UsabilityAttribute attribute;
    @Column
    private String goal;
}
