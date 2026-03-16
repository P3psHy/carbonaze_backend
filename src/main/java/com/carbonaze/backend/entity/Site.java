package com.carbonaze.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "site")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String city;

    @Column(name = "number_employee", nullable = false)
    private Integer numberEmployee;

    @Column(name = "parking_places", nullable = false)
    private Integer parkingPlaces;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "number_pc", nullable = false)
    private Integer numberPc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;

    @OneToMany(mappedBy = "site")
    private List<Bilan> bilans;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "site_material",
        joinColumns = @JoinColumn(name = "site_id"),
        inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private Set<Material> materials = new HashSet<Material>();

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getNumberPc() {
        return numberPc;
    }

    public void setNumberPc(Integer numberPc) {
        this.numberPc = numberPc;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public List<Bilan> getBilans() {
        return bilans;
    }

    public void setBilans(List<Bilan> bilans) {
        this.bilans = bilans;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }
}
