package com.carbonaze.backend.repository;

import com.carbonaze.backend.entity.Bilan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BilanRepository extends JpaRepository<Bilan, Long> {

    List<Bilan> findBySiteIdOrderByCalculationDateDescIdDesc(Long siteId);

    List<Bilan> findAllByOrderByCalculationDateDescIdDesc();
}
