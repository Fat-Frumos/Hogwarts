package com.epam.esm.gym.dto.trainee;

import com.epam.esm.gym.dto.trainer.TrainerProfile;
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
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object for representing a trainer profile.
 *
 * <p>This DTO includes the trainer's username, first and last name,
 * specialization, active status, and associated trainees.</p>
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfile {
    private String firstName;
    private String lastName;
    private String username;
    private String address;
    private Boolean active;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate dateOfBirth;
    private List<TrainerProfile> trainers;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TraineeProfile profile = (TraineeProfile) obj;
        return Objects.equals(firstName, profile.firstName)
                && Objects.equals(lastName, profile.lastName)
                && Objects.equals(username, profile.username)
                && Objects.equals(address, profile.address)
                && Objects.equals(active, profile.active)
                && Objects.equals(dateOfBirth, profile.dateOfBirth)
                && Objects.equals(trainers, profile.trainers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, username, address, active, dateOfBirth, trainers);
    }
}
