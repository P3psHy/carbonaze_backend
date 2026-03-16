package com.carbonaze.backend.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateSiteRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String city;

    @NotNull
    @Min(0)
    private Integer numberEmployee;

    @NotNull
    @Min(0)
    private Integer parkingPlaces;

    @NotNull
    @Min(0)
    private Integer numberPc;

    @NotNull
    private Long societyId;

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
