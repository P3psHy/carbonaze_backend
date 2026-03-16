package com.carbonaze.backend.repository;

import com.carbonaze.backend.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {
}
