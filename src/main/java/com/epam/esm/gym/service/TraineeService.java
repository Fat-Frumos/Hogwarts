package com.epam.esm.gym.service;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TraineeService {
    ResponseEntity<ProfileResponse> register(TraineeRequest request);

    ResponseEntity<Void> deleteTrainee(String username);

    ResponseEntity<TraineeProfile> getTraineeProfileByName(String username);

    ResponseEntity<TraineeProfile> updateTrainee(String username, TraineeRequest request);

    boolean validateUser(ProfileRequest request);

    void changePassword(ProfileRequest request);

    ResponseEntity<List<TrainerProfile>> updateTraineeTrainersByName(String username, List<String> trainersUsernames);

    ResponseEntity<List<TrainingResponse>> getTraineeTrainingsByName(String username, Map<String, String> params);

    ResponseEntity<Void> activateDeactivateProfile(String username, Boolean active);

    ResponseEntity<List<TrainerProfile>> getNotAssignedTrainers(String username);

    Trainee getTrainee(String traineeUsername);
}
