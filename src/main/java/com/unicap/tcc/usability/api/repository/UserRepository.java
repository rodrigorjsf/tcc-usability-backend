package com.unicap.tcc.usability.api.repository;

import com.unicap.tcc.usability.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginAndRemovedDateIsNull(String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndRemovedDateIsNull(String email);

    Optional<User> findByUid(UUID uuid);

    Optional<User> findByUidAndRemovedDateIsNull(UUID uuid);

    User findByEmailIgnoreCase(String email);

    List<User> findAllByIsReviewerTrueAndRemovedDateIsNullAndUidNotIn(List<UUID> uid);
}
