package com.epam.esm.gym.user.dto.profile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Data Transfer Object for representing a user profile.
 *
 * <p>This DTO includes the username and password of a user.
 * It is used to expose user profile information in a secure manner.</p>
 */
@Builder
public record ProfileResponse(
        @NotNull(message = "Required request parameter 'username' is not present")
        @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
        String username,
        @NotNull(message = "Required request parameter 'password' is not present")
        @Size(min = 1, max = 50, message = "Password must be between 1 and 50 characters")
        String password) {
}
