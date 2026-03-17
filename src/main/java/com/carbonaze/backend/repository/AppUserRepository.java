package com.carbonaze.backend.repository;

import com.carbonaze.backend.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);
}
