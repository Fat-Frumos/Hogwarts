package com.epam.esm.gym.user.dao;

import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link com.epam.esm.gym.user.entity.Trainer} entities.
 * This interface provides methods to perform CRUD operations and custom queries on Trainer data.
 */
@Repository
public interface JpaTrainingTypeDao extends JpaRepository<TrainingType, Long> {

    /**
     * Finds a training type by its specialization.
     *
     * @param specialization the specialization associated with the training type
     * @return an {@link Optional} containing the training type if found, otherwise empty
     */
    Optional<TrainingType> findBySpecialization(Specialization specialization);

    /**
     * Finds all training types associated with a given specialization.
     *
     * @param specialization the specialization to filter training types
     * @return a list of training types that match the specified specialization
     */
    List<TrainingType> findAllBySpecialization(Specialization specialization);
}
