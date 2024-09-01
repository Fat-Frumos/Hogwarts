package com.epam.esm.gym.dto.training;

import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingProfile {
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String trainerName;
    private String trainingType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingProfile profile = (TrainingProfile) o;
        return Objects.equals(periodFrom, profile.periodFrom) && Objects.equals(periodTo, profile.periodTo) && Objects.equals(trainerName, profile.trainerName) && Objects.equals(trainingType, profile.trainingType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periodFrom, periodTo, trainerName, trainingType);
    }
}
