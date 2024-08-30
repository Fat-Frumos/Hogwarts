package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
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

import java.util.List;
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
        Optional<Trainee> trainee = dao.findByUsername(username);
        return trainee.map(value -> ResponseEntity.ok(mapper.toTraineeProfile(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public TraineeProfile updateTrainee(
            String username, TraineeRequest request) {
        Trainee trainee = getTrainee(username);
        Trainee updated = dao.update(mapper.update(request, trainee));
        return mapper.toTraineeProfile(updated);
    }

    @Override
    public boolean validateUser(ProfileRequest request) {
        UserProfile user = userService.getUserByUsername(request.getUsername());
        return user.getPassword().equals(request.getPassword()) || user.getActive();
    }

    @Override
    public void changePassword(ProfileRequest request) {
        userService.changePassword(request);
    }

    @Override
    public List<TrainerProfile> updateTraineeTrainersByName(String username, List<String> trainersUsernames) {
        List<TrainerProfile> trainerProfiles = trainersUsernames.stream()
                .map(trainerService::getTrainer)
                .map(mapper::toTrainerProfile)
                .collect(Collectors.toList());
        Trainee trainee = getTrainee(username);
        trainee.setTrainers(mapper.toTrainers(trainerProfiles));
        dao.save(trainee);
        return trainerProfiles;
    }


    @Override
    public List<TrainingResponse> getTraineeTrainingsByName(
            String username, TrainingProfile request) {
        return getTrainee(username).getTrainings().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public void activateDeactivateProfile(String username, Boolean active) {
        Trainee trainee = getTrainee(username);
        trainee.getUser().setActive(active);
        dao.save(trainee);
    }

    @Override
    public List<TrainerProfile> getNotAssignedTrainers(String username) {
        List<Trainer> trainers = dao.findNotAssignedTrainers(username);
        return trainers.stream()
                .map(mapper::toTrainerProfile)
                .toList();
    }

    public Trainee getTrainee(String username) {
        return dao.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Trainee not found: " + username));
    }
}
