package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeRegistrationRequestDto;
import com.epam.esm.gym.dto.trainee.TraineeUpdateRequestDto;
import com.epam.esm.gym.dto.trainer.TrainerResponse;
import com.epam.esm.gym.dto.trainee.TraineeTrainingRequest;
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
            TraineeRegistrationRequestDto dto) {
        return null;
    }

    @Override
    public void deleteTrainee(String username) {

    }

    @Override
    public TraineeRegistrationRequestDto getTraineeByName(String username) {
        return null;
    }

    @Override
    public TraineeRegistrationRequestDto updateTrainee(
            String username, TraineeUpdateRequestDto request) {
        return null;
    }

    @Override
    public void validateUser(ProfileRequest request) {

    }

    @Override
    public void changePassword(ProfileRequest request) {

    }

    @Override
    public List<TrainerResponse> updateTraineeTrainersByName(String username, List<String> trainerUsernames) {
        return null;
    }

    @Override
    public List<TrainingResponse> getTraineeTrainingsByName(String username, TraineeTrainingRequest request) {
        return null;
    }

    @Override
    public void activateDeactivateProfile(String username, Boolean isActive) {

    }
}
