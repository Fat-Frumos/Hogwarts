package com.epam.esm.gym.service.profile;

import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.dao.jpa.TrainingTypeRepository;
import com.epam.esm.gym.domain.Specialization;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of TrainingService for managing training profiles.
 * Provides functionality for retrieving training types, trainer-specific trainings, and creating new trainings.
 * Integrates with TraineeService and TrainerService to manage relationships between trainees and trainers.
 * Utilizes TrainingDao for data access and TrainingMapper for entity-to-DTO conversions.
 * Handles exceptions during training creation and logs relevant information for debugging and monitoring.
 */
@Service
@AllArgsConstructor
public class TrainingProfileService implements TrainingService {

    private final TrainingDao dao;
    private final TrainingMapper mapper;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeRepository typeRepository;

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
     *
     * @param profile  The {@link TrainingProfile} request object containing additional filters or parameters.
     * @return A {@link ResponseEntity} containing a list of {@link TrainingResponse} objects related to the trainer.
     */
    public ResponseEntity<List<TrainingResponse>> getTrainerTrainingsByName(TrainingProfile profile) {
        Optional<Trainer> trainerOptional = trainerService.getTrainer(profile.getTrainerName());
        if (trainerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }

        Trainer trainer = trainerOptional.get();
        profile.setTrainerName(trainer.getUsername());
        return ResponseEntity.ok(
                dao.findTrainingsBy(profile)
                        .stream()
                        .map(mapper::toDto)
                        .toList());
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

        Optional<Training> trainingTypeOptional = dao.findByName(request.getTrainingName());
        if (trainingTypeOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Training already exists: " + request.getTrainingName()));
        }

        Trainer trainer = trainerOptional.get();
        List<TrainingType> trainingTypes = trainer.getTrainingTypes();
        if (trainingTypes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Trainer has no training types available."));
        }
        Specialization specialization = trainingTypes.get(0).getSpecialization();

        Training training = mapper.toEntity(request, traineeOptional.get(), trainerOptional.get());
        training.setType(typeRepository.findBySpecialization(specialization));
        TrainingResponse response = mapper.toDto(dao.save(training));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a list of all trainings and returns them as a response.
     * This method interacts with the data access object (DAO) to fetch all training records and then maps
     * them to a list of {@link TrainingResponse} objects using the mapper. The result is encapsulated in an
     * HTTP response with a status of OK (200).
     *
     * @return a {@link ResponseEntity} containing a list of {@link TrainingResponse} objects with status OK.
     */
    @Override
    public ResponseEntity<List<TrainingResponse>> getAllTrainings() {
        return ResponseEntity.ok().body(mapper.toResponses(dao.findAll()));
    }
}
