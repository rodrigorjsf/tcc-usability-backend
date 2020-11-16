package com.unicap.tcc.usability.api.models;

import com.unicap.tcc.usability.api.models.enums.Scales;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "scale")
@TypeDefs({
        @TypeDef(
                name = "list-array",
                typeClass = ListArrayType.class
        ),
})
public class Scale extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "acronym", columnDefinition = "varchar(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Scales acronym;

    @Column
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

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
            name = "measures",
            columnDefinition = "text[]"
    )
    private List<String> measures;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "scale_id", referencedColumnName = "id", nullable = false)
    private List<ScaleQuestions> scaleQuestions;
}


