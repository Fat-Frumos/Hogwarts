package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/auth")
@Tag(name = "Authentication API", description = "Endpoints for user authentication and registration")
public interface IAuthenticationController {
    @PostMapping("/signup")
    @Operation(
            summary = "User Registration",
            description = "Register a new user with the provided details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to invalid data")
            }
    )
    ResponseEntity<AuthenticationResponse> signup(
            @RequestBody RegisterRequest request);

    @PostMapping("/token/authenticate")
    @Operation(
            summary = "Authenticate User",
            description = "Authenticate a user and obtain JWT tokens.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User authenticated successfully and tokens provided"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to invalid credentials"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access due to invalid credentials")
            }
    )
    ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request);

    @PostMapping("/token/refresh")
    @Operation(
            summary = "Refresh Tokens",
            description = "Refresh JWT tokens using the provided authorization header.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to invalid token"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access due to invalid or expired token")
            }
    )
    ResponseEntity<AuthenticationResponse> refreshTokens(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletResponse response);

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Log in a user and obtain JWT tokens.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged in successfully and tokens provided"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to invalid credentials"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access due to invalid credentials")
            }
    )
    ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest loginRequest);

    @PostMapping("/logout")
    @Operation(
            summary = "User Logout",
            description = "Log out a user by invalidating the current JWT tokens.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged out successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to invalid session"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access due to invalid session")
            }
    )
    ResponseEntity<Object> logout(
            HttpServletRequest request,
            HttpServletResponse response);
}
