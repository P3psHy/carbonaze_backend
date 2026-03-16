package com.carbonaze.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarbonHistoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndRetrieveCalculationHistoryForASite() throws Exception {
        MvcResult societyResult = mockMvc.perform(post("/api/societies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Carbonaze\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Carbonaze"))
            .andReturn();

        JsonNode societyJson = objectMapper.readTree(societyResult.getResponse().getContentAsString());
        long societyId = societyJson.get("id").asLong();

        String sitePayload = "{"
            + "\"name\":\"Site Paris\","
            + "\"city\":\"Paris\","
            + "\"numberEmployee\":120,"
            + "\"parkingPlaces\":30,"
            + "\"numberPc\":90,"
            + "\"societyId\":" + societyId
            + "}";

        MvcResult siteResult = mockMvc.perform(post("/api/sites")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sitePayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.city").value("Paris"))
            .andReturn();

        JsonNode siteJson = objectMapper.readTree(siteResult.getResponse().getContentAsString());
        long siteId = siteJson.get("id").asLong();

        mockMvc.perform(post("/api/sites/{siteId}/bilans", siteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{"
                    + "\"electricityKwhYear\":12000,"
                    + "\"gasKwhYear\":4500,"
                    + "\"totalCo2\":18.5,"
                    + "\"calculationDate\":\"2026-03-16\""
                    + "}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.siteId").value(siteId))
            .andExpect(jsonPath("$.totalCo2").value(18.5));

        mockMvc.perform(post("/api/sites/{siteId}/bilans", siteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{"
                    + "\"electricityKwhYear\":12450,"
                    + "\"gasKwhYear\":4100,"
                    + "\"totalCo2\":17.9,"
                    + "\"calculationDate\":\"2026-03-17\""
                    + "}"))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/sites/{siteId}/bilans", siteId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].calculationDate").value("2026-03-17"))
            .andExpect(jsonPath("$[0].totalCo2").value(17.9))
            .andExpect(jsonPath("$[1].calculationDate").value("2026-03-16"))
            .andExpect(jsonPath("$[1].totalCo2").value(18.5));
    }

    @Test
    void shouldReturn404WhenSiteDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/sites/999/bilans"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Site introuvable avec l'id 999"));
    }
}
