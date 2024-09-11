package com.epam.esm.gym.dto.training;

import com.epam.esm.gym.dto.auth.BaseResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents the response containing details about a training session.
 *
 * <p>This class provides the information needed to present the details of a training session, including the trainer,
 * the training name, type, and duration.</p>
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingResponse implements BaseResponse {
    private String trainerName;
    private String trainingName;
    private String trainingType;
    private int trainingDuration;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate trainingDate;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrainingResponse that = (TrainingResponse) obj;
        return trainingDuration == that.trainingDuration
                && Objects.equals(trainerName, that.trainerName)
                && Objects.equals(trainingName, that.trainingName)
                && Objects.equals(trainingType, that.trainingType)
                && Objects.equals(trainingDate, that.trainingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainerName, trainingName, trainingType, trainingDuration, trainingDate);
    }
}
