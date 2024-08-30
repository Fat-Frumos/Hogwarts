package com.epam.esm.gym.dto.training;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingResponse {
    private String trainerName;
    private String trainingName;
    private String trainingType;
    private int trainingDuration;
    private LocalDate trainingDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingResponse that = (TrainingResponse) o;
        return trainingDuration == that.trainingDuration && Objects.equals(trainerName, that.trainerName) && Objects.equals(trainingName, that.trainingName) && Objects.equals(trainingType, that.trainingType) && Objects.equals(trainingDate, that.trainingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainerName, trainingName, trainingType, trainingDuration, trainingDate);
    }
}
