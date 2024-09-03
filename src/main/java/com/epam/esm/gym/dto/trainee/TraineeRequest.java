package com.epam.esm.gym.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Data Transfer Object for representing a training session response.
 *
 * <p>This DTO includes details about a training session such as the trainer's username,
 * training name, type, duration, and date.</p>
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TraineeRequest {
    @NotBlank(message = "FirstName is required")
    @Size(min = 1, max = 50, message = "Firstname must be between 1 and 50 characters")
    private String firstName;
    @NotBlank(message = "LastName is required")
    @Size(min = 1, max = 50, message = "LastName must be between 1 and 50 characters")
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private Boolean active;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TraineeRequest that = (TraineeRequest) obj;
        return Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(dateOfBirth, that.dateOfBirth)
                && Objects.equals(address, that.address)
                && Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, dateOfBirth, address, active);
    }
}
