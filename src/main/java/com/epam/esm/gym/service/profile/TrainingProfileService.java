package com.epam.esm.gym.service.profile;

import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.auth.MessageResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
                .map(TrainingMapper::toType)
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
        return ResponseEntity.ok(mapper.toResponses(trainings));
    }

    /**
     * {@inheritDoc}
     * Creates a new training session based on the provided request.
     *
     * @param request The {@link TrainingRequest} object containing details of the training to be created.
     */
    @Override
    @Transactional
    public ResponseEntity<BaseResponse> createTraining(TrainingRequest request) {
        Optional<Trainee> traineeOptional = traineeService.getTrainee(request.getTraineeUsername());
        if (traineeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Trainee not found for username: " + request.getTraineeUsername()));
        }
        Optional<Trainer> trainerOptional = trainerService.getTrainer(request.getTrainerUsername());
        if (trainerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Trainer not found for username: " + request.getTrainerUsername()));
        }

        Training savedTraining = dao.save(mapper.toEntity(request, traineeOptional.get(), trainerOptional.get()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Training saved successfully: " + savedTraining));
    }
}
