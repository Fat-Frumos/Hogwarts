package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerResponse;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import java.util.List;
import java.util.Optional;

public interface TrainerService {

    Optional<TrainerProfile> getTrainerByUsername(String username);

    void changeTrainerPassword(String username, String newPassword);

    void activateTrainer(String username);

    void deactivateTrainer(String username);

    void deleteTrainer(String username);

    ProfileResponse createTrainer(TrainerRequest request);

    TrainerProfile getTrainer(String username);

    TrainerProfile updateTrainer(String username, TrainerUpdateRequest request);

    List<TrainerProfile> getNotAssigned(String traineeUsername);

    void activateDeactivateProfile(String username, Boolean isActive);
}
