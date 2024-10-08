package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.user.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.user.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.user.dto.auth.BaseResponse;
import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.auth.RegisterRequest;
import com.epam.esm.gym.user.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles authentication-related operations for user registration, login, token management, and logout.
 * Implements the IAuthenticationController interface to provide endpoints for user and session management.
 * Uses AuthenticationUserService for processing authentication requests and managing user sessions.
 * Provides mappings for signup, login, token refresh, and logout operations.
 * Ensures secure handling of authentication requests and responses.
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController implements IAuthenticationController {

    private final AuthenticationService service;

    /**
     * {@inheritDoc}
     * Registers a new user with the provided details.
     *
     * @param request The {@link RegisterRequest} object containing user registration details.
     * @return A {@link ResponseEntity} containing the {@link AuthenticationResponse} with registration result.
     */
    @Override
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signupUser(
            final @RequestBody RegisterRequest request) {
        return service.signup(request);
    }

    /**
     * {@inheritDoc}
     * Registers a new user with the provided details.
     *
     * @param request The {@link RegisterRequest} object containing user registration details.
     * @return A {@link ResponseEntity} containing the {@link AuthenticationResponse} with registration result.
     */
    @Override
    @PostMapping("/token/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
            final @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    /**
     * {@inheritDoc}
     * Refreshes JWT tokens using the provided authorization header.
     *
     * @param authorizationHeader The authorization header containing the refresh token.
     * @param response            The {@link HttpServletResponse} used to set new tokens.
     * @return A {@link ResponseEntity} containing the {@link AuthenticationResponse} with refreshed tokens.
     */
    @Override
    @PostMapping("/token/refresh")
    public ResponseEntity<AuthenticationResponse> refreshTokens(
            final @RequestHeader("Authorization") String authorizationHeader,
            final HttpServletResponse response) {
        return ResponseEntity.ok(service.refresh(authorizationHeader, response));
    }

    /**
     * {@inheritDoc}
     * Logs in a user and provides JWT tokens.
     *
     * @param loginRequest The {@link AuthenticationRequest} object containing login credentials.
     * @return A {@link ResponseEntity} containing the {@link AuthenticationResponse} with login tokens.
     */
    @Override
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(
            final @RequestBody AuthenticationRequest loginRequest) {
        return ResponseEntity.ok(service.login(loginRequest));
    }

    /**
     * Logs in a user for broker and provides JWT tokens.
     *
     * @param loginRequest The {@link AuthenticationRequest} object containing login credentials.
     * @return A {@link ResponseEntity} containing the {@link AuthenticationResponse} with login tokens.
     */
    @PostMapping("/kafka/login")
    public ResponseEntity<MessageResponse> getAccessToken(
            final @RequestBody AuthenticationRequest loginRequest) {
        return ResponseEntity.ok(service.publishToken(loginRequest));
    }

    /**
     * {@inheritDoc}
     * Logs out a user by invalidating the current JWT tokens.
     *
     * @param request  The {@link HttpServletRequest} for retrieving session information.
     * @param response The {@link HttpServletResponse} used to clear tokens.
     * @return A {@link ResponseEntity} with an empty body indicating successful logout.
     */
    @Override
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse> logout(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        service.logout(request, response);
        return ResponseEntity.ok().build();
    }
}
