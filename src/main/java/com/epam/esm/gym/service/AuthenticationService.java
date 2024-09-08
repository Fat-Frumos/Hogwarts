package com.epam.esm.gym.service;

import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.AuthenticationRequest;
import com.epam.esm.gym.dto.auth.AuthenticationResponse;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import com.epam.esm.gym.dto.auth.UserPrincipal;
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
    ResponseEntity<BaseResponse> signup(RegisterRequest request);

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
    ResponseEntity<BaseResponse> login(AuthenticationRequest request);

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
    ResponseEntity<BaseResponse> refresh(String authorizationHeader, HttpServletResponse response);

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
     * Finds and returns a {@link com.epam.esm.gym.dto.auth.UserPrincipal} object based on the provided username.
     * Retrieves user details from the repository using the given username.
     * Throws {@link org.springframework.security.core.userdetails.UsernameNotFoundException}
     * if no user is found with the specified username.
     * Returns a {@link com.epam.esm.gym.dto.auth.UserPrincipal} representing the user with the provided username.
     * This method is crucial for user authentication and authorization processes.
     *
     * @param username The username of the user to find.
     * @return A {@link com.epam.esm.gym.dto.auth.UserPrincipal} object representing the user.
     */
    UserPrincipal findUser(String username);

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
     * Generates an {@link AuthenticationResponse} for a given {@link com.epam.esm.gym.dto.auth.UserPrincipal} object.
     * Creates an authentication response containing tokens and user details.
     * Uses the {@link com.epam.esm.gym.dto.auth.UserPrincipal} instance to populate the response data.
     * Returns the generated {@link AuthenticationResponse} for the user.
     * This method supports generating response data for authenticated users.
     *
     * @param user The {@link com.epam.esm.gym.dto.auth.UserPrincipal} instance for which to generate the response.
     * @return An {@link AuthenticationResponse} containing tokens and user details.
     */
    AuthenticationResponse getAuthenticationResponse(UserPrincipal user);
}
