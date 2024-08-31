package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.mapper.TraineeMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TraineeProfileService implements TraineeService {

    private final TraineeDao dao;
    private final TraineeMapper mapper;
    private final UserService userService;
    private final TrainerService trainerService;

    @Override
    public ResponseEntity<List<TraineeProfile>> findAll() {
        List<TraineeProfile> trainees = dao.findAll()
                .stream()
                .map(mapper::toTraineeProfile)
                .toList();
        return ResponseEntity.ok(trainees);
    }

    @Override
    public ResponseEntity<ProfileResponse> register(TraineeRequest dto) {
        TraineeProfile profile = userService.saveTrainee(dto);
        Trainee trainee = dao.save(mapper.toEntity(profile));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toProfile(trainee.getUser()));
    }

    @Override
    public ResponseEntity<Void> deleteTrainee(String username) {
        Optional<Trainee> traineeOptional = dao.findByUsername(username);
        if (traineeOptional.isPresent()) {
            dao.delete(traineeOptional.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<TraineeProfile> getTraineeProfileByName(String username) {
        return dao.findByUsername(username)
                .map(trainee -> ResponseEntity.ok(mapper.toTraineeProfile(trainee)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<TraineeProfile> updateTrainee(
            String username, TraineeRequest request) {
        return dao.findByUsername(username)
                .map(trainee -> ResponseEntity.ok(mapper.toTraineeProfile(dao.update(mapper.update(request, trainee)))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public boolean validateUser(ProfileRequest request) {
        UserProfile user = userService.getUserByUsername(request.getUsername());
        return request.getPassword().equals(user.getPassword()) || user.getActive();
    }

    @Override
    public void changePassword(ProfileRequest request) {
        userService.changePassword(request);
    }

    @Override
    public ResponseEntity<List<TrainerProfile>> updateTraineeTrainersByName(String username, List<String> trainersUsernames) {
        List<TrainerProfile> trainerProfiles = trainersUsernames.stream()
                .map(trainerService::getTrainer)
                .map(mapper::toTrainerProfile)
                .collect(Collectors.toList());
        Trainee trainee = getTrainee(username);
        trainee.setTrainers(mapper.toTrainers(trainerProfiles));
        dao.save(trainee);
        return ResponseEntity.ok(trainerProfiles);
    }

    @Override
    public ResponseEntity<List<TrainingResponse>> getTraineeTrainingsByName(
            String username, Map<String, String> params) {
        TrainingProfile filter = TrainingProfile.builder()
                .periodFrom(params.containsKey("periodFrom") ? LocalDate.parse(params.get("periodFrom")) : null)
                .periodTo(params.containsKey("periodTo") ? LocalDate.parse(params.get("periodTo")) : null)
                .trainerName(params.get("trainerName"))
                .trainingType(params.get("trainingType"))
                .build();

        return dao.findByUsername(username)
                .map(trainee -> ResponseEntity.ok(trainee.getTrainings()
                        .stream()
                        .filter(training -> matches(training, filter))
                        .map(mapper::toResponse).toList()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private boolean matches(Training training, TrainingProfile filter) {
        if (filter.getPeriodFrom() != null && training.getTrainingDate().isBefore(filter.getPeriodFrom())) {
            return false;
        }
        if (filter.getPeriodTo() != null && training.getTrainingDate().isAfter(filter.getPeriodTo())) {
            return false;
        }
        if (filter.getTrainerName() != null && !training.getTrainer().getUser().getUsername().equalsIgnoreCase(filter.getTrainerName())) {
            return false;
        }
        return filter.getTrainingType() == null || training.getType().getTrainingType().name().equalsIgnoreCase(filter.getTrainingType());
    }

    @Override
    public ResponseEntity<Void> activateDeactivateProfile(String username, Boolean active) {
        Trainee trainee = getTrainee(username);
        trainee.getUser().setActive(active);
        dao.save(trainee);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<TrainerProfile>> getNotAssignedTrainers(String username) {
        List<Trainer> trainers = dao.findNotAssignedTrainers(username);
        return ResponseEntity.ok(trainers.stream()
                .map(mapper::toTrainerProfile)
                .toList());
    }

    public Trainee getTrainee(String username) {
        return dao.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Trainee not found: " + username));
    }
}
