package com.unicap.tcc.usability.api.repository;

import com.unicap.tcc.usability.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByUid(UUID uuid);

    User findByEmailIgnoreCase(String email);
}
