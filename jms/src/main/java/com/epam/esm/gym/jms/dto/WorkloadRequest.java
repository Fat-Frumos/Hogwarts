package com.epam.esm.gym.jms.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * Represents a request to update the workload of a trainer. This includes details about the
 * trainer, the training session, and the action type (add or delete) to be performed.
 */
public record WorkloadRequest(
        @NotBlank(message = "Trainer username cannot be blank")
        String trainerUsername,
        @NotBlank(message = "Trainer first name cannot be blank")
        String trainerFirstName,
        @NotBlank(message = "Trainer last name cannot be blank")
        String trainerLastName,
        @NotNull(message = "Trainer status cannot be null")
        TrainerStatus status,
        @NotNull(message = "Training date cannot be null")
        @Future(message = "Training date must be in the future")
        LocalDate trainingDate,
        @Positive(message = "Training duration must be positive")
        int trainingDuration,
        @NotNull(message = "Action type cannot be null")
        ActionType actionType
) {
}
