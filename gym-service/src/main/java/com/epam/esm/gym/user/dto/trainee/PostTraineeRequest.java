package com.epam.esm.gym.user.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

/**
 * Represents a request for creating a trainee.
 */
@Builder
public record PostTraineeRequest(
        @NotBlank(message = "Required request parameter 'firstName' is not present")
        @Size(min = 1, max = 50, message = "Firstname must be between 1 and 50 characters")
        String firstName,
        @NotBlank(message = "Required request parameter 'lastName' is not present")
        @Size(min = 1, max = 50, message = "LastName must be between 1 and 50 characters")
        String lastName,
        LocalDate dateOfBirth,
        String address
) {
}
