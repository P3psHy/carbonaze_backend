package com.carbonaze.backend.dto;

import javax.validation.constraints.NotBlank;

public class CreateSocietyRequest {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
