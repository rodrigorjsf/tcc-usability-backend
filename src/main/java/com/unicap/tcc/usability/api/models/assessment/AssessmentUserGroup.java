package com.unicap.tcc.usability.api.models.assessment;

import com.unicap.tcc.usability.api.models.BaseEntity;
import com.unicap.tcc.usability.api.models.SmartCityQuestionnaire;
import com.unicap.tcc.usability.api.models.User;
import com.unicap.tcc.usability.api.models.enums.ParticipationLocalType;
import com.unicap.tcc.usability.api.models.enums.UserProfileEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "assessment_user_group")
public class AssessmentUserGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY)
    private User systemUser;

    @OneToOne(fetch=FetchType.LAZY)
    private Assessment assessment;

    @Column(name = "profile", columnDefinition = "varchar(15)", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserProfileEnum profile;
}


