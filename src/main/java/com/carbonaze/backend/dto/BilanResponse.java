package com.carbonaze.backend.dto;

import java.time.LocalDate;

public class BilanResponse {

    private Long id;
    private Double electricityKwhYear;
    private Double gasKwhYear;
    private Double totalCo2;
    private LocalDate calculationDate;
    private Long siteId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getElectricityKwhYear() {
        return electricityKwhYear;
    }

    public void setElectricityKwhYear(Double electricityKwhYear) {
        this.electricityKwhYear = electricityKwhYear;
    }

    public Double getGasKwhYear() {
        return gasKwhYear;
    }

    public void setGasKwhYear(Double gasKwhYear) {
        this.gasKwhYear = gasKwhYear;
    }

    public Double getTotalCo2() {
        return totalCo2;
    }

    public void setTotalCo2(Double totalCo2) {
        this.totalCo2 = totalCo2;
    }

    public LocalDate getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(LocalDate calculationDate) {
        this.calculationDate = calculationDate;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }
}
