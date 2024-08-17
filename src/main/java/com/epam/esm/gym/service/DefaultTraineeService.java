package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DefaultTraineeService implements TraineeService {

    private final TraineeDao dao;

    public DefaultTraineeService(TraineeDao dao) {
        this.dao = dao;
    }

    @Override
    public ProfileResponse register(
            TraineeRequest dto) {
        return null;
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
    public List<TrainerProfile> updateTraineeTrainersByName(String username, List<String> trainerUsernames) {
        return null;
    }

    @Override
    public List<TrainingResponse> getTraineeTrainingsByName(String username, TrainingProfile request) {
        return null;
    }

    @Override
    public void activateDeactivateProfile(String username, Boolean isActive) {

    }

    @Override
    public List<TrainerProfile> getNotAssignedTrainers(String username) {
        return null;
    }
}
