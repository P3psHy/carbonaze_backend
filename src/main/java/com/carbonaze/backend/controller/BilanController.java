package com.carbonaze.backend.controller;

import com.carbonaze.backend.dto.BilanResponse;
import com.carbonaze.backend.dto.CreateBilanRequest;
import com.carbonaze.backend.service.BilanService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/sites/{siteId}/bilans")
public class BilanController {

    private final BilanService bilanService;

    public BilanController(BilanService bilanService) {
        this.bilanService = bilanService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BilanResponse createBilan(@PathVariable Long siteId, @Valid @RequestBody CreateBilanRequest request) {
        return bilanService.createBilan(siteId, request);
    }

    @GetMapping
    public List<BilanResponse> getBilans(@PathVariable Long siteId) {
        return bilanService.getBilansBySite(siteId);
    }
}
