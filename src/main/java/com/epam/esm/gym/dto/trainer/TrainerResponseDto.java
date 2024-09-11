package com.epam.esm.gym.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Data Transfer Object (DTO) representing the response for a trainer.
 *
 * <p>This class encapsulates the details of a trainer,
 * including their username, firstname, lastname, and specialization.
 * It is used to transfer trainer information in responses from the server.</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        TrainerResponseDto that = (TrainerResponseDto) obj;

        return new EqualsBuilder().append(username, that.username)
                .append(firstName, that.firstName)
                .append(lastName, that.lastName)
                .append(specialization, that.specialization)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username)
                .append(firstName)
                .append(lastName)
                .append(specialization)
                .toHashCode();
    }
}
