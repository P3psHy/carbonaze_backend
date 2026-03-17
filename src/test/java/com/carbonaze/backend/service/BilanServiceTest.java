package com.carbonaze.backend.service;

import com.carbonaze.backend.dto.BilanResponse;
import com.carbonaze.backend.dto.CreateBilanRequest;
import com.carbonaze.backend.entity.Bilan;
import com.carbonaze.backend.entity.Site;
import com.carbonaze.backend.entity.Society;
import com.carbonaze.backend.exception.ResourceNotFoundException;
import com.carbonaze.backend.repository.BilanRepository;
import com.carbonaze.backend.repository.SiteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BilanServiceTest {

    @Mock
    private BilanRepository bilanRepository;

    @Mock
    private SiteRepository siteRepository;

    @InjectMocks
    private BilanService bilanService;

    @Test
    void createBilanShouldMapRequestAndPersistIt() {
        Site site = site(7L);
        CreateBilanRequest request = createRequest(LocalDate.of(2026, 3, 16));

        when(siteRepository.findById(7L)).thenReturn(Optional.of(site));
        when(bilanRepository.save(any(Bilan.class))).thenAnswer(invocation -> {
            Bilan bilan = invocation.getArgument(0);
            bilan.setId(42L);
            return bilan;
        });

        BilanResponse response = bilanService.createBilan(7L, request);

        assertThat(response.getId()).isEqualTo(42L);
        assertThat(response.getSiteId()).isEqualTo(7L);
        assertThat(response.getElectricityKwhYear()).isEqualTo(12000.0);
        assertThat(response.getGasKwhYear()).isEqualTo(4300.0);
        assertThat(response.getTotalCo2()).isEqualTo(18.4);
        assertThat(response.getCalculationDate()).isEqualTo(LocalDate.of(2026, 3, 16));
        assertThat(response.getSite()).isNotNull();
        assertThat(response.getSite().getName()).isEqualTo("Site 7");
        assertThat(response.getSite().getNumberEmployee()).isEqualTo(120);
    }

    @Test
    void createBilanShouldDefaultCalculationDateToTodayWhenMissing() {
        Site site = site(3L);
        CreateBilanRequest request = createRequest(null);
        LocalDate beforeCall = LocalDate.now();

        when(siteRepository.findById(3L)).thenReturn(Optional.of(site));
        when(bilanRepository.save(any(Bilan.class))).thenAnswer(invocation -> {
            Bilan bilan = invocation.getArgument(0);
            bilan.setId(11L);
            return bilan;
        });

        BilanResponse response = bilanService.createBilan(3L, request);
        LocalDate afterCall = LocalDate.now();

        assertThat(response.getCalculationDate()).isBetween(beforeCall, afterCall);
    }

    @Test
    void createBilanShouldThrowWhenSiteDoesNotExist() {
        when(siteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bilanService.createBilan(99L, createRequest(LocalDate.now())))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Site introuvable avec l'id 99");
    }

    @Test
    void getBilansBySiteShouldReturnMappedResponses() {
        Site site = site(5L);
        when(siteRepository.existsById(5L)).thenReturn(true);
        when(bilanRepository.findBySiteIdOrderByCalculationDateDescIdDesc(5L)).thenReturn(Arrays.asList(
            bilan(12L, site, LocalDate.of(2026, 3, 17), 17.1),
            bilan(10L, site, LocalDate.of(2026, 3, 16), 18.2)
        ));

        java.util.List<BilanResponse> responses = bilanService.getBilansBySite(5L);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(12L);
        assertThat(responses.get(0).getSiteId()).isEqualTo(5L);
        assertThat(responses.get(0).getSite().getCity()).isEqualTo("Paris");
        assertThat(responses.get(0).getCalculationDate()).isEqualTo(LocalDate.of(2026, 3, 17));
        assertThat(responses.get(1).getTotalCo2()).isEqualTo(18.2);
    }

    @Test
    void getBilansBySiteShouldThrowWhenSiteDoesNotExist() {
        when(siteRepository.existsById(404L)).thenReturn(false);

        assertThatThrownBy(() -> bilanService.getBilansBySite(404L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Site introuvable avec l'id 404");

        verify(siteRepository).existsById(404L);
    }

    private CreateBilanRequest createRequest(LocalDate calculationDate) {
        CreateBilanRequest request = new CreateBilanRequest();
        request.setElectricityKwhYear(12000.0);
        request.setGasKwhYear(4300.0);
        request.setTotalCo2(18.4);
        request.setCalculationDate(calculationDate);
        return request;
    }

    private Site site(Long id) {
        Site site = new Site();
        site.setId(id);
        site.setName("Site " + id);
        site.setCity("Paris");
        site.setNumberEmployee(120);
        site.setParkingPlaces(30);
        site.setNumberPc(90);

        Society society = new Society();
        society.setId(1L);
        site.setSociety(society);
        return site;
    }

    private Bilan bilan(Long id, Site site, LocalDate calculationDate, Double totalCo2) {
        Bilan bilan = new Bilan();
        bilan.setId(id);
        bilan.setSite(site);
        bilan.setElectricityKwhYear(12000.0);
        bilan.setGasKwhYear(4300.0);
        bilan.setTotalCo2(totalCo2);
        bilan.setCalculationDate(calculationDate);
        return bilan;
    }
}
