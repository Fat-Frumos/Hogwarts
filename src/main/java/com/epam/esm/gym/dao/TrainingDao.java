package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;

import java.util.List;

public interface TrainingDao extends Dao<Training> {

    List<TrainingType> findAllTrainingTypes();

    List<Training> findTrainingsByTrainerUsername(String username);
}
