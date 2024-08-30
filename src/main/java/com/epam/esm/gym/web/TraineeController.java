package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.service.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/trainees")
@Tag(name = "Trainee", description = "Trainee related operations")
public class TraineeController {

    private TraineeService service;

    @PostMapping("/register")
    @Operation(summary = "1. Register a new trainee")
    public ResponseEntity<ProfileResponse> registerTrainee(
            @Valid @RequestBody TraineeRequest request) {
        return service.register(request);
    }

    @GetMapping("/{username}")
    @Operation(summary = "5. Get Trainee Profile by username")
    public ResponseEntity<TraineeProfile> getTraineeProfile(
            @PathVariable @NotNull @Valid String username) {
        return service.getTraineeProfileByName(username);
    }

    @PutMapping("/{username}")
    @Operation(summary = "6. Update Trainee Profile")
    public ResponseEntity<TraineeProfile> updateTraineeProfile(
            @PathVariable String username,
            @Valid @RequestBody TraineeRequest request) {
        return ResponseEntity.ok(service.updateTrainee(username, request));
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "7. Delete Trainee Profile")
    public ResponseEntity<Void> deleteTraineeProfile(
            @PathVariable String username) {
        return service.deleteTrainee(username);
    }

    @PutMapping("/{username}/trainers")
    @Operation(summary = "11. Update Trainee's Trainer List")
    public ResponseEntity<List<TrainerProfile>> updateTraineeTrainers(
            @PathVariable String username,
            @Valid @RequestBody List<String> trainersUsernames) {
        return ResponseEntity.ok(service.updateTraineeTrainersByName(username, trainersUsernames));
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "12. Get Trainee Trainings List")
    public ResponseEntity<List<TrainingResponse>> getTraineeTrainings(
            @PathVariable String username,
            @RequestBody TrainingProfile request) {
        return ResponseEntity.ok(service.getTraineeTrainingsByName(username, request));
    }

    @PatchMapping("/{username}/activate")
    @Operation(summary = "15. Activate/Deactivate Trainee")
    public ResponseEntity<Void> activateDeactivateTrainee(
            @PathVariable String username, @RequestParam Boolean active) {
        service.activateDeactivateProfile(username, active);
        return ResponseEntity.ok().build();
    }
}
