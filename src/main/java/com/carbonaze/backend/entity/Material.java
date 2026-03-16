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
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "energetic_value", nullable = false)
    private Double energeticValue;

    @Column(nullable = false)
    private Double quantity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "site_material",
        joinColumns = @JoinColumn(name = "material_id"),
        inverseJoinColumns = @JoinColumn(name = "site_id")
    )
    private Set<Site> sites = new HashSet<Site>();

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

    public Set<Site> getSites() {
        return sites;
    }

    public void setSites(Set<Site> sites) {
        this.sites = sites;
    }
}
