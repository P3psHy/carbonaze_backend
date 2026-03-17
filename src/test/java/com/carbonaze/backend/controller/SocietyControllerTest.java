package com.carbonaze.backend.controller;

import com.carbonaze.backend.dto.SocietyResponse;
import com.carbonaze.backend.exception.RestExceptionHandler;
import com.carbonaze.backend.service.SocietyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SocietyController.class)
@Import(RestExceptionHandler.class)
class SocietyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SocietyService societyService;

    @Test
    void createSocietyShouldReturnCreatedResponse() throws Exception {
        SocietyResponse response = new SocietyResponse();
        response.setId(6L);
        response.setName("Carbonaze");

        when(societyService.createSociety(any())).thenReturn(response);

        mockMvc.perform(post("/api/societies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Carbonaze\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(6))
            .andExpect(jsonPath("$.name").value("Carbonaze"));
    }

    @Test
    void createSocietyShouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        mockMvc.perform(post("/api/societies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("name")))
            .andExpect(jsonPath("$.path").value("/api/societies"));
    }
}
