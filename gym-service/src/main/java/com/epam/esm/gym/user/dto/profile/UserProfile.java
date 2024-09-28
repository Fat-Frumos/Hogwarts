package com.epam.esm.gym.user.dto.profile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

/**
 * Data Transfer Object for representing a user profile.
 *
 * <p>This DTO includes the username and password of a user.
 * It is used to expose user profile information in a secure manner.</p>
 */
@Validated
@Builder
public record UserProfile(
        @NotNull(message = "Required request parameter 'firstName' is not present")
        @Size(min = 1, max = 50, message = "Firstname must be between 1 and 50 characters")
        String firstName,
        @NotNull(message = "Required request parameter 'lastName' is not present")
        @Size(min = 1, max = 50, message = "LastName must be between 1 and 50 characters")
        String lastName
) {
}
