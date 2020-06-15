package com.unicap.react.api.repository;

import com.unicap.react.api.models.Whisky;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface WhiskyRepository extends JpaRepository<Whisky, Long> {


    Whisky findByUuid(String uuid);

    @Query(value = "SELECT w FROM Whisky w WHERE w.endDate IS NULL")
    List<Whisky> findAll();

    @Transactional
    @Modifying
    @Query(value = "update Whisky tw set tw.endDate = :endDate where tw.uuid = :whiskyUuid")
    void logicDelete(@Param("whiskyUuid") String whiskyUuid, @Param("endDate") LocalDateTime endDate);
}
