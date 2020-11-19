package com.unicap.tcc.usability.api.models.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicap.tcc.usability.api.models.BaseEntity;
import com.unicap.tcc.usability.api.models.User;
import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.models.enums.EReviewState;
import com.unicap.tcc.usability.api.models.enums.EScore;
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
@Table(name = "review_item")
public class ReviewItem extends BaseEntity {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state", columnDefinition = "varchar(15)", nullable = false)
    @Enumerated(EnumType.STRING)
    private EScore score;

    @OneToOne(fetch=FetchType.LAZY)
    private Review review;

    @Column(name = "comment", columnDefinition = "text")
    private String comment;
}


