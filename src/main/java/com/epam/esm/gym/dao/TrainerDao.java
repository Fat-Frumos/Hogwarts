package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.Trainer;

import java.util.List;

/**
 * Data Access Object (DAO) interface for managing {@link Trainer} entities.
 *
 * <p>This interface extends the generic {@link Dao} interface to provide additional methods specific
 * to {@link Trainer} operations. It includes methods to activate a trainer, find trainers not assigned
 * to a specific trainee, and assign a trainee to a trainer.</p>
 */
public interface TrainerDao extends Dao<Trainer> {

    /**
     * Activates or deactivates a trainer based on the provided status.
     *
     * @param name   the username of the trainer
     * @param active the activation status
     */
    void activateTrainer(String name, Boolean active);

    /**
     * Finds trainers who are not assigned to a specific trainee.
     *
     * @param username the username of the trainee
     * @return a list of {@link Trainer} objects not assigned to the trainee
     */
    List<Trainer> findNotAssigned(String username);

    /**
     * Assigns a trainee to a trainer.
     *
     * @param trainerUsername the username of the trainer
     * @param traineeUsername the username of the trainee
     */
    void assignTraineeToTrainer(String trainerUsername, String traineeUsername);
}
