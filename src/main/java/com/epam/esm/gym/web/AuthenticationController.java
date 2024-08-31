package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import com.epam.esm.gym.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(
            final @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.signup(request));
    }

    @PostMapping("/token/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            final @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<AuthenticationResponse> refreshTokens(
            final @RequestHeader("Authorization") String authorizationHeader,
            final HttpServletResponse response) {
        return ResponseEntity.ok(service.refresh(
                authorizationHeader, response));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            final @RequestBody AuthenticationRequest loginRequest) {
        return ResponseEntity.ok(service.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        service.logout(request, response);
        return ResponseEntity.ok().build();
    }
}
