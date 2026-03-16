package com.carbonaze.backend.controller;

import com.carbonaze.backend.dto.MaterialResponse;
import com.carbonaze.backend.dto.SaveMaterialRequest;
import com.carbonaze.backend.service.MaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public List<MaterialResponse> getMaterials() {
        return materialService.getMaterials();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<MaterialResponse> saveMaterials(@Valid @RequestBody List<SaveMaterialRequest> requests) {
        return materialService.saveMaterials(requests);
    }
}
