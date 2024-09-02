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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api")
@Tag(name = "Login API", description = "Handles user login and password management")
public interface ILoginController {
    @GetMapping("/login")
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

    @PutMapping("/login/change")
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
