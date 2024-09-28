package com.epam.esm.gym.user.dto.training;

import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a training profile containing details about a specific training session.
 *
 * <p>This class encapsulates information about a training session,
 * including the period, trainer, and type of training.</p>
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingProfile {
    private String trainerName;
    private String traineeName;
    private String trainingType;
    private LocalDate periodFrom;
    private LocalDate periodTo;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrainingProfile profile = (TrainingProfile) obj;
        return Objects.equals(periodFrom, profile.periodFrom)
                && Objects.equals(periodTo, profile.periodTo)
                && Objects.equals(trainerName, profile.trainerName)
                && Objects.equals(trainingType, profile.trainingType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periodFrom, periodTo, trainerName, trainingType);
    }
}
