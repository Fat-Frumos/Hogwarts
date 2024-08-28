package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfile {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingType specialization;
    private boolean active;
    private List<TraineeProfile> trainees;
}
