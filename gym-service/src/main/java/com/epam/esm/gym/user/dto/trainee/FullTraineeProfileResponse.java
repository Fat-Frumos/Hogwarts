package com.epam.esm.gym.user.dto.trainee;

import com.epam.esm.gym.user.dto.trainer.TrainerResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a slim version of a trainee profile, including essential personal information
 * and contact details. This class is used in scenarios where a full profile is not required.
 * The class is annotated with Lombok annotations to automatically generate getters,
 * a toString() method, a builder, a no-args constructor, and an all-args constructor.
 */
@Builder
public record FullTraineeProfileResponse(
        String firstName,
        String lastName,
        String username,
        String address,
        Boolean active,
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        LocalDate dateOfBirth,
        List<TrainerResponse> trainers
) {
}
