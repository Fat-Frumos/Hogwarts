package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingTypeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller interface for managing training-related operations.
 *
 * <p>This interface defines endpoints for interacting with training types and sessions.
 * It provides methods for retrieving available training types and creating new training sessions.
 * Access to these operations is restricted to users with TRAINER and ADMIN roles.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@RequestMapping("/api/trainings")
@Tag(name = "Training API", description = "Operations related to Trainings and Training Types")
public interface ITrainingController {

    /**
     * Retrieves a list of all available training types.
     *
     * <p>This endpoint returns a list of training types that are available in the system.
     * It is accessible only to users with TRAINER and ADMIN roles. The response provides a
     * detailed list of training types, each represented by a {@link com.epam.esm.gym.dto.training.TrainingTypeDto}.</p>
     *
     * @return a {@link ResponseEntity} containing
     * a list of {@link com.epam.esm.gym.dto.training.TrainingTypeDto} objects.
     * @see com.epam.esm.gym.dto.training.TrainingTypeDto
     */
    @GetMapping("/types")
    @Operation(
            summary = "Get Training Types",
            description = "Retrieve a list of all available training types. Accessible by TRAINER and ADMIN roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the training types"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<List<TrainingTypeDto>> getTrainingTypes();

    /**
     * Creates a new training session in the system.
     *
     * <p>This endpoint allows users to create a new training session by providing the necessary
     * details in the request body. It is accessible only to users with TRAINER and ADMIN roles.
     * The request body must be validated to ensure that it meets the required criteria for creating
     * a training session.</p>
     *
     * @param request the {@link TrainingRequest} object containing details for the new training session.
     * @return a {@link ResponseEntity} with no content if the training session is created successfully.
     */
    @PostMapping("/add")
    @Operation(
            summary = "Add Training",
            description = "Create a new training session. Accessible by TRAINER and ADMIN roles.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Training session created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation errors"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<Void> addTraining(
            @Valid @RequestBody TrainingRequest request);
}
