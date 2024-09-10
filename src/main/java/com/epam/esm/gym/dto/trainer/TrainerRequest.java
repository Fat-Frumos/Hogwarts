package com.epam.esm.gym.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents the response containing trainer details.
 *
 * <p>This class is used to encapsulate the data returned when retrieving trainer details.</p>
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRequest {
    @NotBlank(message = "Required request parameter 'firstName' is not present")
    @Size(min = 1, max = 50, message = "Firstname must be between 1 and 50 characters")
    private String firstName;
    @NotBlank(message = "Required request parameter 'lastnameName' is not present")
    @Size(min = 1, max = 50, message = "LastName must be between 1 and 50 characters")
    @NotNull(message = "Required request parameter 'specialization' is not present")
    private String specialization;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrainerRequest that = (TrainerRequest) obj;
        return Objects.equals(firstName, that.firstName)
                && Objects.equals(specialization, that.specialization)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(dateOfBirth, that.dateOfBirth)
                && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, specialization, lastName, dateOfBirth, address);
    }
}
