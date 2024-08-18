package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import com.epam.esm.gym.mapper.TrainerMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrainingProfileService implements TrainingService {

    private final TrainingDao dao;
    private final TrainerMapper mapper;

    @Override
    public List<TrainingTypeResponse> getTrainingTypes() {
        return null;
    }

    @Override
    public List<TrainingResponse> getTrainerTrainingsByName(String username, TrainingProfile request) {
        return null;
    }

    @Override
    public void createTraining(TrainingRequest request) {
    }
}
