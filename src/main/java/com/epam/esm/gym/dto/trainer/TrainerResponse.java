package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.dto.training.TrainingTypeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

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
public class TrainerResponse extends SlimTrainerResponse {

    private List<TrainingTypeDto> specializations;

    /**
     * Constructs a new {@code TrainerResponse} with the specified username, first name, and last name.
     * Initializes an empty list of specializations.
     *
     * @param username the username of the trainer
     * @param first    the first name of the trainer
     * @param last     the last name of the trainer
     */
    public TrainerResponse(String username, String first, String last) {
        super(username, first, last);
        specializations = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        TrainerResponse that = (TrainerResponse) obj;

        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(specializations, that.specializations)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(specializations)
                .toHashCode();
    }
}
