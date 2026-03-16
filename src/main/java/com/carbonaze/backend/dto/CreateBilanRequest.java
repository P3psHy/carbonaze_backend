package com.carbonaze.backend.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public class CreateBilanRequest {

    @NotNull
    @PositiveOrZero
    private Double electricityKwhYear;

    @NotNull
    @PositiveOrZero
    private Double gasKwhYear;

    @NotNull
    @PositiveOrZero
    private Double totalCo2;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate calculationDate;

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
}
