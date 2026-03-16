package com.carbonaze.backend.service;

import com.carbonaze.backend.dto.CreateSocietyRequest;
import com.carbonaze.backend.dto.SocietyResponse;
import com.carbonaze.backend.entity.Society;
import com.carbonaze.backend.repository.SocietyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocietyService {

    private final SocietyRepository societyRepository;

    public SocietyService(SocietyRepository societyRepository) {
        this.societyRepository = societyRepository;
    }

    @Transactional
    public SocietyResponse createSociety(CreateSocietyRequest request) {
        Society society = new Society();
        society.setName(request.getName());
        return toResponse(societyRepository.save(society));
    }

    private SocietyResponse toResponse(Society society) {
        SocietyResponse response = new SocietyResponse();
        response.setId(society.getId());
        response.setName(society.getName());
        return response;
    }
}
