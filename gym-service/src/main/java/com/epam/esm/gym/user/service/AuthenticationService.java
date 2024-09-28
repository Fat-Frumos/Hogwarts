package com.epam.esm.gym.user.service;

import com.epam.esm.gym.user.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.user.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.auth.RegisterRequest;
import com.epam.esm.gym.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service interface for handling authentication and user management operations.
 *
 * <p>This interface defines methods for user registration, login, authentication, and token management.
 * It includes functionalities to handle user sign-ups, logins, and token refreshes. Additionally, it provides
 * methods to manage user sessions and retrieve user details based on their username or roles. Each method is
 * annotated with {@link Transactional} to ensure that operations are executed within a transaction context.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public interface AuthenticationService {
    /**
     * Registers a new user and returns an authentication response with access and refresh tokens.
     * Takes a registration request containing user details and creates a new user account.
     * Generates authentication tokens upon successful registration.
     * Returns an {@link AuthenticationResponse} that includes the access token, refresh token, and user details.
     * This method ensures that new users are properly registered and authenticated in a single operation.
     *
     * @param request The registration request containing user details.
     * @return An {@link AuthenticationResponse} with tokens and user information.
     */
    ResponseEntity<AuthenticationResponse> signup(RegisterRequest request);

    /**
     * Authenticates a user with the provided credentials and returns an authentication response.
     * Validates the username and password provided in the authentication request.
     * If authentication is successful, generates access and refresh tokens.
     * Returns an {@link AuthenticationResponse} containing these tokens and user details.
     * This method supports the login process by providing a comprehensive authentication response.
     *
     * @param request The authentication request containing user credentials.
     * @return An {@link AuthenticationResponse} with tokens and user information.
     */
    AuthenticationResponse login(AuthenticationRequest request);

    /**
     * Handles user authentication based on the provided request and returns an authentication response.
     * Validates user credentials and performs necessary security checks.
     * Returns an {@link AuthenticationResponse} with new tokens and user details if authentication is successful.
     * This method can be used in various authentication scenarios to provide consistent responses.
     * It supports different authentication flows, ensuring secure and reliable user verification.
     *
     * @param request The authentication request containing user credentials.
     * @return An {@link AuthenticationResponse} with tokens and user information.
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    /**
     * Refreshes the authentication token based on the provided authorization header.
     * Validates the current token and generates a new access token if the existing token is valid.
     * Updates the HTTP response with the new token details.
     * Returns an {@link AuthenticationResponse} with the refreshed tokens.
     * This method supports token renewal, allowing users to maintain their authenticated sessions securely.
     *
     * @param authorizationHeader The header containing the current access token.
     * @param response            The HTTP response used to set the new token.
     * @return An {@link AuthenticationResponse} with the refreshed tokens.
     */
    AuthenticationResponse refresh(String authorizationHeader, HttpServletResponse response);

    /**
     * Logs out the current user by invalidating the authentication session and associated tokens.
     * Handles session termination and ensures that the user is properly logged out.
     * Clears any authentication tokens to prevent unauthorized access.
     * Uses the HTTP request to identify the session and the response to acknowledge the logout.
     * This method enhances security by managing user logout and token invalidation.
     *
     * @param request  The HTTP request used to retrieve session information.
     * @param response The HTTP response used to acknowledge the logout.
     */
    void logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * Retrieves a {@link User} object with roles based on the registration request and username.
     * Uses the registration request to gather user details and role information.
     * Fetches the user with the specified username, including their assigned roles.
     * Returns a {@link User} instance with the relevant role information.
     * This method is used for handling user details in various authentication and role management scenarios.
     *
     * @param request  The registration request containing user details.
     * @param username The username of the user to find.
     * @return A {@link User} object with roles and details.
     */
    User getUserWithRole(RegisterRequest request, String username);

    /**
     * Publishes a token for the given authentication request.
     * This method processes the provided {@link AuthenticationRequest}
     * to generate an access token and returns a {@link MessageResponse}
     * indicating the result of the operation. The token can be used
     * for subsequent authenticated requests.
     *
     * @param loginRequest the authentication request containing user credentials
     * @return a {@link MessageResponse} containing the result of the token publishing
     */
    MessageResponse publishToken(AuthenticationRequest loginRequest);
}
