package com.epam.esm.gym.user.service.profile;

import com.epam.esm.gym.user.dao.JpaTraineeDao;
import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileRequest;
import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.profile.UserResponse;
import com.epam.esm.gym.user.dto.trainee.PostTraineeRequest;
import com.epam.esm.gym.user.dto.trainee.PutTraineeRequest;
import com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse;
import com.epam.esm.gym.user.dto.trainer.TrainerResponse;
import com.epam.esm.gym.user.dto.trainer.TrainerResponseDto;
import com.epam.esm.gym.user.dto.training.TrainingProfile;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.Training;
import com.epam.esm.gym.user.entity.User;
import com.epam.esm.gym.user.exception.UserAlreadyExistsException;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.mapper.TraineeMapper;
import com.epam.esm.gym.user.service.TraineeService;
import com.epam.esm.gym.user.service.TrainerService;
import com.epam.esm.gym.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the {@link TraineeService} for managing trainee profiles and related operations.
 * <p>
 * This class handles the core functionalities associated with trainee management, profile updates,
 * and retrieval of trainees' training sessions. It interacts with the underlying database and provides
 * business logic for operations like assigning trainers, validating user credentials, and managing activation status.
 * Additionally, it manages password changes and deletion of trainee profiles.
 * </p>
 */
@Service
@AllArgsConstructor
public class TraineeProfileService implements TraineeService {

    private final TraineeMapper mapper;
    private final JpaTraineeDao traineeDao;
    private final UserService userService;
    private final TrainerService trainerService;

