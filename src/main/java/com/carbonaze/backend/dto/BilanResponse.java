package com.carbonaze.backend.dto;

import java.time.LocalDate;

public class BilanResponse {

    private Long id;
    private Double electricityKwhYear;
    private Double gasKwhYear;
    private Double totalCo2;
    private LocalDate calculationDate;
    private Long siteId;
    private SiteSummary site;

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

    public SiteSummary getSite() {
        return site;
    }

    public void setSite(SiteSummary site) {
        this.site = site;
    }

    public static class SiteSummary {

        private Long id;
        private String name;
        private String city;
        private Integer numberEmployee;
        private Integer parkingPlaces;
        private Integer numberPc;
        private Long societyId;

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

        public Long getSocietyId() {
            return societyId;
        }

        public void setSocietyId(Long societyId) {
            this.societyId = societyId;
        }
    }
}
