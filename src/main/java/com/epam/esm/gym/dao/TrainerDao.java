package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.Trainer;

import java.util.List;

public interface TrainerDao extends Dao<Trainer> {

    void activateTrainer(String name, Boolean active);

    List<Trainer> findNotAssigned(String username);
}
