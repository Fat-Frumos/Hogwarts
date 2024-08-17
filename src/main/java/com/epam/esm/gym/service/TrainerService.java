package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerDto;
import com.epam.esm.gym.dto.trainer.TrainerRegistrationDto;
import com.epam.esm.gym.dto.trainer.TrainerResponse;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import java.util.List;
import java.util.Optional;

public interface TrainerService {

    Optional<TrainerDto> getTrainerByUsername(String username);

    void changeTrainerPassword(String username, String newPassword);

    void activateTrainer(String username);

    void deactivateTrainer(String username);

    void deleteTrainer(String username);

    ProfileResponse registerTrainer(TrainerRegistrationDto request);

    TrainerResponse getTrainer(String username);

    TrainerDto updateTrainer(String username, TrainerUpdateRequest request);

    List<TrainerResponse> getNotAssigned(String traineeUsername);

    void activateDeactivateProfile(String username, Boolean isActive);
}
