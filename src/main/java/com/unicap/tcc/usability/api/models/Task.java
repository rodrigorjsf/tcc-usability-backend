package com.unicap.tcc.usability.api.models;

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
@Table(name = "assessment_tasks")
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_tools_id", insertable = false, updatable = false)
    private Long assessmentToolsId;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column
    private Integer taskExecutionTime;

    @Column
    private String acceptanceCriteria;
}
