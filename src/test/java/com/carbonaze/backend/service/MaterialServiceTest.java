package com.carbonaze.backend.service;

import com.carbonaze.backend.dto.MaterialResponse;
import com.carbonaze.backend.dto.SaveMaterialRequest;
import com.carbonaze.backend.entity.Material;
import com.carbonaze.backend.repository.MaterialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MaterialServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialService materialService;

    @Test
    void getMaterialsShouldReturnMappedResponses() {
        when(materialRepository.findAllByOrderByIdAsc()).thenReturn(Arrays.asList(
            material(1L, "Acier", 2.5, 10.0),
            material(2L, "Bois", 1.2, 4.0)
        ));

        List<MaterialResponse> responses = materialService.getMaterials();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getName()).isEqualTo("Acier");
        assertThat(responses.get(1).getEnergeticValue()).isEqualTo(1.2);
    }

    @Test
    void saveMaterialsShouldCreateAndUpdateMaterials() {
        SaveMaterialRequest createRequest = saveRequest(null, "Acier", 2.5, 10.0);
        SaveMaterialRequest updateRequest = saveRequest(7L, "Acier recycle", 2.1, 12.0);
        Material existingMaterial = material(7L, "Ancien acier", 3.0, 8.0);

        when(materialRepository.findById(7L)).thenReturn(Optional.of(existingMaterial));
        when(materialRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<Material> materials = invocation.getArgument(0);
            materials.get(0).setId(1L);
            return materials;
        });

        List<MaterialResponse> responses = materialService.saveMaterials(Arrays.asList(createRequest, updateRequest));

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        assertThat(responses.get(0).getName()).isEqualTo("Acier");
        assertThat(responses.get(1).getId()).isEqualTo(7L);
        assertThat(responses.get(1).getName()).isEqualTo("Acier recycle");
        assertThat(responses.get(1).getQuantity()).isEqualTo(12.0);
    }

    @Test
    void saveMaterialsShouldCreateNewMaterialWhenRequestedIdIsUnknown() {
        SaveMaterialRequest request = saveRequest(99L, "Cuivre", 5.0, 2.0);

        when(materialRepository.findById(99L)).thenReturn(Optional.empty());
        when(materialRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<Material> materials = invocation.getArgument(0);
            materials.get(0).setId(3L);
            return materials;
        });

        List<MaterialResponse> responses = materialService.saveMaterials(Collections.singletonList(request));

        assertThat(responses).singleElement()
            .satisfies(response -> {
                assertThat(response.getId()).isEqualTo(3L);
                assertThat(response.getName()).isEqualTo("Cuivre");
                assertThat(response.getEnergeticValue()).isEqualTo(5.0);
            });
    }

    private SaveMaterialRequest saveRequest(Long id, String name, Double energeticValue, Double quantity) {
        SaveMaterialRequest request = new SaveMaterialRequest();
        request.setId(id);
        request.setName(name);
        request.setEnergeticValue(energeticValue);
        request.setQuantity(quantity);
        return request;
    }

    private Material material(Long id, String name, Double energeticValue, Double quantity) {
        Material material = new Material();
        material.setId(id);
        material.setName(name);
        material.setEnergeticValue(energeticValue);
        material.setQuantity(quantity);
        return material;
    }
}
