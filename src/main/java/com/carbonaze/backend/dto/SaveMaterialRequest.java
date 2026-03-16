package com.carbonaze.backend.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SaveMaterialRequest {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin("0.0")
    private Double energeticValue;

    @NotNull
    @DecimalMin("0.0")
    private Double quantity;

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

    public Double getEnergeticValue() {
        return energeticValue;
    }

    public void setEnergeticValue(Double energeticValue) {
        this.energeticValue = energeticValue;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
