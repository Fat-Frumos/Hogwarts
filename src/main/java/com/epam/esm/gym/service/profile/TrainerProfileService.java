package com.epam.esm.gym.service.profile;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.SlimTrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.esm.gym.exception.UserNotFoundException;
import com.epam.esm.gym.mapper.TrainerMapper;
import com.epam.esm.gym.service.TrainerService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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
@Transactional
@AllArgsConstructor
public class TrainerProfileService implements TrainerService {

    private final UserProfileService userService;
    private final TrainerMapper mapper;
    private final TrainerDao dao;

    /**
     * {@inheritDoc}
     * Registers a new trainer by saving the trainer profile and returns a response with the profile details.
     */
    @Override
    public ResponseEntity<ProfileResponse> registerTrainer(TrainerRequest dto) {
        User user = userService.createTrainerUser(dto);
        String rawPassword = userService.generateRandomPassword();
        String password = userService.encodePassword(rawPassword);
        user.setPassword(password);
        user.setPermission(RoleType.ROLE_TRAINER);
        Trainer trainer = mapper.toTrainer(user, dto);
        Trainer saved = dao.save(trainer);
        log.info("Saved user: {}", saved);
        ProfileResponse response = mapper.toProfileDto(trainer.getUsername(), rawPassword);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
    public void deleteTrainer(String username) {
        dao.delete(getTrainer(username));
    }

    /**
     * {@inheritDoc}
     * Retrieves the profile of the trainer identified by the given username.
     */
    @Override
    public ResponseEntity<SlimTrainerProfile> getTrainerProfileByName(String username) {
        return ResponseEntity.ok(mapper.toDto(getTrainer(username)));
    }

    /**
     * {@inheritDoc}
     * Updates the trainer profile based on the given username and update request.
     */
    @Override
    public ResponseEntity<TrainerProfile> updateTrainer(
            String username, TrainerUpdateRequest request) {
        Trainer trainer = mapper.toEntity(request);
        TrainerProfile profile = mapper.toDto(dao.save(trainer));
        return ResponseEntity.ok(profile);
    }

    /**
     * {@inheritDoc}
     * Retrieves a list of trainers who are not assigned to the given trainee.
     */
    @Override
    public ResponseEntity<List<TrainerProfile>> getNotAssigned(String username) {
        List<Trainer> notAssigned = dao.findNotAssigned(username);
        return ResponseEntity.ok(mapper.toTrainerProfiles(notAssigned));
    }

    /**
     * {@inheritDoc}
     * Activates or deactivates the trainer profile identified by the given username based on the active status.
     */
    @Override
    public ResponseEntity<Void> activateDeactivateProfile(String username, Boolean active) {
        dao.activateTrainer(username, active);
        return ResponseEntity.ok().build();
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
    public Trainer getTrainer(String username) {
        return dao.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    /**
     * {@inheritDoc}
     * Retrieves a list of all trainer profiles.
     */
    @Override
    public ResponseEntity<List<TrainerProfile>> findAll() {
        return ResponseEntity.ok(mapper.toTrainerProfiles(dao.findAll()));
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
        dao.assignTraineeToTrainer(authentication.getName(), traineeUsername);
    }
}
