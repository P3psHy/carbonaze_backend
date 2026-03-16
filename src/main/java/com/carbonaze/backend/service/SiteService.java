package com.carbonaze.backend.service;

import com.carbonaze.backend.dto.CreateSiteRequest;
import com.carbonaze.backend.dto.SiteResponse;
import com.carbonaze.backend.entity.Site;
import com.carbonaze.backend.entity.Society;
import com.carbonaze.backend.exception.ResourceNotFoundException;
import com.carbonaze.backend.repository.SiteRepository;
import com.carbonaze.backend.repository.SocietyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SiteService {

    private final SiteRepository siteRepository;
    private final SocietyRepository societyRepository;

    public SiteService(SiteRepository siteRepository, SocietyRepository societyRepository) {
        this.siteRepository = siteRepository;
        this.societyRepository = societyRepository;
    }

    @Transactional
    public SiteResponse createSite(CreateSiteRequest request) {
        Society society = societyRepository.findById(request.getSocietyId())
            .orElseThrow(() -> new ResourceNotFoundException("Societe introuvable avec l'id " + request.getSocietyId()));

        Site site = new Site();
        site.setName(request.getName());
        site.setCity(request.getCity());
        site.setNumberEmployee(request.getNumberEmployee());
        site.setParkingPlaces(request.getParkingPlaces());
        site.setNumberPc(request.getNumberPc());
        site.setCreatedAt(LocalDateTime.now());
        site.setSociety(society);

        return toResponse(siteRepository.save(site));
    }

    private SiteResponse toResponse(Site site) {
        SiteResponse response = new SiteResponse();
        response.setId(site.getId());
        response.setName(site.getName());
        response.setCity(site.getCity());
        response.setNumberEmployee(site.getNumberEmployee());
        response.setParkingPlaces(site.getParkingPlaces());
        response.setNumberPc(site.getNumberPc());
        response.setCreatedAt(site.getCreatedAt());
        response.setSocietyId(site.getSociety().getId());
        return response;
    }
}
