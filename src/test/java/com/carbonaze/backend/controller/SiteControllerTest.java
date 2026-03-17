package com.carbonaze.backend.controller;

import com.carbonaze.backend.dto.SiteResponse;
import com.carbonaze.backend.exception.ResourceNotFoundException;
import com.carbonaze.backend.exception.RestExceptionHandler;
import com.carbonaze.backend.service.SiteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SiteController.class)
@Import(RestExceptionHandler.class)
class SiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SiteService siteService;

    @Test
    void createSiteShouldReturnCreatedResponse() throws Exception {
        SiteResponse response = new SiteResponse();
        response.setId(9L);
        response.setName("Site Paris");
        response.setCity("Paris");
        response.setNumberEmployee(120);
        response.setParkingPlaces(30);
        response.setNumberPc(90);
        response.setSocietyId(5L);
        response.setCreatedAt(LocalDateTime.of(2026, 3, 16, 10, 15));

        when(siteService.createSite(any())).thenReturn(response);

        mockMvc.perform(post("/api/sites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Site Paris\",\"city\":\"Paris\",\"numberEmployee\":120,\"parkingPlaces\":30,\"numberPc\":90,\"societyId\":5}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(9))
            .andExpect(jsonPath("$.societyId").value(5))
            .andExpect(jsonPath("$.createdAt").value("2026-03-16T10:15:00"));
    }

    @Test
    void createSiteShouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        mockMvc.perform(post("/api/sites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\",\"city\":\"Paris\",\"numberEmployee\":-1,\"parkingPlaces\":30,\"numberPc\":90}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("name")))
            .andExpect(jsonPath("$.message", containsString("numberEmployee")))
            .andExpect(jsonPath("$.message", containsString("societyId")));
    }

    @Test
    void createSiteShouldReturnNotFoundWhenServiceThrows() throws Exception {
        when(siteService.createSite(any())).thenThrow(new ResourceNotFoundException("Societe introuvable avec l'id 99"));

        mockMvc.perform(post("/api/sites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Site Lyon\",\"city\":\"Lyon\",\"numberEmployee\":50,\"parkingPlaces\":10,\"numberPc\":40,\"societyId\":99}"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Societe introuvable avec l'id 99"))
            .andExpect(jsonPath("$.path").value("/api/sites"));
    }
}
