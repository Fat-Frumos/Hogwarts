package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.jms.dto.WorkloadRequest;
import com.epam.esm.gym.user.dto.training.TrainingRequest;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import com.epam.esm.gym.user.service.TrainingService;
import com.epam.esm.gym.user.service.WorkloadService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing training-related operations.
 *
 * <p>This REST controller handles HTTP requests related to training sessions and training types.
 * It implements the endpoints defined in the {@link ITrainingController} interface.</p>
 *
 * <p>The class is annotated with {@link RestController} to designate it as a Spring MVC controller,
 * where methods return domain objects directly rather than views. The {@link Validated} annotation
 * ensures that input parameters and request bodies are validated according to constraints. The
 * {@link AllArgsConstructor} annotation generates a constructor with parameters for all fields,
 * which facilitates dependency injection for any required services or components.</p>
 *
 * <p>Endpoint mappings are defined in the {@link ITrainingController} interface and managed
 * by this controller to provide functionality for retrieving training types and adding new training sessions.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/trainings")
public class TrainingController implements ITrainingController {

    private final TrainingService trainingService;
    private final WorkloadService workloadService;

    /**
     * Handles a training session update or creation.
     * This endpoint accepts a {@link com.epam.esm.gym.jms.dto.WorkloadRequest} object in the request
     * body and processes it by delegating to the {@link WorkloadService}.
     * The service handles the logic for updating or creating the training session.
     *
     * @param request the {@link com.epam.esm.gym.jms.dto.WorkloadRequest}
     *                 object containing training session details.
     */
    @PostMapping("/update")
    public ResponseEntity<MessageResponse> updateTrainingSession(
            @Valid @RequestBody WorkloadRequest request) {
        return ResponseEntity.ok(workloadService.updateWorkload(request));
    }

    /**
     * {@inheritDoc}
     * Retrieves a list of all available training types.
     *
     * @return A {@link ResponseEntity} containing a list of {@link com.epam.esm.gym.user.dto.training.TrainingTypeDto}.
     * Returns HTTP 200 if the list is successfully retrieved.
     * Includes HTTP 401 if unauthorized and HTTP 403 if access is forbidden.
     */
    @Override
    @GetMapping("/types")
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<TrainingTypeDto>> getTrainingTypes() {
        return ResponseEntity.ok(trainingService.getTrainingTypes());
    }

    /**
     * Retrieves a list of trainings accessible by users with roles of 'TRAINER', 'TRAINEE', or 'ADMIN'.
     * This method delegates the request to the {@link TrainingService} to fetch the list of trainings and
     * returns them as a response. Access is controlled through Spring Security annotations to ensure that
     * only users with the appropriate roles can access this endpoint.
     *
     * @return a {@link ResponseEntity} containing a list of {@link TrainingResponse} objects with status OK.
     */
    @Override
    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_TRAINEE') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<TrainingResponse>> getTrainings() {
        return ResponseEntity.ok(trainingService.getAllTrainings());
    }

    /**
     * {@inheritDoc}
     * Creates a new training session.
     *
     * @param request The {@link TrainingRequest} object containing details of the training session to be created.
     * @return A {@link ResponseEntity} with HTTP 201 if the training session is successfully created.
     * Includes HTTP 400 for bad request HTTP 401 if unauthorized, and HTTP 403 if access is forbidden.
     */
    @Override
    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TrainingResponse> addTraining(
            @Valid @RequestBody TrainingRequest request) {
        return ResponseEntity.ok(trainingService.createTraining(request));
    }

    /**
     * Deletes a training session profile by username.
     *
     * @param trainingName The username of the training to be deleted.
     * @return {@link ResponseEntity} with status 200 if trainer is deleted,
     * or status 401 for unauthorized access, 403 for forbidden access,
     * and 404 if training is not found.
     */
    @DeleteMapping("/{trainingName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> deleteTraining(
            @PathVariable String trainingName) {
        return ResponseEntity.ok(trainingService.removeTraining(trainingName));
    }
}