    /**
     * {@inheritDoc}
     * Finds and retrieves all trainee profiles in the system.
     */
    @Override
    public List<FullTraineeProfileResponse> findAll(List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            return traineeDao.findAll().stream()
                    .map(mapper::toTraineeProfile).toList();
        }
        else {
            return traineeDao.findAllByUsernames(usernames).stream()
                    .map(mapper::toTraineeProfile).toList();
        }
    }

    /**
     * {@inheritDoc}
     * This method retrieves a list of active trainers that are not assigned to a specific trainee.
     * It looks up a trainee by their username, and if the trainee is found, it fetches a list of trainers
     * who are not assigned to this trainee and returns the information as a list of {@link TrainerResponseDto} objects.
     *
     * @param username the username of the trainee for whom active trainers need to be retrieved.
     * @return a {@link ResponseEntity} containing a list of {@link TrainerResponseDto} objects representing
     * the active trainers who are not assigned to the specified trainee. Returns {@code 200 OK} if trainers are found,
     * or {@code 404 Not Found} if the trainee is not found.
     * @throws UserNotFoundException if no trainee is found with the provided username.
     */
    @Override
    public List<TrainerResponseDto> getActiveTrainersForTrainee(String username) {
        Trainee trainee = traineeDao.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return trainerService.getActiveTrainersForTrainee(trainee);
    }

    /**
     * {@inheritDoc}
     * Registers a new trainee based on the provided request and returns the profile response.
     */
    @Override
    public ProfileResponse register(PostTraineeRequest dto) {
        String rawPassword = userService.generateRandomPassword();
        String password = userService.encodePassword(rawPassword);
        User user = userService.createTraineeUser(dto, password);
        User savedUser = userService.saveUser(user);
        Trainee trainee = traineeDao.save(mapper.toTrainee(savedUser, dto));
        return mapper.toProfile(trainee.getUser().getUsername(), rawPassword);
    }

    /**
     * {@inheritDoc}
     * Deletes a trainee profile based on the provided username.
     */
    @Override
    @Transactional
    public ResponseEntity<MessageResponse> deleteTrainee(String username) {
        Optional<Trainee> traineeOptional = traineeDao.findByUsername(username);
        if (traineeOptional.isPresent()) {
            traineeDao.delete(traineeOptional.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new MessageResponse(
                    String.format("Trainee with username '%s' has been successfully deleted.", username)));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(
                    String.format("Trainee with username '%s' not found.", username)));
        }
    }

    /**
     * {@inheritDoc}
     * Retrieves a trainee profile by the given username, returning a detailed profile if found.
     */
    @Override
    public FullTraineeProfileResponse getTraineeProfileByName(String username) {
        return traineeDao.findByUsername(username)
                .map(mapper::toTraineeProfile)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User by name: %s not found", username)));
    }

    /**
     * {@inheritDoc}
     * Updates an existing trainee profile with the provided request data.
     */
    @Override
    public FullTraineeProfileResponse updateTrainee(String username, PutTraineeRequest request) {
        Optional<Trainee> optional = traineeDao.findByUsername(username);
        if (optional.isEmpty()) {
            throw new UserNotFoundException(String.format("User by name: %s not found", username));
        }
        if (traineeDao.findByUsername(request.username()).isPresent()) {
            throw new UserAlreadyExistsException("Username already in use: " + request.username());
        }

        Trainee updatedTrainee = mapper.update(request, optional.get());
        userService.saveUser(updatedTrainee.getUser());
        Trainee updated = traineeDao.save(updatedTrainee);
        return mapper.toTraineeProfile(updated);
    }

    /**
     * {@inheritDoc}
     * Validates user credentials based on the provided profile request.
     */
    @Override
    public boolean validateUser(ProfileRequest request) {
        UserResponse user = userService.getUserByUsername(request.getUsername());
        return request.getOldPassword().equals(user.getPassword()) || user.getActive();
    }

    /**
     * {@inheritDoc}
     * Changes the password for the trainee based on the provided profile request.
     */
    @Override
    public void changePassword(ProfileRequest request) {
        userService.changePassword(request);
    }

    /**
     * {@inheritDoc}
     * Updates the list of trainers assigned to a trainee identified by their username.
     */
    @Override
    public List<TrainerResponse> updateTraineeTrainersByName(
            String username, List<String> trainersUsernames) {
        List<TrainerResponse> trainerResponses = trainersUsernames.stream()
                .map(trainerService::getTrainer)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(mapper::toTrainerProfile)
                .toList();

        Optional<Trainee> optionalTrainee = getTrainee(username);
        if (optionalTrainee.isPresent()) {
            Trainee trainee = optionalTrainee.get();
            trainee.setTrainers(mapper.toTrainers(trainerResponses));
            traineeDao.save(trainee);
            return trainerResponses;
        }
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     * Retrieves the training sessions of a trainee based on the provided username and filtering parameters.
     */
    @Override
    public List<TrainingResponse> getTraineeTrainingsByName(
            String username, Map<String, String> params) {
        return traineeDao.findByUsername(username)
                .map(trainee -> trainee.getTrainings()
                        .stream()
                        .filter(training -> matches(training, getProfile(params)))
                        .map(mapper::toResponse).toList())
                .orElseThrow(()-> new UserNotFoundException(username));
    }

    /**
     * {@inheritDoc}
     * Activates or deactivates a trainee's profile based on the provided status.
     */
    @Override
    public MessageResponse updateStatusProfile(String username, Boolean active) {
        Optional<Trainee> traineeOptional = getTrainee(username);
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            trainee.getUser().setActive(active);
            traineeDao.save(trainee);
            return new MessageResponse("Profile updated successfully");
        }
        return new MessageResponse("Trainee not found for username: " + username);
    }

    /**
     * {@inheritDoc}
     * Retrieves a trainee by their username, throwing an exception if not found.
     *
     * @throws UserNotFoundException If no trainee with the given username is found.
     */
    @Override
    public Optional<Trainee> getTrainee(String username) {
        return traineeDao.findByUsername(username);
    }

    /**
     * Constructs a {@link TrainingProfile} object from the provided parameters.
     * <p>
     * This method builds a {@link TrainingProfile} instance using parameters from
     * the provided map. It parses date strings for the training period, sets the
     * trainer name, and specifies the training type, if available in the map. If
     * certain parameters are not present, corresponding fields in the profile
     * will be set to null.
     * </p>
     *
     * @param params a map containing keys and values for period dates, trainer
     *               name, and training type.
     * @return a {@link TrainingProfile} built from the provided parameters.
     */
    private TrainingProfile getProfile(Map<String, String> params) {
        return TrainingProfile.builder()
                .periodFrom(params.containsKey("periodFrom")
                        ? LocalDate.parse(params.get("periodFrom")) : null)
                .periodTo(params.containsKey("periodTo")
                        ? LocalDate.parse(params.get("periodTo")) : null)
                .trainerName(params.get("trainerName"))
                .trainingType(params.get("trainingType"))
                .build();
    }

    /**
     * Checks if a given training matches the specified filter criteria.
     * <p>
     * This method evaluates whether a {@link Training} object meets the conditions
     * defined in a {@link TrainingProfile} filter. It compares the training dates,
     * trainer name, and training type against the filter values, returning true if
     * all criteria match. If any condition does not align, the training is
     * considered a non-match.
     * </p>
     *
     * @param training the training session to be evaluated against the filter.
     * @param filter   the {@link TrainingProfile} containing filter criteria.
     * @return true if the training matches all filter conditions; otherwise, false.
     */
    private boolean matches(Training training, TrainingProfile filter) {
        if (filter.getPeriodFrom() != null && training.getTrainingDate().isBefore(filter.getPeriodFrom())) {
            return false;
        }
        if (filter.getPeriodTo() != null && training.getTrainingDate().isAfter(filter.getPeriodTo())) {
            return false;
        }
        if (filter.getTrainerName() != null && !training.getTrainer().getUser().getUsername()
                .equalsIgnoreCase(filter.getTrainerName())) {
            return false;
        }
        return filter.getTrainingType() == null || training.getType().getSpecialization().name()
                .equalsIgnoreCase(filter.getTrainingType());
    }
}
