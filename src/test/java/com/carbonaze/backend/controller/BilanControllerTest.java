package com.carbonaze.backend.controller;

import com.carbonaze.backend.dto.BilanResponse;
import com.carbonaze.backend.dto.BilanMaterialResponse;
import com.carbonaze.backend.exception.ResourceNotFoundException;
import com.carbonaze.backend.exception.RestExceptionHandler;
import com.carbonaze.backend.service.BilanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(BilanController.class)
@Import(RestExceptionHandler.class)
class BilanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BilanService bilanService;

    @Test
    void createBilanShouldReturnCreatedResponse() throws Exception {
        BilanResponse response = new BilanResponse();
        response.setId(4L);
        response.setSiteId(2L);
        response.setElectricityKwhYear(12000.0);
        response.setGasKwhYear(4300.0);
        response.setTotalCo2(18.4);
        response.setCalculationDate(LocalDate.of(2026, 3, 16));
        response.setSite(siteSummary(2L, "Site Paris", "Paris"));
        response.setMaterials(Collections.singletonList(materialSummary("Acier", 2.0)));

        when(bilanService.createBilan(org.mockito.ArgumentMatchers.eq(2L), org.mockito.ArgumentMatchers.any()))
            .thenReturn(response);

        mockMvc.perform(post("/api/sites/2/bilans")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"electricityKwhYear\":12000,\"gasKwhYear\":4300,\"totalCo2\":18.4,\"calculationDate\":\"2026-03-16\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(4))
            .andExpect(jsonPath("$.siteId").value(2))
            .andExpect(jsonPath("$.site.name").value("Site Paris"))
            .andExpect(jsonPath("$.materials[0].name").value("Acier"))
            .andExpect(jsonPath("$.calculationDate").value("2026-03-16"));
    }

    @Test
    void getBilansShouldReturnServicePayload() throws Exception {
        BilanResponse response = new BilanResponse();
        response.setId(8L);
        response.setSiteId(3L);
        response.setTotalCo2(17.9);
        response.setCalculationDate(LocalDate.of(2026, 3, 17));
        response.setSite(siteSummary(3L, "Site Lyon", "Lyon"));

        when(bilanService.getBilansBySite(3L)).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/sites/3/bilans"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(8))
            .andExpect(jsonPath("$[0].site.city").value("Lyon"))
            .andExpect(jsonPath("$[0].totalCo2").value(17.9));
    }

    @Test
    void createBilanShouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        mockMvc.perform(post("/api/sites/2/bilans")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"electricityKwhYear\":-1,\"gasKwhYear\":4300}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("electricityKwhYear")))
            .andExpect(jsonPath("$.message", containsString("totalCo2")))
            .andExpect(jsonPath("$.path").value("/api/sites/2/bilans"));
    }

    @Test
    void getBilansShouldReturnNotFoundWhenServiceThrows() throws Exception {
        when(bilanService.getBilansBySite(404L)).thenThrow(new ResourceNotFoundException("Site introuvable avec l'id 404"));

        mockMvc.perform(get("/api/sites/404/bilans"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Site introuvable avec l'id 404"))
            .andExpect(jsonPath("$.error").value("Not Found"));
    }

    private BilanResponse.SiteSummary siteSummary(Long id, String name, String city) {
        BilanResponse.SiteSummary siteSummary = new BilanResponse.SiteSummary();
        siteSummary.setId(id);
        siteSummary.setName(name);
        siteSummary.setCity(city);
        return siteSummary;
    }

    private BilanMaterialResponse materialSummary(String name, Double quantity) {
        BilanMaterialResponse materialResponse = new BilanMaterialResponse();
        materialResponse.setName(name);
        materialResponse.setQuantity(quantity);
        return materialResponse;
    }
}
