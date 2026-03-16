package com.carbonaze.backend.dto;

public class MaterialResponse {

    private Long id;
    private String name;
    private Double energeticValue;
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
