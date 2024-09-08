package com.epam.esm.gym.service.profile;

import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeDto;
import com.epam.esm.gym.mapper.TrainingMapper;
import com.epam.esm.gym.service.TraineeService;
import com.epam.esm.gym.service.TrainerService;
import com.epam.esm.gym.service.TrainingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of TrainingService for managing training profiles.
 * Provides functionality for retrieving training types, trainer-specific trainings, and creating new trainings.
 * Integrates with TraineeService and TrainerService to manage relationships between trainees and trainers.
 * Utilizes TrainingDao for data access and TrainingMapper for entity-to-DTO conversions.
 * Handles exceptions during training creation and logs relevant information for debugging and monitoring.
 */
@Slf4j
@Service
@AllArgsConstructor
public class TrainingProfileService implements TrainingService {

    private final TrainingDao dao;
    private final TrainingMapper mapper;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    /**
     * {@inheritDoc}
     * Retrieves a list of all available training types.
     *
     * @return A list of {@link com.epam.esm.gym.dto.training.TrainingTypeDto}
     * objects representing the available training types.
     */
    @Override
    public List<TrainingTypeDto> getTrainingTypes() {
        List<TrainingType> trainingTypes = dao.findAllTrainingTypes();
        return trainingTypes.stream()
                .map(mapper::toType)
                .toList();
    }

    /**
     * {@inheritDoc}
     * Retrieves all trainings associated with a specific trainer's username.
     *
     * @param username The username of the trainer whose trainings are to be retrieved.
     * @param request  The {@link TrainingProfile} request object containing additional filters or parameters.
     * @return A {@link ResponseEntity} containing a list of {@link TrainingResponse} objects related to the trainer.
     */
    @Override
    public ResponseEntity<List<TrainingResponse>> getTrainerTrainingsByName(
            String username, TrainingProfile request) {
        List<Training> trainings = dao.findTrainingsByTrainerUsername(username);
        return ResponseEntity.ok(mapper.toDtos(trainings));
    }

    /**
     * {@inheritDoc}
     * Creates a new training session based on the provided request.
     *
     * @param request The {@link TrainingRequest} object containing details of the training to be created.
     * @throws RuntimeException If there is an error while saving the training entity.
     */
    @Override
    @Transactional
    public void createTraining(TrainingRequest request) {
        log.debug("Received createTraining request: {}", request);
        Trainee trainee = traineeService.getTrainee(request.getTraineeUsername());
        Trainer trainer = trainerService.getTrainer(request.getTrainerUsername());
        Training training = mapper.toEntity(request, trainee, trainer);
        Training saved = dao.save(training);
        log.info("Training entity saved successfully: {}", saved);
    }
}
