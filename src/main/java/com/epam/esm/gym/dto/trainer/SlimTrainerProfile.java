package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.domain.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a slim version of a trainee profile with essential details.
 * This class contains basic information about a trainee, including personal details and
 * contact information. It is intended for scenarios where a full profile is not needed.
 * The class uses Lombok annotations to automatically generate getters,
 * a toString() method, a builder, a no-args constructor, and an all-args constructor.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SlimTrainerProfile {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingType specialization;
    private boolean active;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        SlimTrainerProfile that = (SlimTrainerProfile) obj;

        return new EqualsBuilder().append(active, that.active)
                .append(username, that.username)
                .append(firstName, that.firstName)
                .append(lastName, that.lastName)
                .append(specialization, that.specialization)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username).append(firstName)
                .append(lastName).append(specialization)
                .append(active).toHashCode();
    }
}
