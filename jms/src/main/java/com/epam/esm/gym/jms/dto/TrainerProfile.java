package com.epam.esm.gym.jms.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

/**
 * Represents the response object for a trainer's details.
 * This record encapsulates the essential information about a trainer,
 * including their username, personal details, status, and associated
 * training requests. It is used primarily in API responses to provide
 * comprehensive trainer information to clients.
 */
@Builder
public record TrainerProfile(
        String username,
        String firstName,
        String lastName,
        boolean active,
        List<TrainingResponse> trainings) {

    /**
     * Creates a new TrainerProfile instance.
     *
     * @param username  the username of the trainer
     * @param firstName the first name of the trainer
     * @param lastName  the last name of the trainer
     * @param active    indicates if the trainer is active
     * @param trainings the list of trainings associated with the trainer
     */
    @JsonCreator
    public TrainerProfile(
            @JsonProperty("username") String username,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("active") boolean active,
            @JsonProperty("trainings") List<TrainingResponse> trainings) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
        this.trainings = trainings;
    }
}
