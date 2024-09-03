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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * This interface defines the endpoints for user authentication and registration within the application.
 * It includes operations for user registration, authentication, token management, and session handling.
 * Each method is designed to interact with the authentication system to manage user access and credentials securely.
 * The interface outlines the expected responses and status codes for successful and unsuccessful operations.
 * It is a crucial part of the API for ensuring secure and effective user management.
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@Tag(name = "Authentication API", description = "Endpoints for user authentication and registration")
public interface IAuthenticationController {

    /**
     * Registers a new user with the provided details such as username, password, and other necessary information.
     * On successful registration, the system will respond with a status of 200 and an authentication response.
     * If the registration data is invalid, the system will return a status of 400 indicating a bad request.
     * The request must include all required fields in the body for proper processing.
     * This endpoint is crucial for onboarding new users to the system.
     *
     * @param request The registration request containing user details such as username, password.
     * @return {@link ResponseEntity} Returns a response entity with status 200
     * if registration is successful, or status 400 for bad request due to invalid data.
     */
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

    /**
     * Authenticates a user by verifying the provided credentials and returns JWT tokens upon successful authentication.
     * A successful authentication will result in a 200 status code along with the authentication response.
     * In cases of invalid credentials, a 400 status code will be returned, or a 401 status if authentication fails.
     * The request body must contain valid credentials for successful processing.
     * This endpoint is essential for users to gain access to protected resources.
     *
     * @param request The authentication request containing user credentials (username and password).
     * @return {@link ResponseEntity} Returns a response entity with status 200 if authentication
     * is successful, or status 400 for bad request and 401 for unauthorized access if credentials are invalid.
     */
    @Operation(
            summary = "Authenticate User",
            description = "Authenticate a user and obtain JWT tokens.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to invalid credentials"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access due to invalid credentials")
            }
    )
    ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request);

    /**
     * Refreshes JWT tokens using the authorization header provided in the request.
     * On a successful refresh, the system will respond with a 200 status code and updated tokens.
     * If the provided token is invalid or expired, a 400 or 401 status code will be returned.
     * This endpoint requires the authorization header to be included for token refresh.
     * It helps maintain user sessions without requiring re-authentication.
     *
     * @param authorizationHeader The authorization header containing the current JWT token.
     * @param response            The HTTP servlet response for sending the refreshed tokens back to the client.
     * @return {@link ResponseEntity} Returns a response entity with status 200 if tokens are refreshed
     * successfully, or status 400 for bad request due to invalid token
     * and 401 for unauthorized access if the token is expired or invalid.
     */
    @Operation(
            summary = "Refresh Tokens",
            description = "Refresh JWT tokens using the provided authorization header.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to invalid token"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access due to invalid token")
            }
    )
    ResponseEntity<AuthenticationResponse> refreshTokens(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletResponse response);

    /**
     * Logs in a user and provides JWT tokens upon successful authentication.
     * A 200 status code is returned along with the authentication response if login is successful.
     * If the credentials are incorrect, the system will return a 400 or 401 status code based on the failure reason.
     * The request body must include valid login credentials for processing.
     * This endpoint is used for user access and session management.
     *
     * @param loginRequest The login request containing user credentials (username and password).
     * @return {@link ResponseEntity} Returns a response entity with status 200 if login is successful
     * and tokens are provided, or status 400 for bad request and 401 for unauthorized access if credentials are invalid
     */
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

    /**
     * Logs out a user by invalidating the current JWT tokens and ending the session.
     * The system responds with a 200 status code upon successful logout.
     * If the session is invalid or the request is malformed, a 400 or 401 status code may be returned.
     * This endpoint requires the request and response objects to manage session invalidation.
     * It is crucial for ensuring user sessions are securely terminated.
     *
     * @param request  The HTTP servlet request to identify the current session.
     * @param response The HTTP servlet response to confirm the logout.
     * @return {@link ResponseEntity} Returns a response entity with status 200 if logout is successful,
     * or status 400 for bad request and 401 for unauthorized access if the session is invalid.
     */
    @Operation(
            summary = "User Logout",
            description = "Log out a user by invalidating the current JWT tokens.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged out successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to invalid session"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access due to invalid session")
            }
    )
    ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response);
}
