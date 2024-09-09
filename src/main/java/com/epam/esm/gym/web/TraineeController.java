package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.SlimTrainerProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.service.TraineeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controller for managing trainee-related operations.
 * Implements ITraineeController to provide endpoints for handling trainee profiles.
 * Uses TraineeService to perform business logic and interact with the data layer.
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/trainees")
public class TraineeController implements ITraineeController {

    private TraineeService service;

    /**
     * {@inheritDoc}
     * Retrieves all trainee profiles.
     *
     * @return ResponseEntity containing a list of TraineeProfile objects.
     * Returns HTTP 200 if the operation is successful.
     * In case of an error, appropriate HTTP status codes are returned.
     * Useful for admins and trainers who need to view all trainees.
     */
    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TRAINER')")
    public ResponseEntity<List<BaseResponse>> getAllTrainees() {
        return service.findAll();
    }

    /**
     * {@inheritDoc}
     * Registers a new trainee with the provided details.
     *
     * @param request the TraineeRequest object with details of the trainee to register.
     * @return ResponseEntity containing a ProfileResponse object with details of the registered trainee.
     * This operation is open to any authenticated user.
     * Validation is performed on the incoming request body.
     * Returns HTTP 201 for successful registration and HTTP 400 for invalid input.
     */
    @Override
    @PostMapping("/register")
    public ResponseEntity<ProfileResponse> registerTrainee(
            @Valid @RequestBody TraineeRequest request) {
        return service.register(request);
    }

    /**
     * {@inheritDoc}
     * Retrieves a specific trainee profile by username.
     *
     * @param username the username of the trainee whose profile is to be retrieved.
     * @return ResponseEntity containing the TraineeProfile object for the specified username.
     * Returns HTTP 200 if the profile is found, otherwise an appropriate error response.
     * Useful for viewing details of a specific trainee. Handles errors if the username does not exist.
     */
    @Override
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_TRAINER', 'ROLE_ADMIN', 'ROLE_TRAINEE')")
    public ResponseEntity<BaseResponse> getTraineeProfile(
            @PathVariable @NotNull @Valid String username) {
        return service.getTraineeProfileByName(username);
    }

    /**
     * {@inheritDoc}
     * Updates the profile of a specific trainee.
     *
     * @param username the username of the trainee whose profile is to be updated.
     * @param request  the TraineeRequest object with updated details.
     * @return ResponseEntity containing the updated TraineeProfile object.
     * Handles validation of input data to ensure correctness.
     * Returns HTTP 200 for a successful update or appropriate error codes if issues occur.
     */
    @Override
    @PutMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse> updateTraineeProfile(
            @PathVariable String username,
            @Valid @RequestBody TraineeRequest request) {
        return service.updateTrainee(username, request);
    }

    /**
     * {@inheritDoc}
     * Deletes the profile of a specific trainee.
     *
     * @param username the username of the trainee whose profile is to be deleted.
     * @return ResponseEntity with HTTP 204 for a successful deletion with no content.
     * Only accessible by users with 'ROLE_ADMIN' authority.
     * Handles errors such as non-existent trainees.
     * Ensures that unauthorized users cannot perform deletions.
     */
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTraineeProfile(
            @PathVariable String username) {
        return service.deleteTrainee(username);
    }

    /**
     * {@inheritDoc}
     * Updates the list of trainers assigned to a specific trainee.
     *
     * @param username          the username of the trainee whose trainers are to be updated.
     * @param trainersUsernames list of usernames of trainers to assign to the trainee.
     * @return ResponseEntity containing a list of updated TrainerProfile objects.
     * Accessible by users with 'ROLE_TRAINER' or 'ROLE_ADMIN' authority.
     * Validates input data to ensure trainers are correctly assigned.
     * Returns HTTP 200 for a successful update or appropriate error codes if issues occur.
     */
    @Override
    @PutMapping("/{username}/trainers")
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<SlimTrainerProfile>> updateTraineeTrainers(
            @PathVariable String username,
            @Valid @RequestBody List<String> trainersUsernames) {
        return service.updateTraineeTrainersByName(username, trainersUsernames);
    }

    /**
     * {@inheritDoc}
     * Retrieves all training sessions associated with a specific trainee.
     *
     * @param username the username of the trainee whose training sessions are to be retrieved.
     * @param params   optional query parameters for filtering the training sessions.
     * @return ResponseEntity containing a list of TrainingResponse objects representing the trainee's trainings.
     * Accessible by users with 'ROLE_TRAINER' or 'ROLE_ADMIN' authority.
     * Handles query parameters to filter or sort the training sessions.
     * Returns HTTP 200 for a successful retrieval or appropriate error codes if issues occur.
     */
    @Override
    @GetMapping("/{username}/trainings")
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<TrainingResponse>> getTraineeTrainings(
            @PathVariable String username,
            @RequestParam Map<String, String> params) {
        return service.getTraineeTrainingsByName(username, params);
    }

    /**
     * {@inheritDoc}
     * Activates or deactivates a trainee profile.
     *
     * @param username the username of the trainee whose profile is to be activated or deactivated.
     * @param active   Boolean flag indicating whether to activate or deactivate the profile.
     * @return ResponseEntity with HTTP 204 for a successful operation with no content.
     * Only accessible by users with 'ROLE_ADMIN' authority.
     * Ensures proper activation or deactivation of trainee profiles based on the provided flag.
     * Handles cases where the username does not exist or errors occur during the process.
     */
    @Override
    @PatchMapping("/{username}/activate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> activateDeactivateTrainee(
            @PathVariable String username, @RequestParam Boolean active) {
        return service.activateDeactivateProfile(username, active);
    }
}
