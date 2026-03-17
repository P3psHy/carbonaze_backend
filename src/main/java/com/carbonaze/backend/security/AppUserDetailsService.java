package com.carbonaze.backend.security;

import com.carbonaze.backend.entity.AppUser;
import com.carbonaze.backend.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByMailIgnoreCase(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable: " + username));

        return new User(appUser.getMail(), appUser.getPassword(), Collections.emptyList());
    }
}
