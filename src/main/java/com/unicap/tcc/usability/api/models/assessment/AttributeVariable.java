package com.unicap.tcc.usability.api.models.assessment;

import com.unicap.tcc.usability.api.models.BaseEntity;
import com.unicap.tcc.usability.api.models.Scale;
import com.unicap.tcc.usability.api.models.enums.ScalesEnum;
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
@Table(name = "attributes_variable")
@TypeDefs({
        @TypeDef(
                name = "list-array",
                typeClass = ListArrayType.class
        ),
})
public @Data
class AttributeVariable extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id", insertable = false, updatable = false)
    private Long assessmentId;

    @Column(name = "usability_attribute", columnDefinition = "varchar(20)", nullable = false)
    @Enumerated(EnumType.STRING)
    private UsabilityAttribute usabilityAttribute;

    @Type(
            type = "com.vladmihalcea.hibernate.type.array.ListArrayType",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = ListArrayType.SQL_ARRAY_TYPE,
                            value = "text"
                    )
            }
    )
    @Column(
            name = "variable_list",
            columnDefinition = "text[]"
    )
    private List<String> variableList;

    @Column
    private String obtainedBy;

    @Column
    private String methods;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "scale_id", referencedColumnName = "id", nullable = false)
    private Scale scale;
}
