package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@RequestMapping("/api/trainers")
@Tag(name = "Trainer API", description = "Operations related to Trainer profiles and assignments")
public interface ITrainerController {

    @GetMapping
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

    @PostMapping("/register")
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


    @PostMapping("/assign")
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


    @DeleteMapping("/{username}")
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
    ResponseEntity<String> deleteTrainer(@PathVariable String username);

    @GetMapping("/{username}")
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

    @PutMapping("/{username}")
    @Operation(
            summary = "Update Trainer Profile",
            description = "Update an existing trainer profile by username. Accessible by ADMIN or the trainer themselves.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access"),
                    @ApiResponse(responseCode = "404", description = "Trainer not found"),
                    @ApiResponse(responseCode = "405", description = "Method not allowed")
            }
    )
    ResponseEntity<TrainerProfile> updateTrainerProfile(@PathVariable String username, @Valid @RequestBody TrainerUpdateRequest request);

    @GetMapping("/{username}/trainings")
    @Operation(
            summary = "Get Trainer Trainings List",
            description = "Retrieve a list of trainings associated with a trainer by username. Accessible by users with role ADMIN or TRAINER.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of trainer's trainings retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access"),
                    @ApiResponse(responseCode = "404", description = "Trainer or training not found")
            }
    )
    ResponseEntity<List<TrainingResponse>> getTrainerTrainings(@PathVariable String username, @Valid @RequestBody TrainingProfile request);

    @PatchMapping("/{username}/activate")
    @Operation(
            summary = "Activate/Deactivate Trainer",
            description = "Activate or deactivate a trainer profile. Accessible only by ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile activation/deactivation successful"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access"),
                    @ApiResponse(responseCode = "404", description = "Trainer not found")
            }
    )
    ResponseEntity<Void> activateTrainer(
            @PathVariable String username, @RequestParam Boolean active);

    @GetMapping("/{username}/unassigned")
    @Operation(
            summary = "Get Not Assigned Active Trainers",
            description = "Retrieve a list of active trainers who are not assigned to any trainees. Accessible only by ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of unassigned active trainers retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request due to validation error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<List<TrainerProfile>> getNotAssignedActiveTrainers(@PathVariable String username);
}
