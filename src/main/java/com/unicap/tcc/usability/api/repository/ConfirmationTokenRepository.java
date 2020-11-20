package com.unicap.tcc.usability.api.repository;

import com.unicap.tcc.usability.api.models.ConfirmationToken;
import com.unicap.tcc.usability.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    ConfirmationToken findByUid(UUID uid);

    @Query(nativeQuery = true, value = "SELECT text(uid) as uid FROM confirmation_token WHERE id = :id")
    Optional<String> findUidById(Long id);
}
