package com.epam.esm.gym.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Data Transfer Object for representing a trainer update request.
 *
 * <p>This DTO contains fields that can be updated for a trainer,
 * excluding sensitive information such as passwords.</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerUpdateRequest {

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotNull(message = "Is Active status is required")
    private Boolean active;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrainerUpdateRequest that = (TrainerUpdateRequest) obj;
        return Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, active);
    }
}
