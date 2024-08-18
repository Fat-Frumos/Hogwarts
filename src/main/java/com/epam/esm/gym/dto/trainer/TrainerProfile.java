package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.dto.trainee.TraineeProfile;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfile {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
    private List<TraineeProfile> trainees;
}
