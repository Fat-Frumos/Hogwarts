package com.epam.esm.gym.dto.training;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Represents a request to schedule or update a training session.
 *
 * <p>This class encapsulates the necessary details required to create or modify a training session, including
 * information about the trainee, trainer, and training specifics.</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingRequest {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private String trainingDate;
    private Integer trainingDuration;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrainingRequest that = (TrainingRequest) obj;
        return Objects.equals(traineeUsername, that.traineeUsername)
                && Objects.equals(trainerUsername, that.trainerUsername)
                && Objects.equals(trainingName, that.trainingName)
                && Objects.equals(trainingDate, that.trainingDate)
                && Objects.equals(trainingDuration, that.trainingDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traineeUsername, trainerUsername, trainingName, trainingDate, trainingDuration);
    }
}
