package com.unicap.tcc.usability.api.models.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicap.tcc.usability.api.models.BaseEntity;
import com.unicap.tcc.usability.api.models.User;
import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.models.enums.EReviewState;
import com.unicap.tcc.usability.api.models.enums.ScalesEnum;
import com.unicap.tcc.usability.api.models.enums.UserProfileEnum;
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
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "review")
public class Review extends BaseEntity {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state", columnDefinition = "varchar(15)", nullable = false)
    @Enumerated(EnumType.STRING)
    private EReviewState state;

    @OneToOne(fetch=FetchType.LAZY)
    private User systemUser;

    @OneToOne(fetch=FetchType.LAZY)
    private Assessment assessment;
}


