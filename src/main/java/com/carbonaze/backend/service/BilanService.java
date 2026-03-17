package com.carbonaze.backend.service;

import com.carbonaze.backend.dto.BilanMaterialRequest;
import com.carbonaze.backend.dto.BilanMaterialResponse;
import com.carbonaze.backend.dto.BilanResponse;
import com.carbonaze.backend.dto.CreateBilanRequest;
import com.carbonaze.backend.entity.Bilan;
import com.carbonaze.backend.entity.BilanMaterial;
import com.carbonaze.backend.entity.Material;
import com.carbonaze.backend.entity.Site;
import com.carbonaze.backend.exception.ResourceNotFoundException;
import com.carbonaze.backend.repository.BilanRepository;
import com.carbonaze.backend.repository.MaterialRepository;
import com.carbonaze.backend.repository.SiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BilanService {

    private final BilanRepository bilanRepository;
    private final SiteRepository siteRepository;
    private final MaterialRepository materialRepository;

    public BilanService(BilanRepository bilanRepository, SiteRepository siteRepository, MaterialRepository materialRepository) {
        this.bilanRepository = bilanRepository;
        this.siteRepository = siteRepository;
        this.materialRepository = materialRepository;
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
        bilan.setMaterials(toBilanMaterials(request.getMaterials(), bilan));

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
        response.setMaterials(toBilanMaterialResponses(bilan.getMaterials()));
        return response;
    }

    private List<BilanMaterial> toBilanMaterials(List<BilanMaterialRequest> requests, Bilan bilan) {
        if (requests == null || requests.isEmpty()) {
            return Collections.emptyList();
        }

        return requests.stream()
            .map(request -> toBilanMaterial(request, bilan))
            .collect(Collectors.toList());
    }

    private BilanMaterial toBilanMaterial(BilanMaterialRequest request, Bilan bilan) {
        BilanMaterial material = new BilanMaterial();
        material.setBilan(bilan);
        material.setName(request.getName().trim());
        material.setQuantity(request.getQuantity());
        material.setFactor(request.getFactor());
        material.setEmission(request.getEmission());

        if (request.getMaterialId() != null) {
            Material linkedMaterial = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Materiau introuvable avec l'id " + request.getMaterialId()));
            material.setMaterial(linkedMaterial);
        }

        return material;
    }

    private List<BilanMaterialResponse> toBilanMaterialResponses(List<BilanMaterial> materials) {
        if (materials == null || materials.isEmpty()) {
            return Collections.emptyList();
        }

        return materials.stream()
            .map(this::toBilanMaterialResponse)
            .collect(Collectors.toList());
    }

    private BilanMaterialResponse toBilanMaterialResponse(BilanMaterial material) {
        BilanMaterialResponse response = new BilanMaterialResponse();
        response.setId(material.getId());
        response.setName(material.getName());
        response.setQuantity(material.getQuantity());
        response.setFactor(material.getFactor());
        response.setEmission(material.getEmission());

        if (material.getMaterial() != null) {
            response.setMaterialId(material.getMaterial().getId());
        }

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
