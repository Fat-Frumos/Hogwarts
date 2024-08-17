package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.trainee.TraineeTrainingRequest;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import java.util.List;

public interface TrainingService {
    List<TrainingTypeResponse> getTrainingTypes();

    List<TrainingResponse> getTrainerTrainingsByName(String username, TraineeTrainingRequest request);

    void createTraining(TrainingRequest request);
}
