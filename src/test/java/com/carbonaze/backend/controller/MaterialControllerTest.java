package com.carbonaze.backend.controller;

import com.carbonaze.backend.dto.MaterialResponse;
import com.carbonaze.backend.exception.RestExceptionHandler;
import com.carbonaze.backend.service.MaterialService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MaterialController.class)
@Import(RestExceptionHandler.class)
class MaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MaterialService materialService;

    @Test
    void getMaterialsShouldReturnServicePayload() throws Exception {
        MaterialResponse material = new MaterialResponse();
        material.setId(1L);
        material.setName("Acier");
        material.setEnergeticValue(2.5);
        material.setQuantity(10.0);

        when(materialService.getMaterials()).thenReturn(Collections.singletonList(material));

        mockMvc.perform(get("/api/materials"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Acier"))
            .andExpect(jsonPath("$[0].quantity").value(10.0));
    }

    @Test
    void saveMaterialsShouldReturnCreatedResponse() throws Exception {
        MaterialResponse material = new MaterialResponse();
        material.setId(2L);
        material.setName("Bois");
        material.setEnergeticValue(1.2);
        material.setQuantity(4.0);

        when(materialService.saveMaterials(anyList())).thenReturn(Arrays.asList(material));

        mockMvc.perform(post("/api/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"name\":\"Bois\",\"energeticValue\":1.2,\"quantity\":4}]"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$[0].id").value(2))
            .andExpect(jsonPath("$[0].name").value("Bois"));
    }

    @Test
    void saveMaterialsShouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        mockMvc.perform(post("/api/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"name\":\"\",\"energeticValue\":-1,\"quantity\":4}]"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("name")))
            .andExpect(jsonPath("$.message", containsString("energeticValue")))
            .andExpect(jsonPath("$.path").value("/api/materials"));
    }
}
