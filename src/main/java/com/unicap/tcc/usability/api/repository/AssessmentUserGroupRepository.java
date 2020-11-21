package com.unicap.tcc.usability.api.repository;

import com.unicap.tcc.usability.api.models.assessment.AssessmentUserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AssessmentUserGroupRepository extends JpaRepository<AssessmentUserGroup, Long> {

    @Query(nativeQuery = true, value = "SELECT text(uid) as uid FROM assessment WHERE id = :id")
    Optional<String> findUidById(Long id);

}
