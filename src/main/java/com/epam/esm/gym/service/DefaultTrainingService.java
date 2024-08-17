package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.trainee.TraineeTrainingRequest;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DefaultTrainingService implements TrainingService {
    @Override
    public List<TrainingTypeResponse> getTrainingTypes() {
        return null;
    }

    @Override
    public List<TrainingResponse> getTrainerTrainingsByName(String username, TraineeTrainingRequest request) {
        return null;
    }

    @Override
    public void createTraining(TrainingRequest request) {

    }
}
