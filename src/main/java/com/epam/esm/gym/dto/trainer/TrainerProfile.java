package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfile {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingType specialization;
    private boolean active;
    private List<TraineeProfile> trainees;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainerProfile profile = (TrainerProfile) o;
        return active == profile.active && Objects.equals(username, profile.username) && Objects.equals(firstName, profile.firstName) && Objects.equals(lastName, profile.lastName) && Objects.equals(specialization, profile.specialization) && Objects.equals(trainees, profile.trainees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, specialization, active, trainees);
    }
}
