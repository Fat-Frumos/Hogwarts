package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.mapper.TrainerMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TraineeProfileService implements TraineeService {

    private final TraineeDao dao;
    private final TrainerMapper mapper;
    private final UserService userService;

    @Override
    public ProfileResponse register(TraineeRequest dto) {
        TraineeProfile profile = userService.saveTrainee(dto);
        Trainee trainee = dao.save(mapper.toEntity(profile));
        return mapper.toProfileDto(trainee.getUser());
    }

    @Override
    public void deleteTrainee(String username) {

    }

    @Override
    public TraineeRequest getTraineeByName(String username) {
        return null;
    }

    @Override
    public TraineeRequest updateTrainee(
            String username, TraineeUpdateRequest request) {
        return null;
    }

    @Override
    public void validateUser(ProfileRequest request) {

    }

    @Override
    public void changePassword(ProfileRequest request) {

    }

    @Override
    public List<TrainerProfile> updateTraineeTrainersByName(String username, List<String> trainersUsernames) {
        Trainee trainee = dao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));
        return null;
    }

    @Override
    public List<TrainingResponse> getTraineeTrainingsByName(
            String username, TrainingProfile request) {
        return null;
    }

    @Override
    public void activateDeactivateProfile(String username, Boolean active) {

    }

    @Override
    public List<TrainerProfile> getNotAssignedTrainers(String username) {
        return null;
    }
}
