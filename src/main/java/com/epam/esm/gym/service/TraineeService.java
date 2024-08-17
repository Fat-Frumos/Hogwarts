package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeRegistrationRequestDto;
import com.epam.esm.gym.dto.trainee.TraineeUpdateRequestDto;
import com.epam.esm.gym.dto.trainer.TrainerResponse;
import com.epam.esm.gym.dto.trainee.TraineeTrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import java.util.List;

public interface TraineeService {
    ProfileResponse register(TraineeRegistrationRequestDto request);

    void deleteTrainee(String username);

    TraineeRegistrationRequestDto getTraineeByName(String username);

    TraineeRegistrationRequestDto updateTrainee(String username, TraineeUpdateRequestDto request);

    void validateUser(ProfileRequest request);

    void changePassword(ProfileRequest request);

    List<TrainerResponse> updateTraineeTrainersByName(String username, List<String> trainerUsernames);

    List<TrainingResponse> getTraineeTrainingsByName(String username, TraineeTrainingRequest request);

    void activateDeactivateProfile(String username, Boolean isActive);
}
