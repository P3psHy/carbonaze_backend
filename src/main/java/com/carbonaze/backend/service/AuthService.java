package com.carbonaze.backend.service;

import com.carbonaze.backend.dto.AuthResponse;
import com.carbonaze.backend.dto.LoginRequest;
import com.carbonaze.backend.dto.RegisterRequest;
import com.carbonaze.backend.entity.AppUser;
import com.carbonaze.backend.entity.Society;
import com.carbonaze.backend.repository.AppUserRepository;
import com.carbonaze.backend.repository.SocietyRepository;
import com.carbonaze.backend.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final SocietyRepository societyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AppUserRepository appUserRepository,
                       SocietyRepository societyRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.societyRepository = societyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedMail = normalizeMail(request.getMail());

        if (appUserRepository.existsByMailIgnoreCase(normalizedMail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un compte existe deja pour cet email.");
        }

        Society society = new Society();
        society.setName(request.getSocietyName().trim());
        Society savedSociety = societyRepository.save(society);

        AppUser appUser = new AppUser();
        appUser.setMail(normalizedMail);
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setSociety(savedSociety);

        AppUser savedUser = appUserRepository.save(appUser);

        return buildAuthResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String normalizedMail = normalizeMail(request.getMail());
        AppUser appUser = appUserRepository.findByMailIgnoreCase(normalizedMail)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides."));

        if (!passwordEncoder.matches(request.getPassword(), appUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides.");
        }

        return buildAuthResponse(appUser);
    }

    private AuthResponse buildAuthResponse(AppUser appUser) {
        AuthResponse response = new AuthResponse();
        response.setToken(jwtService.generateToken(appUser));
        response.setUserId(appUser.getId());
        response.setMail(appUser.getMail());
        response.setSocietyId(appUser.getSociety().getId());
        response.setSocietyName(appUser.getSociety().getName());
        return response;
    }

    private String normalizeMail(String mail) {
        return mail.trim().toLowerCase(Locale.ROOT);
    }
}
