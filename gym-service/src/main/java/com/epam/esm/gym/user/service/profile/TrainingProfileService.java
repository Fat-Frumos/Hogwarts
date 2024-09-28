package com.epam.esm.gym.user.service.profile;

import com.epam.esm.gym.user.broker.WorkloadJmsService;
import com.epam.esm.gym.user.dao.JpaTrainingDao;
import com.epam.esm.gym.user.dao.JpaTrainingTypeDao;
import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.training.TrainingProfile;
import com.epam.esm.gym.user.dto.training.TrainingRequest;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.Training;
import com.epam.esm.gym.user.entity.TrainingType;
import com.epam.esm.gym.user.exception.UserAlreadyExistsException;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.mapper.TrainingMapper;
import com.epam.esm.gym.user.service.TraineeService;
import com.epam.esm.gym.user.service.TrainerService;
import com.epam.esm.gym.user.service.TrainingService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.gym.jms.ActiveMQConfig.ADD_TRAINING_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.DELETE_TRAINING_QUEUE;

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

    private final JpaTrainingDao dao;
    private final TrainingMapper mapper;
    private final WorkloadJmsService workloadJmsService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final JpaTrainingTypeDao typeDao;

    /**
     * {@inheritDoc}
     * Retrieves a list of all available training types.
     *
     * @return A list of {@link com.epam.esm.gym.user.dto.training.TrainingTypeDto}
     * objects representing the available training types.
     */
    @Override
    public List<TrainingTypeDto> getTrainingTypes() {
        return typeDao.findAll().stream()
                .map(TrainingMapper::toType)
                .toList();
    }

    /**
     * {@inheritDoc}
     * Retrieves all trainings associated with a specific trainer's username.
     *
     * @param profile The {@link TrainingProfile} request object containing additional filters or parameters.
     * @return A {@link ResponseEntity} containing a list of {@link TrainingResponse} objects related to the trainer.
     */
    public List<TrainingResponse> getTrainerTrainingsByName(TrainingProfile profile) {
        Optional<Trainer> trainerOptional = trainerService.getTrainer(profile.getTrainerName());
        if (trainerOptional.isEmpty()) {
            return Collections.emptyList();
        }

        Trainer trainer = trainerOptional.get();
        profile.setTrainerName(trainer.getUsername());

        if (!trainer.getTrainees().isEmpty() && StringUtils.isNotBlank(profile.getTraineeName())) {
            profile.setTraineeName(trainer.getTrainees().iterator().next().getUsername());
        }

        return dao.findTrainingsByTrainerName(
                profile.getTrainerName(),
                        profile.getPeriodFrom(),
                        profile.getPeriodTo())
                .stream()
                .map(TrainingMapper::toDto)
                .toList();
    }


    /**
     * {@inheritDoc}
     * Creates a new training session based on the provided request.
     *
     * @param request The {@link TrainingRequest} object containing details of the training to be created.
     */
    @Override
    @Transactional
    public TrainingResponse createTraining(TrainingRequest request) {
        Optional<Trainee> traineeOptional = traineeService.getTrainee(request.getTraineeUsername());
        if (traineeOptional.isEmpty()) {
            throw new UserNotFoundException("Trainee not found for username: " + request.getTraineeUsername());
        }
        Trainee trainee = traineeOptional.get();

        Optional<Trainer> trainerOptional = trainerService.getTrainer(request.getTrainerUsername());
        if (trainerOptional.isEmpty()) {
            throw new UserNotFoundException("Trainer not found for username: " + request.getTrainerUsername());
        }

        Optional<Training> trainingTypeOptional = dao.findByTrainingName(request.getTrainingName());
        if (trainingTypeOptional.isPresent()) {
            throw new UserAlreadyExistsException("Training already exists: " + request.getTrainingName());
        }

        Trainer trainer = trainerOptional.get();
        if (!trainer.getTrainees().contains(trainee)) {
            trainer.getTrainees().add(trainee);
            trainee.getTrainers().add(trainer);
        }
        trainerService.save(trainer);
        Training training = mapper.toEntity(request, trainee, trainerOptional.get());
        training.setType(typeDao.findBySpecialization(trainer.getTrainingType().getSpecialization())
                .orElse(TrainingType.builder().specialization(Specialization.DEFAULT).build()));
        TrainingResponse trainingResponse = TrainingMapper.toDto(dao.save(training));
        workloadJmsService.convertAndSend(ADD_TRAINING_QUEUE, trainingResponse);
        return trainingResponse;
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
    public List<TrainingResponse> getAllTrainings() {
        return TrainingMapper.toResponses(dao.findAll());
    }

    /**
     * Saves the given TrainingType entity to the database.
     *
     * @param type the TrainingType entity to be saved
     * @return the saved TrainingType entity, including any generated fields
     */
    @Override
    @Transactional
    public TrainingType save(TrainingType type) {
        return typeDao.save(type);
    }

    /**
     * {@inheritDoc}
     * Deletes a new training session based on the provided name.
     */
    @Override
    public MessageResponse removeTraining(String trainingName) {
        Training training = dao.findByTrainingName(trainingName)
                .orElseThrow(()-> new UserNotFoundException("Trainer not found for username: " + trainingName));
        dao.delete(training);
        workloadJmsService.convertAndSend(DELETE_TRAINING_QUEUE, training);
        return new MessageResponse("Training successfully deleted by provided name");
    }
}
