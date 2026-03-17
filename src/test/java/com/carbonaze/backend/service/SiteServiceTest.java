package com.carbonaze.backend.service;

import com.carbonaze.backend.dto.CreateSiteRequest;
import com.carbonaze.backend.dto.SiteComparisonResponse;
import com.carbonaze.backend.dto.SiteResponse;
import com.carbonaze.backend.entity.Bilan;
import com.carbonaze.backend.entity.Site;
import com.carbonaze.backend.entity.Society;
import com.carbonaze.backend.exception.ResourceNotFoundException;
import com.carbonaze.backend.repository.BilanRepository;
import com.carbonaze.backend.repository.SiteRepository;
import com.carbonaze.backend.repository.SocietyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
    private BilanRepository bilanRepository;

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

    @Test
    void getSitesForComparisonShouldMapLatestBilans() {
        Society society = new Society();
        society.setId(5L);

        Site siteParis = new Site();
        siteParis.setId(8L);
        siteParis.setName("Site Paris");
        siteParis.setCity("Paris");
        siteParis.setNumberEmployee(100);
        siteParis.setParkingPlaces(30);
        siteParis.setNumberPc(70);
        siteParis.setCreatedAt(LocalDateTime.of(2026, 3, 17, 11, 15));
        siteParis.setSociety(society);

        Site siteLyon = new Site();
        siteLyon.setId(9L);
        siteLyon.setName("Site Lyon");
        siteLyon.setCity("Lyon");
        siteLyon.setNumberEmployee(80);
        siteLyon.setParkingPlaces(24);
        siteLyon.setNumberPc(54);
        siteLyon.setCreatedAt(LocalDateTime.of(2026, 3, 16, 10, 30));
        siteLyon.setSociety(society);

        Bilan latestParisBilan = new Bilan();
        latestParisBilan.setId(101L);
        latestParisBilan.setTotalCo2(17.9);
        latestParisBilan.setElectricityKwhYear(12000.0);
        latestParisBilan.setGasKwhYear(4100.0);
        latestParisBilan.setCalculationDate(LocalDate.of(2026, 3, 17));

        when(siteRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(siteParis, siteLyon));
        when(bilanRepository.findFirstBySiteIdOrderByCalculationDateDescIdDesc(8L)).thenReturn(Optional.of(latestParisBilan));
        when(bilanRepository.findFirstBySiteIdOrderByCalculationDateDescIdDesc(9L)).thenReturn(Optional.empty());

        List<SiteComparisonResponse> response = siteService.getSitesForComparison();

        assertThat(response).hasSize(2);
        assertThat(response.get(0).getId()).isEqualTo(8L);
        assertThat(response.get(0).getLatestBilanId()).isEqualTo(101L);
        assertThat(response.get(0).getLatestCalculationDate()).isEqualTo(LocalDate.of(2026, 3, 17));
        assertThat(response.get(0).getLatestTotalCo2()).isEqualTo(17.9);
        assertThat(response.get(0).getLatestElectricityKwhYear()).isEqualTo(12000.0);
        assertThat(response.get(0).getLatestGasKwhYear()).isEqualTo(4100.0);
        assertThat(response.get(1).getId()).isEqualTo(9L);
        assertThat(response.get(1).getLatestBilanId()).isNull();
        assertThat(response.get(1).getLatestTotalCo2()).isNull();
    }
}
