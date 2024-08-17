package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.Trainer;

public interface TrainerDao extends Dao<Trainer, Long> {
    void activateTrainer(Long id);

    Trainer updateTrainer(Trainer trainer);
}
