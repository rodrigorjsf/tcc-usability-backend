package com.unicap.tcc.usability.api.models.assessment;

import com.unicap.tcc.usability.api.models.BaseEntity;
import com.unicap.tcc.usability.api.models.enums.ParticipationLocalType;
import com.unicap.tcc.usability.api.models.enums.ParticipationType;
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
@Table(name = "participants")
@TypeDefs({
        @TypeDef(
                name = "list-array",
                typeClass = ListArrayType.class
        ),
})
public class Participant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Integer participantsQuantity;

    @Column(name = "participation_local_type", columnDefinition = "varchar(1)", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipationLocalType participationLocalType;

    @Column(name = "participation_type", columnDefinition = "varchar(1)", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipationType participationType;

    @Column
    private String compensationDescription;

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
            name = "eligibility_criteria",
            columnDefinition = "text[]"
    )
    private List<String> criteriaList;


    @Column(name = "has_collected_information", columnDefinition = "boolean default false", insertable = false)
    @Generated(GenerationTime.ALWAYS)
    private Boolean hasCollectedInformation;

    @Column
    private String collectedInformationUse;

    @Column
    private String instructions;

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
            name = "questions",
            columnDefinition = "text[]"
    )
    private List<String> questions;

}
