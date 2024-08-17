package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import java.util.List;

public interface TraineeService {
    ProfileResponse register(TraineeRequest request);

    void deleteTrainee(String username);

    TraineeRequest getTraineeByName(String username);

    TraineeRequest updateTrainee(String username, TraineeUpdateRequest request);

    void validateUser(ProfileRequest request);

    void changePassword(ProfileRequest request);

    List<TrainerProfile> updateTraineeTrainersByName(String username, List<String> trainersUsernames);

    List<TrainingResponse> getTraineeTrainingsByName(String username, TrainingProfile request);

    void activateDeactivateProfile(String username, Boolean isActive);

    List<TrainerProfile> getNotAssignedTrainers(String username);
}
