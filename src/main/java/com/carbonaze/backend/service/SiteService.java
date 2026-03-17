package com.carbonaze.backend.service;

import com.carbonaze.backend.dto.CreateSiteRequest;
import com.carbonaze.backend.dto.SiteComparisonResponse;
import com.carbonaze.backend.dto.SiteResponse;
import com.carbonaze.backend.entity.Bilan;
import com.carbonaze.backend.entity.Site;
import com.carbonaze.backend.entity.Society;
import com.carbonaze.backend.exception.ResourceNotFoundException;
import com.carbonaze.backend.repository.BilanRepository;
import com.carbonaze.backend.repository.SiteRepository;
import com.carbonaze.backend.repository.SocietyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SiteService {

    private final SiteRepository siteRepository;
    private final BilanRepository bilanRepository;
    private final SocietyRepository societyRepository;

    public SiteService(
        SiteRepository siteRepository,
        BilanRepository bilanRepository,
        SocietyRepository societyRepository
    ) {
        this.siteRepository = siteRepository;
        this.bilanRepository = bilanRepository;
        this.societyRepository = societyRepository;
    }

    @Transactional
    public SiteResponse createSite(CreateSiteRequest request) {
        Society society = societyRepository.findById(request.getSocietyId())
            .orElseThrow(() -> new ResourceNotFoundException("Societe introuvable avec l'id " + request.getSocietyId()));

        String normalizedName = normalizeText(request.getName());
        String normalizedCity = normalizeText(request.getCity());

        Optional<Site> existingSite = siteRepository
            .findFirstBySocietyIdAndNameIgnoreCaseAndCityIgnoreCaseAndNumberEmployeeAndParkingPlacesAndNumberPc(
                society.getId(),
                normalizedName,
                normalizedCity,
                request.getNumberEmployee(),
                request.getParkingPlaces(),
                request.getNumberPc()
            );

        if (existingSite != null && existingSite.isPresent()) {
            return toResponse(existingSite.get());
        }

        Site site = new Site();
        site.setName(normalizedName);
        site.setCity(normalizedCity);
        site.setNumberEmployee(request.getNumberEmployee());
        site.setParkingPlaces(request.getParkingPlaces());
        site.setNumberPc(request.getNumberPc());
        site.setCreatedAt(LocalDateTime.now());
        site.setSociety(society);

        return toResponse(siteRepository.save(site));
    }

    @Transactional(readOnly = true)
    public List<SiteComparisonResponse> getSitesForComparison() {
        return siteRepository.findAllByOrderByCreatedAtDesc()
            .stream()
            .map((site) -> {
                Optional<Bilan> latestBilan = bilanRepository.findFirstBySiteIdOrderByCalculationDateDescIdDesc(site.getId());
                return toComparisonResponse(site, latestBilan.orElse(null));
            })
            .collect(Collectors.toList());
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

    private SiteComparisonResponse toComparisonResponse(Site site, Bilan latestBilan) {
        SiteComparisonResponse response = new SiteComparisonResponse();
        response.setId(site.getId());
        response.setName(site.getName());
        response.setCity(site.getCity());
        response.setNumberEmployee(site.getNumberEmployee());
        response.setParkingPlaces(site.getParkingPlaces());
        response.setNumberPc(site.getNumberPc());
        response.setCreatedAt(site.getCreatedAt());
        response.setSocietyId(site.getSociety().getId());

        if (latestBilan != null) {
            response.setLatestBilanId(latestBilan.getId());
            response.setLatestCalculationDate(latestBilan.getCalculationDate());
            response.setLatestTotalCo2(latestBilan.getTotalCo2());
            response.setLatestElectricityKwhYear(latestBilan.getElectricityKwhYear());
            response.setLatestGasKwhYear(latestBilan.getGasKwhYear());
        }

        return response;
    }

    private String normalizeText(String value) {
        return value == null ? "" : value.trim();
    }
}
