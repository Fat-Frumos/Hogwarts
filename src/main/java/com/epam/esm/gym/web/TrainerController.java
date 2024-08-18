package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.service.TrainerService;
import com.epam.esm.gym.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    private final TrainingService trainingService;

    @PostMapping("/register")
    @Operation(summary = "2. Register a new trainer")
    public ResponseEntity<ProfileResponse> registerTrainerProfile(
            @Valid @RequestBody TrainerRequest request) {
        ProfileResponse response = trainerService.registerTrainer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{username}")
    @Operation(summary = "8. Get Trainer Profile")
    public ResponseEntity<TrainerProfile> getTrainerProfile(
            @PathVariable String username) {
        TrainerProfile profile = trainerService.getTrainer(username);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{username}")
    @Operation(summary = "9. Update Trainer Profile")
    public ResponseEntity<TrainerProfile> updateTrainerProfile(
            @PathVariable String username,
            @Valid @RequestBody TrainerUpdateRequest request) {
        TrainerProfile profile = trainerService.updateTrainer(username, request);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "13. Get Trainer Trainings List")
    public ResponseEntity<List<TrainingResponse>> getTrainerTrainings(
            @PathVariable String username,
            @Valid @RequestBody TrainingProfile request) {
        List<TrainingResponse> response = trainingService.getTrainerTrainingsByName(username, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{username}/activate")
    @Operation(summary = "16. Activate/Deactivate Trainer")
    public ResponseEntity<Void> activateTrainer(
            @PathVariable String username, @RequestParam Boolean isActive) {
        trainerService.activateDeactivateProfile(username, isActive);
        return ResponseEntity.ok().build();
    }
}
