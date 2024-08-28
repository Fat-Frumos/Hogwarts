package com.epam.esm.gym.service;

import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import java.util.List;

public interface TrainerService {

    void changeTrainerPassword(ProfileRequest request);

    void activateTrainer(String username);

    void deactivateTrainer(String username);

    void deleteTrainer(String username);

    ProfileResponse registerTrainer(TrainerRequest request);

    TrainerProfile getTrainerProfileByName(String username);

    TrainerProfile updateTrainer(String username, TrainerUpdateRequest request);

    List<TrainerProfile> getNotAssigned(String traineeUsername);

    void activateDeactivateProfile(String username, Boolean active);

    Trainer getTrainer(String trainerUsername);
}
