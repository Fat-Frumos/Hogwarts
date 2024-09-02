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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
    public ResponseEntity<List<TrainingResponse>> getTrainerTrainingsByName(
            String username, TrainingProfile request) {
        List<Training> trainings = dao.findTrainingsByTrainerUsername(username);
        return ResponseEntity.ok(mapper.toDtos(trainings));
    }

    @Override
    @Transactional
    public void createTraining(TrainingRequest request) {
        log.debug("Received createTraining request: {}", request);
        Trainee trainee = traineeService.getTrainee(request.getTraineeUsername());
        Trainer trainer = trainerService.getTrainer(request.getTrainerUsername());
        Training training = mapper.toEntity(request, trainee, trainer);
        try {
            dao.save(training);
            log.info("Training entity saved successfully: {}", training);
        } catch (Exception e) {
            log.error("Failed to save Training entity: {} {}", training, e.getMessage());
            throw new RuntimeException("Failed to save Training entity", e);
        }
    }
}
