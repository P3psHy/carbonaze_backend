package com.carbonaze.backend.repository;

import com.carbonaze.backend.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Long> {

    List<Site> findAllByOrderByCreatedAtDesc();

    Optional<Site> findFirstBySocietyIdAndNameIgnoreCaseAndCityIgnoreCaseAndNumberEmployeeAndParkingPlacesAndNumberPc(
        Long societyId,
        String name,
        String city,
        Integer numberEmployee,
        Integer parkingPlaces,
        Integer numberPc
    );
}
