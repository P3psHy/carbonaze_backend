package com.carbonaze.backend.service;

import com.carbonaze.backend.dto.BilanResponse;
import com.carbonaze.backend.dto.CreateBilanRequest;
import com.carbonaze.backend.entity.Bilan;
import com.carbonaze.backend.entity.Site;
import com.carbonaze.backend.exception.ResourceNotFoundException;
import com.carbonaze.backend.repository.BilanRepository;
import com.carbonaze.backend.repository.SiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BilanService {

    private final BilanRepository bilanRepository;
    private final SiteRepository siteRepository;

    public BilanService(BilanRepository bilanRepository, SiteRepository siteRepository) {
        this.bilanRepository = bilanRepository;
        this.siteRepository = siteRepository;
    }

    @Transactional
    public BilanResponse createBilan(Long siteId, CreateBilanRequest request) {
        Site site = siteRepository.findById(siteId)
            .orElseThrow(() -> new ResourceNotFoundException("Site introuvable avec l'id " + siteId));

        Bilan bilan = new Bilan();
        bilan.setSite(site);
        bilan.setElectricityKwhYear(request.getElectricityKwhYear());
        bilan.setGasKwhYear(request.getGasKwhYear());
        bilan.setTotalCo2(request.getTotalCo2());
        bilan.setCalculationDate(request.getCalculationDate() != null ? request.getCalculationDate() : LocalDate.now());

        return toResponse(bilanRepository.save(bilan));
    }

    @Transactional(readOnly = true)
    public List<BilanResponse> getBilansBySite(Long siteId) {
        if (!siteRepository.existsById(siteId)) {
            throw new ResourceNotFoundException("Site introuvable avec l'id " + siteId);
        }

        return bilanRepository.findBySiteIdOrderByCalculationDateDescIdDesc(siteId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BilanResponse> getAllBilans() {
        return bilanRepository.findAllByOrderByCalculationDateDescIdDesc()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BilanResponse getBilanById(Long bilanId) {
        Bilan bilan = bilanRepository.findById(bilanId)
            .orElseThrow(() -> new ResourceNotFoundException("Bilan introuvable avec l'id " + bilanId));
        return toResponse(bilan);
    }

    @Transactional
    public void deleteBilan(Long bilanId) {
        Bilan bilan = bilanRepository.findById(bilanId)
            .orElseThrow(() -> new ResourceNotFoundException("Bilan introuvable avec l'id " + bilanId));
        bilanRepository.delete(bilan);
    }

    private BilanResponse toResponse(Bilan bilan) {
        BilanResponse response = new BilanResponse();
        response.setId(bilan.getId());
        response.setElectricityKwhYear(bilan.getElectricityKwhYear());
        response.setGasKwhYear(bilan.getGasKwhYear());
        response.setTotalCo2(bilan.getTotalCo2());
        response.setCalculationDate(bilan.getCalculationDate());
        Site site = bilan.getSite();
        response.setSiteId(site.getId());
        response.setSite(toSiteSummary(site));
        return response;
    }

    private BilanResponse.SiteSummary toSiteSummary(Site site) {
        BilanResponse.SiteSummary siteSummary = new BilanResponse.SiteSummary();
        siteSummary.setId(site.getId());
        siteSummary.setName(site.getName());
        siteSummary.setCity(site.getCity());
        siteSummary.setNumberEmployee(site.getNumberEmployee());
        siteSummary.setParkingPlaces(site.getParkingPlaces());
        siteSummary.setNumberPc(site.getNumberPc());

        if (site.getSociety() != null) {
            siteSummary.setSocietyId(site.getSociety().getId());
        }

        return siteSummary;
    }
}
