package com.unicap.tcc.usability.api.repository;

import com.unicap.tcc.usability.api.models.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {

    List<Characteristic> findAllByCategory(String category);
}
