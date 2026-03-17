package com.carbonaze.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SiteComparisonResponse {

    private Long id;
    private String name;
    private String city;
    private Integer numberEmployee;
    private Integer parkingPlaces;
    private Integer numberPc;
    private LocalDateTime createdAt;
    private Long societyId;
    private Long latestBilanId;
    private LocalDate latestCalculationDate;
    private Double latestTotalCo2;
    private Double latestElectricityKwhYear;
    private Double latestGasKwhYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getNumberEmployee() {
        return numberEmployee;
    }

    public void setNumberEmployee(Integer numberEmployee) {
        this.numberEmployee = numberEmployee;
    }

    public Integer getParkingPlaces() {
        return parkingPlaces;
    }

    public void setParkingPlaces(Integer parkingPlaces) {
        this.parkingPlaces = parkingPlaces;
    }

    public Integer getNumberPc() {
        return numberPc;
    }

    public void setNumberPc(Integer numberPc) {
        this.numberPc = numberPc;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public Long getLatestBilanId() {
        return latestBilanId;
    }

    public void setLatestBilanId(Long latestBilanId) {
        this.latestBilanId = latestBilanId;
    }

    public LocalDate getLatestCalculationDate() {
        return latestCalculationDate;
    }

    public void setLatestCalculationDate(LocalDate latestCalculationDate) {
        this.latestCalculationDate = latestCalculationDate;
    }

    public Double getLatestTotalCo2() {
        return latestTotalCo2;
    }

    public void setLatestTotalCo2(Double latestTotalCo2) {
        this.latestTotalCo2 = latestTotalCo2;
    }

    public Double getLatestElectricityKwhYear() {
        return latestElectricityKwhYear;
    }

    public void setLatestElectricityKwhYear(Double latestElectricityKwhYear) {
        this.latestElectricityKwhYear = latestElectricityKwhYear;
    }

    public Double getLatestGasKwhYear() {
        return latestGasKwhYear;
    }

    public void setLatestGasKwhYear(Double latestGasKwhYear) {
        this.latestGasKwhYear = latestGasKwhYear;
    }
}
