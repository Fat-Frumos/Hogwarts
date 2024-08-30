package com.epam.esm.gym.dto.trainee;

import com.epam.esm.gym.dto.trainer.TrainerProfile;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfile  {
    private String firstName;
    private String lastName;
    private String username;
    private String address;
    private Boolean active;
    private LocalDate dateOfBirth;
    private List<TrainerProfile> trainers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraineeProfile profile = (TraineeProfile) o;
        return Objects.equals(firstName, profile.firstName) && Objects.equals(lastName, profile.lastName) && Objects.equals(username, profile.username) && Objects.equals(address, profile.address) && Objects.equals(active, profile.active) && Objects.equals(dateOfBirth, profile.dateOfBirth) && Objects.equals(trainers, profile.trainers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, username, address, active, dateOfBirth, trainers);
    }
}
