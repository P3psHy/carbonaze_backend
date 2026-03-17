package com.carbonaze.backend.service;

import com.carbonaze.backend.dto.CreateSocietyRequest;
import com.carbonaze.backend.dto.SocietyResponse;
import com.carbonaze.backend.entity.Society;
import com.carbonaze.backend.repository.SocietyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocietyServiceTest {

    @Mock
    private SocietyRepository societyRepository;

    @InjectMocks
    private SocietyService societyService;

    @Test
    void createSocietyShouldPersistMappedSociety() {
        CreateSocietyRequest request = new CreateSocietyRequest();
        request.setName("Carbonaze");

        when(societyRepository.save(any(Society.class))).thenAnswer(invocation -> {
            Society society = invocation.getArgument(0);
            society.setId(12L);
            return society;
        });

        SocietyResponse response = societyService.createSociety(request);

        assertThat(response.getId()).isEqualTo(12L);
        assertThat(response.getName()).isEqualTo("Carbonaze");
    }
}
