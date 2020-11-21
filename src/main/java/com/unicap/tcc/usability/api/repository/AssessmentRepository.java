package com.unicap.tcc.usability.api.repository;

import com.unicap.tcc.usability.api.models.assessment.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    Optional<Assessment> findByUid(UUID uuid);

    List<Assessment> findBySystemUserUidAndRemovedDateIsNullAndSystemUserRemovedDateIsNull(UUID uid);

    @Query(nativeQuery = true, value = "SELECT a.*\n" +
            "FROM assessment a\n" +
            "         INNER JOIN assessment_user_group aug on a.id = aug.assessment_id\n" +
            "         INNER JOIN sys_user su on su.id = aug.system_user_id\n" +
            "WHERE su.uid = :uid \n" +
            "  AND a.removed_at IS NULL\n" +
            "  AND su.removed_at IS NULL;")
    List<Assessment> findByCollaboratorUid(UUID uid);

}
