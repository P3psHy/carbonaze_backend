package com.carbonaze.backend.controller;

import com.carbonaze.backend.dto.BilanResponse;
import com.carbonaze.backend.service.BilanService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/bilans")
public class BilanManagementController {

    private final BilanService bilanService;

    public BilanManagementController(BilanService bilanService) {
        this.bilanService = bilanService;
    }

    @GetMapping
    public List<BilanResponse> getAllBilans() {
        return bilanService.getAllBilans();
    }

    @GetMapping("/{bilanId}")
    public BilanResponse getBilanById(@PathVariable Long bilanId) {
        return bilanService.getBilanById(bilanId);
    }

    @DeleteMapping("/{bilanId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBilan(@PathVariable Long bilanId) {
        bilanService.deleteBilan(bilanId);
    }
}
