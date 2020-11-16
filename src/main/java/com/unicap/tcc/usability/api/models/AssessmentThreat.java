package com.unicap.tcc.usability.api.models;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "assessment_threat")
@TypeDefs({
        @TypeDef(
                name = "list-array",
                typeClass = ListArrayType.class
        ),
})
public class AssessmentThreat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_tools_id", insertable = false, updatable = false)
    private Long assessmentToolsId;

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
            name = "threats",
            columnDefinition = "text[]"
    )
    private List<String> threats;

    @Column(name = "control_measure", columnDefinition = "text")
    private String controlMeasure;

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
            name = "limitations",
            columnDefinition = "text[]"
    )
    private List<String> limitations;

    @Column(name = "ethical_aspects_defined", columnDefinition = "boolean default false", insertable = false)
    @Generated(GenerationTime.ALWAYS)
    private Boolean ethicalAspectsDefined;

    @Column(name = "ethical_aspects_description", columnDefinition = "text")
    private String ethicalAspectsDescription;

    @Column(name = "bias_description", columnDefinition = "text")
    private String biasDescription;
}
