package com.unicap.tcc.usability.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicap.tcc.usability.api.models.enums.CategoriesEnum;
import com.unicap.tcc.usability.api.models.enums.ScalesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "assessment_questions")
public class AssessmentQuestion extends BaseEntity {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", columnDefinition = "varchar(2)", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoriesEnum category;


    @Column(name = "instruction", columnDefinition = "text")
    private String instruction;

    @Column
    private String question;

    @Column(name = "hint", columnDefinition = "text")
    private String hint;

}


