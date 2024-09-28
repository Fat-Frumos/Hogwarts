package com.epam.esm.gym.user.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @NotBlank(message = "Required request parameter 'traineeUsername' is not present")
    @Size(min = 1, max = 128, message = "Trainee username must be between 1 and 128 characters")
    private String traineeUsername;

    @NotBlank(message = "Required request parameter 'trainerUsername' is not present")
    @Size(min = 1, max = 128, message = "Trainer username must be between 1 and 128 characters")
    private String trainerUsername;
    @NotBlank(message = "Required request parameter 'trainingName' is not present")
    @Size(min = 1, max = 128, message = "Training name must be between 1 and 128 characters")
    private String trainingName;
    @NotNull(message = "Required request parameter 'trainingDuration' is not present")
    @Max(value = 1440, message = "Training duration cannot exceed 1440 minutes")
    private Integer trainingDuration;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @FutureOrPresent(message = "Training date must be today or in the future.")
    private LocalDate trainingDate;

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
