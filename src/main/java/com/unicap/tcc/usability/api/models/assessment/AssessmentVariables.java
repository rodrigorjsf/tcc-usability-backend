package com.unicap.tcc.usability.api.models.assessment;

import com.unicap.tcc.usability.api.models.BaseEntity;
import com.unicap.tcc.usability.api.models.Scale;
import com.unicap.tcc.usability.api.models.enums.UsabilityAttribute;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "assessment_variables")
@TypeDefs({
        @TypeDef(
                name = "list-array",
                typeClass = ListArrayType.class
        ),
})
public @Data
class AssessmentVariables extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "assessment_variables_id", referencedColumnName = "id", nullable = false)
    private List<Variable> variables;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "av_scale",
            joinColumns = @JoinColumn(name = "attribute_variable_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "scale_id", referencedColumnName = "id"))
    private List<Scale> scale;
}
