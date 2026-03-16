package com.carbonaze.backend.controller;

import com.carbonaze.backend.dto.CreateSocietyRequest;
import com.carbonaze.backend.dto.SocietyResponse;
import com.carbonaze.backend.service.SocietyService;
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
@RequestMapping("/api/societies")
public class SocietyController {

    private final SocietyService societyService;

    public SocietyController(SocietyService societyService) {
        this.societyService = societyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SocietyResponse createSociety(@Valid @RequestBody CreateSocietyRequest request) {
        return societyService.createSociety(request);
    }
}
