package com.unicap.tcc.usability.api.models.assessment;

import com.unicap.tcc.usability.api.models.enums.UsabilityAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usabiliy_goals")
public class UsabilityGoal {

    @JsonIgnore
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
    @Column(name = "done", columnDefinition = "boolean default false")
    @Generated(GenerationTime.ALWAYS)
    private Boolean done;
}
