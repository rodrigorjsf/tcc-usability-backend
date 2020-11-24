package com.unicap.tcc.usability.api.repository;

import com.unicap.tcc.usability.api.models.Scale;
import com.unicap.tcc.usability.api.models.enums.ScalesEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScaleRepository extends JpaRepository<Scale, Long> {

    Optional<Scale> findByAcronym(ScalesEnum scale);

    Optional<Scale> findByUid(UUID uuid);

    List<Scale> findByAcronymIn(List<ScalesEnum> acronyms);
}
