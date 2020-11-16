package com.unicap.tcc.usability.api.models;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "assessment_data")
@TypeDefs({
        @TypeDef(
                name = "list-array",
                typeClass = ListArrayType.class
        ),
})
public class AssessmentData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String dataCollectionProcedure;

    @Column(name = "questions_allowed", columnDefinition = "boolean default false", insertable = false)
    @Generated(GenerationTime.ALWAYS)
    private Boolean questionsAllowed;

    @Column(name = "analysis_description", columnDefinition = "text")
    private String analysisDescription;

    @Column(name = "statistical_methods", columnDefinition = "boolean default false", insertable = false)
    @Generated(GenerationTime.ALWAYS)
    private Boolean statisticalMethods;

    @Column(name = "statistical_methods_description", columnDefinition = "text")
    private String statisticalMethodsDescription;
}
