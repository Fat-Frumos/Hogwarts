package com.epam.esm.gym.dto.trainee;

import com.epam.esm.gym.dto.trainer.TrainerProfile;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TraineeProfile extends TraineeRequest {
    private String username;
    private boolean isActive;
    private List<TrainerProfile> trainers;
}
