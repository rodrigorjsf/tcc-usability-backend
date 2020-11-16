package com.unicap.tcc.usability.api.models;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "assessment_tools")
@TypeDefs({
        @TypeDef(
                name = "list-array",
                typeClass = ListArrayType.class
        ),
})
public class AssessmentTools extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
            name = "tools",
            columnDefinition = "text[]"
    )
    private List<String> tools;

    @Column(name = "tools_usage_description", columnDefinition = "text", insertable = false)
    private String toolsUsageDescription;


    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "assessment_tools_id", referencedColumnName = "id", nullable = false)
    private List<Task> tasks;

}
