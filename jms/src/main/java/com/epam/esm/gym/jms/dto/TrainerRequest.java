package com.epam.esm.gym.jms.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object for representing a trainer's request.
 *
 * <p>This DTO includes the trainer's username, first and last name,
 * their status, and a list of associated training sessions.</p>
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TrainerRequest implements Serializable {
    private String username;
    private String firstName;
    private String lastName;
    private TrainerStatus trainerStatus;
    private List<TrainingResponse> trainings;

    /**
     * Creates a new TrainerRequest instance via JSON deserialization.
     *
     * @param username      the username of the trainer
     * @param firstName     the first name of the trainer
     * @param lastName      the last name of the trainer
     * @param trainerStatus the status indicating whether the trainer is active or inactive
     * @param trainings     the list of trainings associated with the trainer
     */
    @JsonCreator
    public TrainerRequest(
            @JsonProperty("username") String username,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("trainerStatus") TrainerStatus trainerStatus,
            @JsonProperty("trainings") List<TrainingResponse> trainings) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.trainerStatus = trainerStatus;
        this.trainings = trainings;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrainerRequest that = (TrainerRequest) obj;
        return Objects.equals(username, that.username)
                && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && trainerStatus == that.trainerStatus
                && Objects.equals(trainings, that.trainings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, trainerStatus, trainings);
    }
}
