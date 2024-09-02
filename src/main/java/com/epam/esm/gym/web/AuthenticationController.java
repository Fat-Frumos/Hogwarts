package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import com.epam.esm.gym.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController implements IAuthenticationController {

    private final AuthenticationService service;

    @Override
    public ResponseEntity<AuthenticationResponse> signup(
            final @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.signup(request));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(
            final @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> refreshTokens(
            final @RequestHeader("Authorization") String authorizationHeader,
            final HttpServletResponse response) {
        return ResponseEntity.ok(service.refresh(
                authorizationHeader, response));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> login(
            final @RequestBody AuthenticationRequest loginRequest) {
        return ResponseEntity.ok(service.login(loginRequest));
    }

    @Override
    public ResponseEntity<Object> logout(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        service.logout(request, response);
        return ResponseEntity.ok().build();
    }
}
