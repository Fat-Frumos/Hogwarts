package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.auth.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling login and profile-related operations.
 *
 * <p>This REST controller provides endpoints for user authentication and profile management.
 * It is responsible for processing login requests and updating user credentials. The controller
 * uses the {@link UserService} to perform these operations.</p>
 *
 * <p>The class is annotated with {@link RestController} to handle HTTP requests and responses.
 * The {@link RequestMapping} annotation sets the base URL for all endpoints in this controller
 * to "/api". The {@link Validated} annotation ensures that input validation is performed on request
 * parameters and bodies. The {@link AllArgsConstructor} annotation generates a constructor with
 * parameters for all fields, facilitating dependency injection of the {@link UserService}.</p>
 *
 * <p>The {@link GetMapping} annotation defines an endpoint for user login, where username and password
 * are validated and authenticated. The {@link PutMapping} annotation defines an endpoint to update
 * user credentials based on the provided {@link ProfileRequest}.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class LoginController implements ILoginController {

    private final UserService userService;

    /**
     * Handles user login requests.
     *
     * <p>This method processes GET requests to "/login" and authenticates the user based on the
     * provided username and password. It uses the {@link UserService} to perform the authentication
     * and returns a {@link ResponseEntity} with a {@link MessageResponse} containing the result.</p>
     *
     * @param username the username of the user to authenticate. Must be between 2 and 50 characters.
     * @param password the password of the user to authenticate. Must be between 6 and 50 characters.
     * @return a {@link ResponseEntity} containing a {@link MessageResponse} with the authentication result.
     */
    @Override
    @GetMapping("/login")
    public ResponseEntity<BaseResponse> login(
            @RequestParam("username") @NotNull @Valid @Size(min = 2, max = 50) String username,
            @RequestParam("password") @NotNull @Valid @Size(min = 6, max = 50) String password) {
        return userService.authenticate(username, password);
    }

    /**
     * Handles password change requests.
     *
     * <p>This method processes PUT requests to "/login/change" and updates the user's credentials
     * based on the provided {@link ProfileRequest}. It uses the {@link UserService} to perform
     * the update operation and returns a {@link ResponseEntity} with a {@link MessageResponse}
     * indicating the result of the operation.</p>
     *
     * @param request a {@link ProfileRequest} containing the new password and other profile details.
     * @return a {@link ResponseEntity} containing a {@link MessageResponse} with the result of
     * the update operation.
     */
    @Override
    @PutMapping("/login/change")
    public ResponseEntity<BaseResponse> changeLogin(
            @RequestBody @Valid ProfileRequest request) {
        return userService.changePassword(request);
    }
}
