package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeRegistrationRequestDto;
import com.epam.esm.gym.dto.trainee.TraineeTrainingRequest;
import com.epam.esm.gym.dto.trainee.TraineeUpdateRequestDto;
import com.epam.esm.gym.dto.trainer.TrainerResponse;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.service.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/trainees")
@Tag(name = "Trainee", description = "Trainee related operations")
public class TraineeController {

    private TraineeService traineeService;

    @PostMapping("/")
    @Operation(summary = "Register a new trainee")
    public ResponseEntity<ProfileResponse> registerTrainee(
            @RequestBody TraineeRegistrationRequestDto request) {
        ProfileResponse response = traineeService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeRegistrationRequestDto> getTraineeProfile(
            @PathVariable String username) {
        TraineeRegistrationRequestDto response = traineeService.getTraineeByName(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeRegistrationRequestDto> updateTraineeProfile(
            @PathVariable String username,
            @RequestBody TraineeUpdateRequestDto request) {
        TraineeRegistrationRequestDto response = traineeService.updateTrainee(username, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable String username) {
        traineeService.deleteTrainee(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerResponse>> updateTraineeTrainers(
            @PathVariable String username,
            @RequestBody List<String> trainerUsernames) {
        List<TrainerResponse> response = traineeService.updateTraineeTrainersByName(username, trainerUsernames);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingResponse>> getTraineeTrainings(
            @PathVariable String username,
            @RequestBody TraineeTrainingRequest request) {
        List<TrainingResponse> response = traineeService.getTraineeTrainingsByName(username, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{username}/activate")
    public ResponseEntity<Void> activateTrainee(
            @PathVariable String username, @RequestParam Boolean isActive) {
        traineeService.activateDeactivateProfile(username, isActive);
        return ResponseEntity.ok().build();
    }
}
