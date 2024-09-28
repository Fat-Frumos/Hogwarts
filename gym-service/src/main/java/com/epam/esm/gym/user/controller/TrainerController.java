package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.trainer.UpdateTrainerRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import com.epam.esm.gym.user.dto.trainer.TrainerRequest;
import com.epam.esm.gym.user.dto.training.TrainingProfile;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.service.TrainerService;
import com.epam.esm.gym.user.service.TrainingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for managing trainer-related operations.
 *
 * <p>This REST controller handles requests related to trainers, including registration and
 * management of trainer-specific data. It is designed to work with the endpoints defined in the
 * {@link ITrainerController} interface.</p>
 *
 * <p>The class is annotated with {@link RestController} to handle HTTP requests and responses.
 * The {@link RequestMapping} annotation sets the base URL for all endpoints in this controller
 * to "/api/trainers". The {@link Validated} annotation ensures that input validation is performed
 * on request parameters and bodies. The {@link AllArgsConstructor} annotation generates a constructor
 * with all required arguments to inject dependencies automatically.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/trainers")
public class TrainerController implements ITrainerController {

    private final TrainerService trainerService;
    private final TrainingService trainingService;

    /**
     * {@inheritDoc}
     * Retrieves a list of all trainers.
     *
     * @return {@link ResponseEntity} with status 200 if trainers are successfully retrieved,
     * or status 401 if unauthorized, and 403 if forbidden.
     */
    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TRAINER')")
    public ResponseEntity<List<TrainerProfile>> getAllTrainers() {
        return ResponseEntity.ok(trainerService.findAll());
    }

    /**
     * {@inheritDoc}
     * Creates a new trainer profile.
     *
     * @param request The {@link TrainerRequest} object with the trainer's details.
     * @return {@link ResponseEntity} with status 200 if profile is created,
     * or status 400 for bad request due to validation errors, 401 for unauthorized access,
     * and 403 for forbidden access.
     */
    @Override
    @PostMapping("/register")
    public ResponseEntity<ProfileResponse> registerTrainerProfile(
            @Valid @RequestBody TrainerRequest request) {
        return ResponseEntity.ok(trainerService.registerTrainer(request));
    }

    /**
     * {@inheritDoc}
     * Assigns a trainee to the trainer who is currently authenticated.
     *
     * @param traineeUsername The username of the trainee to be assigned.
     * @return {@link ResponseEntity} with status 200 if trainee is successfully assigned,
     * or status 400 for bad request, 401 for unauthorized access, 403 for forbidden access,
     * and 404 if trainee or trainer is not found.
     */
    @Override
    @PostMapping("/assign")
    @PreAuthorize("hasAuthority('ROLE_TRAINER')")
    public ResponseEntity<String> assignTraineeToTrainer(
            @RequestParam String traineeUsername) {
        trainerService.assignTraineeToTrainer(traineeUsername);
        return ResponseEntity.ok("Trainee assigned successfully");
    }

    /**
     * {@inheritDoc}
     * Deletes a trainer profile by username.
     *
     * @param username The username of the trainer to be deleted.
     * @return {@link ResponseEntity} with status 200 if trainer is deleted,
     * or status 401 for unauthorized access, 403 for forbidden access,
     * and 404 if trainer is not found.
     */
    @Override
    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> deleteTrainer(
            @PathVariable String username) {
        return ResponseEntity.ok(trainerService.deleteTrainer(username));
    }

    /**
     * {@inheritDoc}
     * Retrieves a trainer profile by username.
     *
     * @param username The username of the trainer whose profile is to be retrieved.
     * @return {@link ResponseEntity} with status 200 if profile is retrieved,
     * or status 401 for unauthorized access, 403 for forbidden access,
     * and 404 if trainer is not found.
     */
    @Override
    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TRAINER')")
    public ResponseEntity<TrainerProfile> getTrainerProfile(
            @PathVariable String username) {
        return ResponseEntity.ok(trainerService.getTrainerProfileByName(username));
    }

    /**
     * {@inheritDoc}
     * Updates an existing trainer profile by username.
     *
     * @param username The username of the trainer whose profile is to be updated.
     * @param request  The {@link com.epam.esm.gym.user.dto.trainer.UpdateTrainerRequest}
     *                 object with updated profile details.
     * @return {@link ResponseEntity} with status 200 if profile is updated,
     * or status 400 for bad request due to validation errors, 401 for unauthorized access,
     * 403 for forbidden access, 404 if trainer is not found, and 405 if method is not allowed.
     */
    @Override
    @PutMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TrainerProfile> updateTrainerProfile(
            @PathVariable String username,
            @Valid @RequestBody UpdateTrainerRequest request) {
        return ResponseEntity.ok(trainerService.updateTrainer(username, request));
    }

    /**
     * {@inheritDoc}
     * Retrieves a list of trainings associated with a trainer by username.
     *
     * @param username The username of the trainer whose trainings are to be retrieved.
     * @return {@link ResponseEntity} with status 200 if trainings are retrieved,
     * or status 400 for bad request, 401 for unauthorized access,
     * 403 for forbidden access, and 404 if trainer or training is not found.
     */
    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingResponse>> getTrainerTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
            @RequestParam(required = false) String traineeName) {
        TrainingProfile request = new TrainingProfile(username, traineeName, "", periodFrom, periodTo);
        return ResponseEntity.ok(trainingService.getTrainerTrainingsByName(request));
    }

    /**
     * {@inheritDoc}
     * Activates or deactivates a trainer profile.
     *
     * @param username The username of the trainer whose status is to be changed.
     * @param active   A boolean indicating whether to activate or deactivate the trainer.
     * @return {@link ResponseEntity} with status 200 if status change is successful,
     * or status 400 for bad request, 401 for unauthorized access,
     * 403 for forbidden access, and 404 if trainer is not found.
     */
    @Override
    @PatchMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> updateStatusTrainer(
            @PathVariable String username, @RequestParam Boolean active) {
        return ResponseEntity.ok(trainerService.updateStatusTrainerProfile(username, active));
    }

    /**
     * {@inheritDoc}
     * Retrieves a list of active trainers who are not assigned to any trainees.
     *
     * @param username The username of the admin requesting the list.
     * @return {@link ResponseEntity} with status 200 if list is retrieved,
     * or status 400 for bad request, 401 for unauthorized access,
     * and 403 for forbidden access.
     */
    @Override
    @GetMapping("/{username}/unassigned")
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<TrainerProfile>> getNotAssignedActiveTrainers(
            @PathVariable String username) {
        return !username.matches("^[a-zA-Z0-9._-]+$")
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok(trainerService.getNotAssigned(username));
    }
}
