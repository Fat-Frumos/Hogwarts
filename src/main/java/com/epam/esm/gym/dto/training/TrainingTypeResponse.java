package com.epam.esm.gym.dto.training;

import com.epam.esm.gym.domain.Specialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeResponse {
    private Specialization trainingType;
    private Long trainingTypeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingTypeResponse that = (TrainingTypeResponse) o;
        return trainingType == that.trainingType && Objects.equals(trainingTypeId, that.trainingTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainingType, trainingTypeId);
    }
}
