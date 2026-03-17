package com.carbonaze.backend.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class EntityAccessorsTest {

    @Test
    void appUserShouldExposeAllProperties() {
        Society society = new Society();
        society.setId(3L);

        AppUser appUser = new AppUser();
        appUser.setId(7L);
        appUser.setMail("user@carbonaze.io");
        appUser.setPassword("secret");
        appUser.setSociety(society);

        assertThat(appUser.getId()).isEqualTo(7L);
        assertThat(appUser.getMail()).isEqualTo("user@carbonaze.io");
        assertThat(appUser.getPassword()).isEqualTo("secret");
        assertThat(appUser.getSociety()).isSameAs(society);
    }

    @Test
    void societyShouldExposeRelations() {
        Site site = new Site();
        site.setId(5L);

        AppUser user = new AppUser();
        user.setId(9L);

        Society society = new Society();
        society.setId(2L);
        society.setName("Carbonaze");
        society.setSites(Arrays.asList(site));
        society.setUsers(Arrays.asList(user));

        assertThat(society.getId()).isEqualTo(2L);
        assertThat(society.getName()).isEqualTo("Carbonaze");
        assertThat(society.getSites()).containsExactly(site);
        assertThat(society.getUsers()).containsExactly(user);
    }

    @Test
    void materialShouldExposeRelations() {
        Site site = new Site();
        site.setId(8L);

        Material material = new Material();
        material.setId(4L);
        material.setName("Acier");
        material.setEnergeticValue(2.5);
        material.setQuantity(10.0);
        material.setSites(new HashSet<Site>(Arrays.asList(site)));

        assertThat(material.getId()).isEqualTo(4L);
        assertThat(material.getName()).isEqualTo("Acier");
        assertThat(material.getEnergeticValue()).isEqualTo(2.5);
        assertThat(material.getQuantity()).isEqualTo(10.0);
        assertThat(material.getSites()).containsExactly(site);
    }

    @Test
    void siteShouldExposeRelations() {
        Society society = new Society();
        society.setId(6L);

        Material material = new Material();
        material.setId(10L);

        Bilan bilan = new Bilan();
        bilan.setId(11L);

        LocalDateTime createdAt = LocalDateTime.of(2026, 3, 16, 14, 0);

        Site site = new Site();
        site.setId(1L);
        site.setName("Site Paris");
        site.setCity("Paris");
        site.setNumberEmployee(120);
        site.setParkingPlaces(30);
        site.setCreatedAt(createdAt);
        site.setNumberPc(90);
        site.setSociety(society);
        site.setBilans(Arrays.asList(bilan));
        site.setMaterials(new HashSet<Material>(Arrays.asList(material)));

        assertThat(site.getId()).isEqualTo(1L);
        assertThat(site.getName()).isEqualTo("Site Paris");
        assertThat(site.getCity()).isEqualTo("Paris");
        assertThat(site.getNumberEmployee()).isEqualTo(120);
        assertThat(site.getParkingPlaces()).isEqualTo(30);
        assertThat(site.getCreatedAt()).isEqualTo(createdAt);
        assertThat(site.getNumberPc()).isEqualTo(90);
        assertThat(site.getSociety()).isSameAs(society);
        assertThat(site.getBilans()).containsExactly(bilan);
        assertThat(site.getMaterials()).containsExactly(material);
    }
}
