package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.service.TrainerService;
import com.epam.esm.gym.service.TrainingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@AllArgsConstructor
public class TrainerController implements ITrainerController {

    private final TrainerService service;

    private final TrainingService trainingService;

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TRAINER')")
    public ResponseEntity<List<TrainerProfile>> getAllTrainers() {
        return service.findAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ProfileResponse> registerTrainerProfile(
            @Valid @RequestBody TrainerRequest request) {
        return service.registerTrainer(request);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_TRAINER')")
    public ResponseEntity<String> assignTraineeToTrainer(@RequestParam String traineeUsername) {
        service.assignTraineeToTrainer(traineeUsername);
        return ResponseEntity.ok("Trainee assigned successfully");
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteTrainer(@PathVariable String username) {
        service.deleteTrainer(username);
        return ResponseEntity.ok("Trainer deleted successfully");
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TRAINER')")
    public ResponseEntity<TrainerProfile> getTrainerProfile(
            @PathVariable String username) {
        return service.getTrainerProfileByName(username);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TrainerProfile> updateTrainerProfile(
            @PathVariable String username,
            @Valid @RequestBody TrainerUpdateRequest request) {
        return service.updateTrainer(username, request);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<TrainingResponse>> getTrainerTrainings(
            @PathVariable String username,
            @Valid @RequestBody TrainingProfile request) {
        return trainingService.getTrainerTrainingsByName(username, request);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> activateTrainer(
            @PathVariable String username, @RequestParam Boolean active) {
        return service.activateDeactivateProfile(username, active);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<TrainerProfile>> getNotAssignedActiveTrainers(
            @PathVariable String username) {
        return !username.matches("^[a-zA-Z0-9._-]+$")
                ? ResponseEntity.badRequest().build()
                : service.getNotAssigned(username);
    }
}
