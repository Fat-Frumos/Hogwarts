package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;

import java.util.List;

/**
 * Data Access Object (DAO) interface for managing {@link Trainee} entities.
 *
 * <p>This interface extends the generic {@link Dao} interface to provide additional methods specific
 * to {@link Trainee} operations. It includes methods to find trainers who are not yet assigned to a
 * specific trainee.</p>
 */
public interface TraineeDao extends Dao<Trainee> {

    /**
     * Finds trainers who are not assigned to a specific trainee.
     *
     * @param username the username of the trainee
     * @return a list of {@link Trainer} objects not assigned to the trainee
     */
    List<Trainer> findNotAssignedTrainers(String username);
}
