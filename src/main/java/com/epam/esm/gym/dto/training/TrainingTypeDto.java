package com.epam.esm.gym.dto.training;

import com.epam.esm.gym.domain.Specialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Represents the response containing details about a training type.
 *
 * <p>This class provides the necessary details regarding a type of training, including the specialization it represents
 * and its unique identifier.</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeDto {
    private Specialization specialization;
    private Long trainingTypeId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrainingTypeDto that = (TrainingTypeDto) obj;
        return specialization == that.specialization && Objects.equals(trainingTypeId, that.trainingTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialization, trainingTypeId);
    }
}
