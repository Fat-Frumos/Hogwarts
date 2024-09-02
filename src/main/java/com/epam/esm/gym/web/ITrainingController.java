package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingTypeResponse;
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

@RequestMapping("/api/trainings")
@Tag(name = "Training API", description = "Operations related to Trainings and Training Types")
public interface ITrainingController {
    @GetMapping("/types")
    @Operation(
            summary = "Get Training Types",
            description = "Retrieve a list of all available training types. Accessible by TRAINER and ADMIN roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of training types"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes();

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
