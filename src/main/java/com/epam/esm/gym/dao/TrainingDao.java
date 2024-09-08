package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;

import java.util.List;

/**
 * Data Access Object (DAO) interface for managing {@link Training} entities.
 *
 * <p>This interface extends the generic {@link Dao} interface to provide additional methods specific
 * to {@link Training} operations. It includes methods to find all training types, find trainings by a
 * trainer's username, and find training sessions within a specific time range.</p>
 */
public interface TrainingDao extends Dao<Training> {

    /**
     * Finds all available training types.
     *
     * @return a list of {@link TrainingType} objects
     */
    List<TrainingType> findAllTrainingTypes();

    /**
     * Finds all trainings associated with a specific trainer's username.
     *
     * @param username the username of the trainer
     * @return a list of {@link Training} objects
     */
    List<Training> findTrainingsByTrainerUsername(String username);
}
