package com.epam.esm.gym.user.dto.trainer;

import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

/**
 * Represents a slim version of a trainee profile with essential details.
 * This class contains basic information about a trainee, including personal details and
 * contact information. It is intended for scenarios where a full profile is not needed.
 * The class uses Lombok annotations to automatically generate getters,
 * a toString() method, a builder, a no-args constructor, and an all-args constructor.
 */
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {

    private String username;
    private String firstName;
    private String lastName;
    private TrainingTypeDto specialization;

    /**
     * Constructs a new {@code TrainerResponse} with the specified username, firstName name, and lastName name.
     * Initializes an empty list of specializations.
     *
     * @param username the username of the trainer
     * @param firstName    the firstName name of the trainer
     * @param lastName     the lastName name of the trainer
     */
    public TrainerResponse(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
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
        return Objects.equals(username, that.username)
                && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(specialization, that.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, specialization);
    }
}
