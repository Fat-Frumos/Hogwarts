package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/trainees")
@Tag(name = "Trainee API", description = "Operations related to Trainee profiles and management")
public interface ITraineeController {

    @GetMapping
    @Operation(
            summary = "Get All Trainees",
            description = "Retrieve a list of all trainee profiles. Accessible by ADMIN and TRAINER roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of trainees"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<List<TraineeProfile>> getAllTrainees();

    @PostMapping("/register")
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
            @Valid @RequestBody TraineeRequest request);

    @GetMapping("/{username}")
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
    ResponseEntity<TraineeProfile> getTraineeProfile(
            @PathVariable @NotNull @Valid String username);

    @PutMapping("/{username}")
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
    ResponseEntity<TraineeProfile> updateTraineeProfile(
            @PathVariable String username,
            @Valid @RequestBody TraineeRequest request);

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
    ResponseEntity<Void> deleteTraineeProfile(
            @PathVariable String username);

    @PutMapping("/{username}/trainers")
    @Operation(
            summary = "Update Trainee Trainers",
            description = "Update the list of trainers assigned to a trainee by username. Accessible by TRAINER and ADMIN roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee trainers updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation errors"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<List<TrainerProfile>> updateTraineeTrainers(
            @PathVariable String username,
            @Valid @RequestBody List<String> trainersUsernames);

    @GetMapping("/{username}/trainings")
    @Operation(
            summary = "Get Trainee Trainings",
            description = "Retrieve the list of trainings for a specific trainee. Accessible by TRAINER and ADMIN roles.",
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

    @PatchMapping("/{username}/activate")
    @Operation(
            summary = "Activate/Deactivate Trainee",
            description = "Activate or deactivate a trainee profile by username. Accessible by ADMIN role only.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee profile activation status updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<Void> activateDeactivateTrainee(
            @PathVariable String username, @RequestParam Boolean active);
}
