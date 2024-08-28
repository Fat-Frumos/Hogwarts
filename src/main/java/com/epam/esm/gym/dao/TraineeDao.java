package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;

import java.util.List;

public interface TraineeDao extends Dao<Trainee> {

    List<Trainer> findNotAssignedTrainers(String username);
}
