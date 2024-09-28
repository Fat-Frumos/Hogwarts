package com.epam.esm.gym.user.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

/**
 * Data Transfer Object for representing a training workload response.
 *
 * <p>This DTO includes details about a training session such as the trainer's username,
 * training name, type, duration, and date.</p>
 */
@Builder
public record PutTraineeRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
        String username,
        @NotBlank(message = "Required request parameter 'firstName' is not present")
        @Size(min = 1, max = 50, message = "Firstname must be between 1 and 50 characters")
        String firstName,
        @NotBlank(message = "Required request parameter 'lastName' is not present")
        @Size(min = 1, max = 50, message = "LastName must be between 1 and 50 characters")
        String lastName,
        LocalDate dateOfBirth,
        String address,
        @NotNull(message = "Active is required")
        Boolean active
) {
}
