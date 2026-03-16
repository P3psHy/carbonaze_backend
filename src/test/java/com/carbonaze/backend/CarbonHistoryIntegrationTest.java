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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Test
    void shouldRetrieveAllBilans() throws Exception {
        long societyId = createSociety("Carbonaze");
        long siteParisId = createSite(societyId, "Site Paris", "Paris");
        long siteLyonId = createSite(societyId, "Site Lyon", "Lyon");

        createBilan(siteParisId, 18.5, "2026-03-16");
        createBilan(siteLyonId, 12.3, "2026-03-18");
        createBilan(siteParisId, 17.9, "2026-03-17");

        mockMvc.perform(get("/api/bilans"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].calculationDate").value("2026-03-18"))
            .andExpect(jsonPath("$[0].siteId").value(siteLyonId))
            .andExpect(jsonPath("$[1].calculationDate").value("2026-03-17"))
            .andExpect(jsonPath("$[2].calculationDate").value("2026-03-16"));
    }

    @Test
    void shouldDeleteABilan() throws Exception {
        long societyId = createSociety("Carbonaze Delete");
        long siteId = createSite(societyId, "Site Nantes", "Nantes");
        long bilanId = createBilan(siteId, 9.4, "2026-03-19");

        mockMvc.perform(delete("/api/bilans/{bilanId}", bilanId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/bilans"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldReturn404WhenDeletingUnknownBilan() throws Exception {
        mockMvc.perform(delete("/api/bilans/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Bilan introuvable avec l'id 999"));
    }

    @Test
    void shouldSaveAndRetrieveMaterialsForSync() throws Exception {
        MvcResult creationResult = mockMvc.perform(post("/api/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{"
                    + "\"name\":\"Acier\","
                    + "\"energeticValue\":2.5,"
                    + "\"quantity\":10"
                    + "},{"
                    + "\"name\":\"Bois\","
                    + "\"energeticValue\":1.2,"
                    + "\"quantity\":4"
                    + "}]"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name").value("Acier"))
            .andExpect(jsonPath("$[1].name").value("Bois"))
            .andReturn();

        JsonNode createdMaterials = objectMapper.readTree(creationResult.getResponse().getContentAsString());
        long acierId = createdMaterials.get(0).get("id").asLong();

        mockMvc.perform(post("/api/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{"
                    + "\"id\":" + acierId + ","
                    + "\"name\":\"Acier recycle\","
                    + "\"energeticValue\":2.1,"
                    + "\"quantity\":12"
                    + "}]"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$[0].id").value(acierId))
            .andExpect(jsonPath("$[0].name").value("Acier recycle"))
            .andExpect(jsonPath("$[0].quantity").value(12.0));

        mockMvc.perform(get("/api/materials"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id").value(acierId))
            .andExpect(jsonPath("$[0].name").value("Acier recycle"))
            .andExpect(jsonPath("$[0].energeticValue").value(2.1))
            .andExpect(jsonPath("$[1].name").value("Bois"));
    }

    private long createSociety(String name) throws Exception {
        MvcResult societyResult = mockMvc.perform(post("/api/societies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"" + name + "\"}"))
            .andExpect(status().isCreated())
            .andReturn();

        JsonNode societyJson = objectMapper.readTree(societyResult.getResponse().getContentAsString());
        return societyJson.get("id").asLong();
    }

    private long createSite(long societyId, String name, String city) throws Exception {
        String payload = "{"
            + "\"name\":\"" + name + "\","
            + "\"city\":\"" + city + "\","
            + "\"numberEmployee\":120,"
            + "\"parkingPlaces\":30,"
            + "\"numberPc\":90,"
            + "\"societyId\":" + societyId
            + "}";

        MvcResult siteResult = mockMvc.perform(post("/api/sites")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andReturn();

        JsonNode siteJson = objectMapper.readTree(siteResult.getResponse().getContentAsString());
        return siteJson.get("id").asLong();
    }

    private long createBilan(long siteId, double totalCo2, String calculationDate) throws Exception {
        String payload = "{"
            + "\"electricityKwhYear\":12000,"
            + "\"gasKwhYear\":4500,"
            + "\"totalCo2\":" + totalCo2 + ","
            + "\"calculationDate\":\"" + calculationDate + "\""
            + "}";

        MvcResult bilanResult = mockMvc.perform(post("/api/sites/{siteId}/bilans", siteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andReturn();

        JsonNode bilanJson = objectMapper.readTree(bilanResult.getResponse().getContentAsString());
        return bilanJson.get("id").asLong();
    }
}
