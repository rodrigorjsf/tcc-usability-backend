package com.unicap.tcc.usability.api.repository;

import com.unicap.tcc.usability.api.models.assessment.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    Optional<Assessment> findByUid(UUID uuid);
}
