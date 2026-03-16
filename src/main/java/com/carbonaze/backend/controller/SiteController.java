package com.carbonaze.backend.controller;

import com.carbonaze.backend.dto.CreateSiteRequest;
import com.carbonaze.backend.dto.SiteResponse;
import com.carbonaze.backend.service.SiteService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SiteResponse createSite(@Valid @RequestBody CreateSiteRequest request) {
        return siteService.createSite(request);
    }
}
