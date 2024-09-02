package com.epam.esm.gym.service;

import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrainerService {

    void changeTrainerPassword(ProfileRequest request);

    void deleteTrainer(String username);

    ResponseEntity<ProfileResponse> registerTrainer(TrainerRequest request);

    ResponseEntity<TrainerProfile> getTrainerProfileByName(String username);

    ResponseEntity<TrainerProfile> updateTrainer(String username, TrainerUpdateRequest request);

    ResponseEntity<List<TrainerProfile>> getNotAssigned(String traineeUsername);

    ResponseEntity<Void> activateDeactivateProfile(String username, Boolean active);

    Trainer getTrainer(String trainerUsername);

    ResponseEntity<List<TrainerProfile>> findAll();

    void assignTraineeToTrainer(String traineeUsername);
}
