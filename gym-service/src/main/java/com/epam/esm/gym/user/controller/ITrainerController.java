package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.trainer.UpdateTrainerRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import com.epam.esm.gym.user.dto.trainer.TrainerRequest;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for the Trainer API, defining operations related to trainer profiles and assignments.
 *
 * <p>This interface declares various endpoints for managing trainers, including retrieving profiles,
 * creating, updating, and deleting trainer profiles, as well as assigning trainees to trainers. It
 * also includes operations for getting lists of trainers and their trainings. The API is designed to be
 * used by ADMIN and TRAINER roles with specific access controls in place.</p>
 *
 * <p>Each method is annotated with {@link Operation} to provide detailed documentation for Swagger/OpenAPI
 * integration, specifying summary, description, and possible responses for the API operations.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@Tag(name = "Trainer API", description = "Operations related to Trainer profiles and assignments")
public interface ITrainerController {

    /**
     * Retrieves a list of all trainers.
     *
     * <p>This endpoint is accessible by users with roles ADMIN or TRAINER. It returns a list of all trainer
     * profiles available in the system.</p>
     *
     * @return A {@link ResponseEntity} containing a list of {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile}.
     */
    @Operation(
            summary = "Get All Trainers",
            description = "Retrieve a list of all trainers. Accessible by users with role ADMIN or TRAINER.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of trainers retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<List<TrainerProfile>> getAllTrainers();

    /**
     * Creates a new trainer profile.
     *
     * <p>This endpoint allows ADMIN users to create a new trainer profile. The request body must be valid
     * as per the {@link TrainerRequest} constraints.</p>
     *
     * @param request A {@link TrainerRequest} object containing trainer details.
     * @return A {@link ResponseEntity} with {@link com.epam.esm.gym.user.dto.profile.UserProfile}
     * indicating the result of the creation.
     */
    @Operation(
            summary = "Create Trainer Profile",
            description = "Create a new trainer profile. Accessible only by ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<ProfileResponse> registerTrainerProfile(@Valid @RequestBody TrainerRequest request);

    /**
     * Assigns a trainee to the currently authenticated trainer.
     *
     * <p>This endpoint is accessible only by TRAINER roles. The trainer is assumed to be the currently
     * authenticated user who will be assigned to the specified trainee.</p>
     *
     * @param traineeUsername The username of the trainee to be assigned.
     * @return A {@link ResponseEntity} with a success message if the assignment was successful.
     */
    @Operation(
            summary = "Assign Trainee to Trainer",
            description = "Assign a trainee to the trainer who is currently authenticated. Accessible only by TRAINER.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee assigned successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access"),
                    @ApiResponse(responseCode = "404", description = "Trainee or trainer not found")
            }
    )
    ResponseEntity<String> assignTraineeToTrainer(@RequestParam String traineeUsername);

    /**
     * Deletes a trainer profile by username.
     *
     * <p>This endpoint is accessible only by ADMIN roles. It removes the trainer profile associated with
     * the specified username from the system.</p>
     *
     * @param username The username of the trainer to be deleted.
     * @return A {@link ResponseEntity} with a success message if the deletion was successful.
     */
    @Operation(
            summary = "Delete Trainer",
            description = "Delete a trainer profile by username. Accessible only by ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer deleted successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access"),
                    @ApiResponse(responseCode = "404", description = "Trainer not found")
            }
    )
    ResponseEntity<MessageResponse> deleteTrainer(@PathVariable String username);

    /**
     * Retrieves a trainer profile by username.
     *
     * <p>This endpoint is accessible by users with roles ADMIN or TRAINER. It returns the profile of
     * the trainer identified by the specified username.</p>
     *
     * @param username The username of the trainer whose profile is to be retrieved.
     * @return A {@link ResponseEntity} containing
     * the {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} if found.
     */
    @Operation(
            summary = "Get Trainer Profile",
            description = "Retrieve a trainer profile by username. Accessible by users with role ADMIN or TRAINER.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access"),
                    @ApiResponse(responseCode = "404", description = "Trainer not found")
            }
    )
    ResponseEntity<TrainerProfile> getTrainerProfile(@PathVariable String username);

    /**
     * Updates an existing trainer profile by username.
     *
     * <p>This endpoint allows ADMIN or the trainer themselves to update the profile. The request body
     * must be valid as per the {@link com.epam.esm.gym.user.dto.trainer.UpdateTrainerRequest} constraints.</p>
     *
     * @param username The username of the trainer to be updated.
     * @param request  A {@link com.epam.esm.gym.user.dto.trainer.UpdateTrainerRequest}
     *                 containing updated trainer details.
     * @return A {@link ResponseEntity} containing the updated {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile}.
     */
    @Operation(
            summary = "Update Trainer Profile",
            description = "Update an existing trainer profile by username. Accessible by ADMIN or the TRAINER.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access"),
                    @ApiResponse(responseCode = "404", description = "Trainer not found"),
                    @ApiResponse(responseCode = "405", description = "Method not allowed")
            }
    )
    ResponseEntity<TrainerProfile> updateTrainerProfile(
            @PathVariable String username, @Valid @RequestBody UpdateTrainerRequest request);

    /**
     * Retrieves a list of trainings associated with a trainer by username.
     *
     * <p>This endpoint is accessible by users with roles ADMIN or TRAINER. It returns a list of training
     * sessions associated with the trainer identified by the specified username.</p>
     *
     * @param username The username of the trainer whose trainings are to be retrieved.
     * @return A {@link ResponseEntity} containing a list of {@link TrainingResponse}.
     */
    @Operation(
            summary = "Get Trainer Trainings List",
            description = "Retrieve a list of trainings associated with a trainer by username. " +
                    "Accessible by users with role ADMIN or TRAINER.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of trainer's trainings find successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access"),
                    @ApiResponse(responseCode = "404", description = "Trainer or training not found")
            }
    )
    ResponseEntity<List<TrainingResponse>> getTrainerTrainings(
            @PathVariable String username,
            @RequestParam(required = false) LocalDate periodFrom,
            @RequestParam(required = false) LocalDate periodTo,
            @RequestParam(required = false) String traineeName);

    /**
     * Activates or deactivates a trainer profile.
     *
     * <p>This endpoint is accessible only by ADMIN roles. It allows activation or deactivation of the trainer
     * profile identified by the specified username based on the provided status.</p>
     *
     * @param username The username of the trainer whose profile is to be activated or deactivated.
     * @param active   A boolean indicating whether to activate (true) or deactivate (false) the trainer profile.
     * @return A {@link ResponseEntity} with no content if the operation was successful.
     */
    @Operation(
            summary = "Activate/Deactivate Trainer",
            description = "Activate or deactivate a trainer profile. Accessible only by ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile activation successful"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access"),
                    @ApiResponse(responseCode = "404", description = "Trainer not found")
            }
    )
    ResponseEntity<MessageResponse> updateStatusTrainer(
            @PathVariable String username, @RequestParam Boolean active);

    /**
     * Retrieves a list of active trainers who are not assigned to any trainees.
     *
     * <p>This endpoint is accessible only by ADMIN roles. It returns a list of active trainers who are not
     * currently assigned to any trainees.</p>
     *
     * @param username The username of the requesting user, typically used for authentication/authorization checks.
     * @return A {@link ResponseEntity} containing a list of {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile}.
     */
    @Operation(
            summary = "Get Not Assigned Active Trainers",
            description = "Retrieve a list of active trainers who are not assigned to any trainees.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Unassigned active trainers get successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<List<TrainerProfile>> getNotAssignedActiveTrainers(@PathVariable String username);
}
