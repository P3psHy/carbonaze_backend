package com.carbonaze.backend.service;

import com.carbonaze.backend.dto.CreateSiteRequest;
import com.carbonaze.backend.dto.SiteResponse;
import com.carbonaze.backend.entity.Site;
import com.carbonaze.backend.entity.Society;
import com.carbonaze.backend.exception.ResourceNotFoundException;
import com.carbonaze.backend.repository.SiteRepository;
import com.carbonaze.backend.repository.SocietyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteServiceTest {

    @Mock
    private SiteRepository siteRepository;

    @Mock
    private SocietyRepository societyRepository;

    @InjectMocks
    private SiteService siteService;

    @Test
    void createSiteShouldPersistMappedSite() {
        CreateSiteRequest request = new CreateSiteRequest();
        request.setName("Site Paris");
        request.setCity("Paris");
        request.setNumberEmployee(120);
        request.setParkingPlaces(30);
        request.setNumberPc(90);
        request.setSocietyId(5L);

        Society society = new Society();
        society.setId(5L);
        society.setName("Carbonaze");

        LocalDateTime beforeCall = LocalDateTime.now();

        when(societyRepository.findById(5L)).thenReturn(Optional.of(society));
        when(siteRepository.save(any(Site.class))).thenAnswer(invocation -> {
            Site site = invocation.getArgument(0);
            site.setId(8L);
            return site;
        });

        SiteResponse response = siteService.createSite(request);
        LocalDateTime afterCall = LocalDateTime.now();

        assertThat(response.getId()).isEqualTo(8L);
        assertThat(response.getName()).isEqualTo("Site Paris");
        assertThat(response.getCity()).isEqualTo("Paris");
        assertThat(response.getNumberEmployee()).isEqualTo(120);
        assertThat(response.getParkingPlaces()).isEqualTo(30);
        assertThat(response.getNumberPc()).isEqualTo(90);
        assertThat(response.getSocietyId()).isEqualTo(5L);
        assertThat(response.getCreatedAt()).isBetween(beforeCall, afterCall);
    }

    @Test
    void createSiteShouldThrowWhenSocietyDoesNotExist() {
        CreateSiteRequest request = new CreateSiteRequest();
        request.setSocietyId(55L);

        when(societyRepository.findById(55L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> siteService.createSite(request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Societe introuvable avec l'id 55");
    }
}
