package com.carbonaze.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "bilan")
public class Bilan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "electricity_kwh_year", nullable = false)
    private Double electricityKwhYear;

    @Column(name = "gas_kwh_year", nullable = false)
    private Double gasKwhYear;

    @Column(name = "total_co2", nullable = false)
    private Double totalCo2;

    @Column(name = "calculation_date", nullable = false)
    private LocalDate calculationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

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

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
