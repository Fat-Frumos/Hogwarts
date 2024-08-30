package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrainingService {
    List<TrainingTypeResponse> getTrainingTypes();

    ResponseEntity<List<TrainingResponse>> getTrainerTrainingsByName(String username, TrainingProfile request);

    void createTraining(TrainingRequest request);
}
