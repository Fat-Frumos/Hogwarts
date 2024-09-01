package com.epam.esm.gym.dto.training;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingRequest that = (TrainingRequest) o;
        return Objects.equals(traineeUsername, that.traineeUsername) && Objects.equals(trainerUsername, that.trainerUsername) && Objects.equals(trainingName, that.trainingName) && Objects.equals(trainingDate, that.trainingDate) && Objects.equals(trainingDuration, that.trainingDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traineeUsername, trainerUsername, trainingName, trainingDate, trainingDuration);
    }
}
