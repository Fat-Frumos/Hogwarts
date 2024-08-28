package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import com.epam.esm.gym.mapper.TrainingMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TrainingProfileService implements TrainingService {

    private final TrainingDao dao;
    private final TrainingMapper mapper;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Override
    public List<TrainingTypeResponse> getTrainingTypes() {
        List<TrainingType> trainingTypes = dao.findAllTrainingTypes();
        return trainingTypes.stream()
                .map(mapper::toType)
                .toList();
    }

    @Override
    public List<TrainingResponse> getTrainerTrainingsByName(String username, TrainingProfile request) {
        List<Training> trainings = dao.findTrainingsByTrainerUsername(username);
        return mapper.toDtos(trainings);
    }

    @Override
    public void createTraining(TrainingRequest request) {
        Trainee trainee = traineeService.getTrainee(request.getTraineeUsername());
        Trainer trainer = trainerService.getTrainer(request.getTrainerUsername());
        Training training = mapper.toEntity(request, trainee, trainer);
        dao.save(training);
    }
}
