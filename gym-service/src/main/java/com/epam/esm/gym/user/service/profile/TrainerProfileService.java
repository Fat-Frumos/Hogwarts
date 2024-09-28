package com.epam.esm.gym.user.service.profile;

import com.epam.esm.gym.user.dao.JpaTrainerDao;
import com.epam.esm.gym.user.dao.JpaTrainingTypeDao;
import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileRequest;
import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.trainer.UpdateTrainerRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import com.epam.esm.gym.user.dto.trainer.TrainerRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerResponseDto;
import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.TrainingType;
import com.epam.esm.gym.user.entity.User;
import com.epam.esm.gym.user.exception.UserAlreadyExistsException;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.mapper.TrainerMapper;
import com.epam.esm.gym.user.service.TrainerService;
import com.epam.esm.gym.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing trainer profiles, implementing the TrainerService interface.
 * <p>
 * This class provides various methods to register, update, delete, and retrieve trainer profiles.
 * It also supports changing trainer passwords, activating/deactivating profiles, and assigning trainees.
 * The service uses a data access object (DAO) and a mapper to interact with the underlying data storage
 * and to map between entities and DTOs (Data Transfer Objects).
 * </p>
 */
@Slf4j
@Service
@AllArgsConstructor
public class TrainerProfileService implements TrainerService {

    private final TrainerMapper mapper;
    private final UserService userService;
    private final JpaTrainerDao trainerDao;
    private final JpaTrainingTypeDao trainingTypeDao;

    /**
     * {@inheritDoc}
     * Registers a new trainer by saving the trainer profile and returns a response with the profile details.
     */
    @Override
    @Transactional
    public ProfileResponse registerTrainer(TrainerRequest dto) {
        String rawPassword = userService.generateRandomPassword();
        User user = userService.createTrainerUser(dto, userService.encodePassword(rawPassword));
        User savedUser = userService.saveUser(user);
        Specialization specialization = Specialization.fromString(dto.getSpecialization());
        TrainingType trainingType = trainingTypeDao.findBySpecialization(specialization)
                .orElseGet(() -> trainingTypeDao.save(TrainingType.builder()
                        .specialization(specialization).build()));
        Trainer trainer = mapper.toTrainer(savedUser, trainingType);
        Trainer savedTrainer = trainerDao.save(trainer);
        return mapper.toProfileDto(savedTrainer.getUsername(), rawPassword);
    }

    /**
     * {@inheritDoc}
     * Changes the password of the trainer based on the provided {@link ProfileRequest}.
     */
    @Override
    public void changeTrainerPassword(ProfileRequest request) {
        userService.changePassword(request);
    }

    /**
     * {@inheritDoc}
     * Deletes the trainer identified by the given username.
     */
    @Override
    @Transactional
    public MessageResponse deleteTrainer(String username) {
        Optional<Trainer> trainerOptional = getTrainer(username);
        if (trainerOptional.isEmpty()) {
            return new MessageResponse(String.format("Trainer by name: %s not found", username));
        } else {
            trainerDao.delete(trainerOptional.get());
            return new MessageResponse("Trainer deleted successfully: " + username);
        }
    }

    /**
     * {@inheritDoc}
     * Retrieves the profile of the trainer identified by the given username.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TrainerProfile getTrainerProfileByName(String username) {
        Optional<Trainer> dao = trainerDao.findByUsername(username);
        return dao.map(mapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User Not Found by provided username " +  username));
    }

    /**
     * {@inheritDoc}
     * Retrieves trainers that are not assigned to a specific trainee identified by username.
     */
    @Override
    public List<TrainerResponseDto> getNotAssignedTrainers(String username) {
        return trainerDao.findNotAssignedTrainers(username)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    public void save(Trainer trainer) {
        trainerDao.save(trainer);
    }

    /**
     * {@inheritDoc}
     * Updates the trainer profile based on the given username and update request.
     */
    @Override
    public TrainerProfile updateTrainer(
            String username, UpdateTrainerRequest request) {
        Optional<Trainer> user = trainerDao.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Trainer not found for username: " + username);
        }

        if (trainerDao.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already in use: " + request.getUsername());
        }

        Trainer trainer = mapper.toEntity(request, user.get());
        return mapper.toDto(trainerDao.save(trainer));
    }

    /**
     * {@inheritDoc}
     * Retrieves a list of trainers who are not assigned to the given trainee.
     */
    @Override
    public List<TrainerProfile> getNotAssigned(String username) {
        List<Trainer> notAssigned = trainerDao.findNotAssignedTrainers(username);
        return mapper.toTrainerProfiles(notAssigned);
    }

    /**
     * {@inheritDoc}
     * Activates or deactivates the trainer profile identified by the given username based on the active status.
     */
    @Override
    public MessageResponse updateStatusTrainerProfile(String username, Boolean status) {
        trainerDao.activateTrainer(username, status);
        return new MessageResponse(String.format(
                "Trainer profile for '%s' has been updated to %s", username, status));
    }

    /**
     * {@inheritDoc}
     * This method is used internally to fetch a trainer and throws an exception if the trainer is not found.
     *
     * @param username The username of the trainer.
     * @return The {@link Trainer} entity.
     * @throws UserNotFoundException If the trainer with the given username is not found.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<Trainer> getTrainer(String username) {
        return trainerDao.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     * Retrieves a list of all trainer profiles.
     */
    @Override
    public List<TrainerProfile> findAll() {
        return mapper.toTrainerProfiles(trainerDao.findAllWithUsers());
    }

    /**
     * Assigns a trainee to the trainer who is currently authenticated.
     * This method uses the authenticated user's name from the security context to assign the trainee.
     *
     * @param traineeUsername The username of the trainee to be assigned.
     */
    @Override
    public void assignTraineeToTrainer(String traineeUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        trainerDao.assignTraineeToTrainer(authentication.getName(), traineeUsername);
    }

    /**
     * {@inheritDoc}
     * Retrieves a list of active trainers who are not yet assigned to the given trainee.
     */
    @Override
    public List<TrainerResponseDto> getActiveTrainersForTrainee(Trainee trainee) {
        return trainerDao.findNotAssignedTrainers(trainee.getUsername())
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }
}
