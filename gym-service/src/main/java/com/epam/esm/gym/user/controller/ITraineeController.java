package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.trainee.PostTraineeRequest;
import com.epam.esm.gym.user.dto.trainee.PutTraineeRequest;
import com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse;
import com.epam.esm.gym.user.dto.trainer.TrainerResponse;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Controller for managing operations related to trainees.
 * Provides endpoints for retrieving, registering, updating, and deleting trainee profiles.
 * Allows trainers and admins to manage trainee details and their associated trainings.
 * Requires appropriate roles to access specific operations based on permissions.
 * Handles various operations such as updating trainers, retrieving training sessions, and activating profiles.
 * All input data is validated, and access control is enforced using Spring Security.
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0 *
 */
@Tag(name = "Trainee API", description = "Operations related to Trainee profiles and management")
public interface ITraineeController {

    /**
     * Retrieves all trainee profiles.
     * Accessible only to users with 'ROLE_ADMIN' or 'ROLE_TRAINER' authority.
     * Returns a list of TraineeProfile objects representing all trainees in the system.
     * Uses the TraineeService to fetch the list of trainees.
     * Provides a response with HTTP 200 if the operation is successful.
     * In case of an error, appropriate HTTP status codes are returned.
     * Useful for admins and trainers who need to view all trainees.
     */
    @Operation(
            summary = "Get All Trainees",
            description = "Retrieve a list of all trainee profiles. Accessible by ADMIN and TRAINER roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of trainees"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<List<FullTraineeProfileResponse>> getAllTrainees();

    /**
     * Registers a new trainee with the provided details.
     * Accepts a TraineeRequest object containing necessary registration information.
     * Returns a ProfileResponse object containing details of the registered trainee.
     * This operation is open to any authenticated user.
     * Validation is performed on the incoming request body.
     * Returns HTTP 201 for successful registration and HTTP 400 for invalid input.
     * Ensures that new trainees are added to the system correctly.
     */
    @Operation(
            summary = "Register a New Trainee",
            description = "Register a new trainee profile. Accessible by ADMIN role only.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee profile created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation errors"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<ProfileResponse> registerTrainee(
            @Valid @RequestBody PostTraineeRequest request);

    /**
     * Retrieves a specific trainee profile by username.
     * Only accessible by users with 'ROLE_TRAINER' or 'ROLE_ADMIN' authority.
     * Validates the username to ensure it meets required constraints.
     * Uses the TraineeService to fetch the profile based on the provided username.
     * Returns HTTP 200 if the profile is found, otherwise an appropriate error response.
     * Useful for viewing details of a specific trainee.
     * Handles errors if the username does not exist.
     */
    @Operation(
            summary = "Get Trainee Profile",
            description = "Retrieve a specific trainee profile by username. Accessible by TRAINER and ADMIN roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the trainee profile"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<FullTraineeProfileResponse> getTraineeProfile(
            @PathVariable @NotNull @Valid String username);

    /**
     * Updates the profile of a specific trainee.
     * Requires the username and the updated details in the request body.
     * Accessible by users with 'ROLE_TRAINER' or 'ROLE_ADMIN' authority.
     * Returns the updated TraineeProfile object upon successful update.
     * Handles validation of input data to ensure correctness.
     * Returns HTTP 200 for a successful update or appropriate error codes if issues occur.
     */
    @Operation(
            summary = "Update Trainee Profile",
            description = "Update a trainee profile by username. Accessible by TRAINER and ADMIN roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee profile updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation errors"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<FullTraineeProfileResponse> updateTraineeProfile(
            @PathVariable String username,
            @Valid @RequestBody PutTraineeRequest request);

    /**
     * Deletes the profile of a specific trainee. Requires the username of the trainee to be deleted.
     * Only accessible by users with 'ROLE_ADMIN' authority.
     * Returns HTTP 204 for a successful deletion with no content. Handles errors such as non-existent trainees.
     * Ensures that unauthorized users cannot perform deletions.
     * Provides a secure method for removing trainees from the system.
     */
    @DeleteMapping("/{username}")
    @Operation(
            summary = "Delete Trainee Profile",
            description = "Delete a specific trainee profile by username. Accessible by ADMIN role only.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee profile deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<MessageResponse> deleteTraineeProfile(@PathVariable String username);

    /**
     * Updates the list of trainers assigned to a specific trainee.
     * Requires the trainee's username and a list of trainers' usernames.
     * Accessible by users with 'ROLE_TRAINER' or 'ROLE_ADMIN' authority.
     * Returns a list of TrainerProfile objects representing the updated trainers.
     * Validates input data to ensure trainers are correctly assigned.
     * Returns HTTP 200 for a successful update or appropriate error codes if issues occur.
     */
    @Operation(
            summary = "Update Trainee Trainers",
            description = "Update the list of trainers assigned to a trainee by username.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee trainers updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation errors"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<List<TrainerResponse>> updateTraineeTrainers(
            @PathVariable String username,
            @Valid @RequestBody List<String> trainersUsernames);

    /**
     * Retrieves all training sessions associated with a specific trainee.
     * Requires the trainee's username and optional query parameters for filtering.
     * Accessible by users with 'ROLE_TRAINER' or 'ROLE_ADMIN' authority.
     * Returns a list of TrainingResponse objects representing the trainee's trainings.
     * Handles query parameters to filter or sort the training sessions.
     * Returns HTTP 200 for a successful retrieval or appropriate error codes if issues occur.
     */
    @Operation(
            summary = "Get Trainee Trainings",
            description = "Retrieve the list of trainings for a trainee. Accessible by TRAINER and ADMIN roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of trainings"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<List<TrainingResponse>> getTraineeTrainings(
            @PathVariable String username,
            @RequestParam Map<String, String> params);

    /**
     * Activates or deactivates a trainee profile.
     * Requires the trainee's username and a Boolean flag indicating active status.
     * Only accessible by users with 'ROLE_ADMIN' authority.
     * Returns HTTP 204 for a successful operation with no content.
     * Ensures proper activation or deactivation of trainee profiles based on the provided flag.
     * Handles cases where the username does not exist or errors occur during the process.
     */
    @Operation(
            summary = "Activate/Deactivate Trainee",
            description = "Activate or deactivate a trainee profile by username. Accessible by ADMIN role only.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile activation status updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<MessageResponse> activateDeactivateTrainee(
            @PathVariable String username, @RequestParam Boolean active);
}
