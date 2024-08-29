package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.mapper.TraineeMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TraineeProfileService implements TraineeService {

    private final TraineeDao dao;
    private final TraineeMapper mapper;
    private final UserService userService;
    private final TrainerService trainerService;

    @Override
    public ProfileResponse register(TraineeRequest dto) {
        TraineeProfile profile = userService.saveTrainee(dto);
        Trainee trainee = dao.save(mapper.toEntity(profile));
        return mapper.toProfile(trainee.getUser());
    }

    @Override
    public void deleteTrainee(String username) {
        dao.delete(getTrainee(username));
    }

    @Override
    public TraineeProfile getTraineeProfileByName(String username) {
        return mapper.toDto(getTrainee(username));
    }


    @Override
    public TraineeProfile updateTrainee(
            String username, TraineeUpdateRequest request) {
        Trainee trainee = getTrainee(username);
        Trainee updated = dao.update(mapper.update(request, trainee));
        return mapper.toDto(updated);

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
        Trainee trainee = getTrainee(username);
        List<TrainerProfile> trainers = trainersUsernames.stream()
                .map(trainerService::getTrainerProfileByName)
                .toList();
        trainee.setTrainers(mapper.toTrainers(trainers));
        dao.save(trainee);
        return trainers;
    }

    @Override
    public List<TrainingResponse> getTraineeTrainingsByName(
            String username, TrainingProfile request) {
        Trainee trainee = getTrainee(username);
        return trainee.getTrainings().stream()
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
                .map(mapper::toTrainer)
                .toList();
    }

    public Trainee getTrainee(String username) {
        return dao.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Trainee not found: " + username));
    }
}
