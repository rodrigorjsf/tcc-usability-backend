package com.unicap.tcc.usability.api.models.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicap.tcc.usability.api.models.BaseEntity;
import com.unicap.tcc.usability.api.models.User;
import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.models.enums.EReviewState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
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


