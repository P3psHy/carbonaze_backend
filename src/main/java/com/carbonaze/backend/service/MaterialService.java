package com.carbonaze.backend.service;

import com.carbonaze.backend.dto.MaterialResponse;
import com.carbonaze.backend.dto.SaveMaterialRequest;
import com.carbonaze.backend.entity.Material;
import com.carbonaze.backend.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> getMaterials() {
        return materialRepository.findAllByOrderByIdAsc()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public List<MaterialResponse> saveMaterials(List<SaveMaterialRequest> requests) {
        List<Material> materials = requests.stream()
            .map(this::toMaterial)
            .collect(Collectors.toList());

        return materialRepository.saveAll(materials)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    private Material toMaterial(SaveMaterialRequest request) {
        Material material = request.getId() == null
            ? new Material()
            : materialRepository.findById(request.getId()).orElseGet(Material::new);

        material.setName(request.getName());
        material.setEnergeticValue(request.getEnergeticValue());
        material.setQuantity(request.getQuantity());
        return material;
    }

    private MaterialResponse toResponse(Material material) {
        MaterialResponse response = new MaterialResponse();
        response.setId(material.getId());
        response.setName(material.getName());
        response.setEnergeticValue(material.getEnergeticValue());
        response.setQuantity(material.getQuantity());
        return response;
    }
}
