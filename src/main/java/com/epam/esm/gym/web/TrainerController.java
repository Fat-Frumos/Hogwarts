package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeTrainingRequest;
import com.epam.esm.gym.dto.trainer.TrainerDto;
import com.epam.esm.gym.dto.trainer.TrainerRegistrationDto;
import com.epam.esm.gym.dto.trainer.TrainerResponse;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.service.TraineeService;
import com.epam.esm.gym.service.TrainerService;
import com.epam.esm.gym.service.TrainingService;
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

    private final TraineeService traineeService;

    private final TrainerService trainerService;

    private final TrainingService trainingService;


    @PostMapping("/")
    public ResponseEntity<ProfileResponse> registerTrainer(
            @RequestBody TrainerRegistrationDto request) {
        ProfileResponse response = trainerService.registerTrainer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login(
            @RequestParam ProfileRequest request) {
        traineeService.validateUser(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-login")
    public ResponseEntity<Void> changeLogin(
            @RequestParam ProfileRequest request) {
        traineeService.changePassword(request);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{username}")
    public ResponseEntity<TrainerResponse> getTrainerProfile(
            @PathVariable String username) {
        TrainerResponse response = trainerService.getTrainer(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TrainerDto> updateTrainerProfile(
            @PathVariable String username,
            @RequestBody TrainerUpdateRequest request) {
        TrainerDto response = trainerService.updateTrainer(username, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/not-assigned/{traineeUsername}")
    public ResponseEntity<List<TrainerResponse>> getNotAssignedTrainers(
            @PathVariable String traineeUsername) {
        List<TrainerResponse> response = trainerService.getNotAssigned(traineeUsername);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingResponse>> getTrainerTrainings(
            @PathVariable String username,
            @RequestBody TraineeTrainingRequest request) {
        List<TrainingResponse> response = trainingService.getTrainerTrainingsByName(username, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{username}/activate")
    public ResponseEntity<Void> activateTrainer(
            @PathVariable String username, @RequestParam Boolean isActive) {
        trainerService.activateDeactivateProfile(username, isActive);
        return ResponseEntity.ok().build();
    }
}
