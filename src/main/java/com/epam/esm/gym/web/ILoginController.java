package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The Login API handles user authentication and password management operations.
 * It provides endpoints for user login and password updates.
 * The login operation verifies user credentials and provides an authentication response.
 * The changeLogin operation updates a user's password based on the provided profile details.
 * Both operations handle different HTTP response codes based on the success or failure of the request.
 * This includes specific responses for invalid data, authentication errors, and user not found scenarios.
 * Use this interface to integrate user login and password management into your application.
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@Tag(name = "Login API", description = "Handles user login and password management")
public interface ILoginController {

    /**
     * Authenticates a user with the provided username and password.
     * The operation checks the credentials and returns a response based on the authentication result.
     *
     * @param username The username of the user. Must be between 2 and 50 characters.
     * @param password The password of the user. Must be between 6 and 50 characters.
     * @return A {@link ResponseEntity} with status 200 if login is successful and tokens are provided,
     * or status 400 for bad request and 401 for unauthorized access if credentials are invalid.
     * Returns HTTP 400 if the input data is invalid.
     * Includes a message indicating the result of the authentication attempt.
     * Handles cases where the credentials are incorrect or missing.
     */
    @Operation(
            summary = "User Login",
            description = "Authenticates a user with the provided username and password.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    ResponseEntity<MessageResponse> login(
            @RequestParam("username") @NotNull @Valid @Size(min = 2, max = 50) String username,
            @RequestParam("password") @NotNull @Valid @Size(min = 6, max = 50) String password);

    /**
     * Allows the user to change their password using the provided details.
     * This operation requires the current and new password details to successfully update the password.
     *
     * @param request A ProfileRequest object containing the current and new passwords.
     * @return A {@link ResponseEntity} with status 200 if logout is successful,
     * or status 400 for bad request and 401 for unauthorized access if the session is invalid.
     * Returns HTTP 400 for invalid request data or if the new password matches the old password.
     * Returns HTTP 403 if the current password is incorrect or HTTP 404 if the user is not found.
     * Provides detailed messages for each response scenario.
     */
    @Operation(
            summary = "Change User Password",
            description = "Allows the user to change their password using the provided details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password changed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - incorrect current password"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    ResponseEntity<MessageResponse> changeLogin(
            @RequestBody @Valid ProfileRequest request);
}
