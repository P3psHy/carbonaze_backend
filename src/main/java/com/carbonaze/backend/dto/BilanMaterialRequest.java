package com.carbonaze.backend.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BilanMaterialRequest {

    private Long materialId;

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin("0.0")
    private Double quantity;

    @NotNull
    @DecimalMin("0.0")
    private Double factor;

    @NotNull
    @DecimalMin("0.0")
    private Double emission;

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    public Double getEmission() {
        return emission;
    }

    public void setEmission(Double emission) {
        this.emission = emission;
    }
}
