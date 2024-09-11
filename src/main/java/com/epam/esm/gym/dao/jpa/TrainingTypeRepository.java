package com.epam.esm.gym.dao.jpa;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link TrainingType} entities.
 * This interface extends {@link JpaRepository} and provides CRUD operations for {@link TrainingType} entities.
 * It also includes a custom query method to find a {@link TrainingType} by its {@link Specialization}.
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {

    /**
     * Finds a {@link TrainingType} entity by its {@link Specialization}.
     *
     * @param specialization the specialization of the training type
     * @return an {@link List} containing the {@link TrainingType} if found, or empty if not
     */
    List<TrainingType> findAllBySpecialization(Specialization specialization);

    /**
     * Finds a {@link TrainingType} by its specialization.
     * This method queries the repository to retrieve a {@link TrainingType} entity based on the provided
     * {@link Specialization} value. The returned value is an instance of {@link TrainingType} that matches
     * the given specialization.
     *
     * @param specialization the specialization of the training type to find.
     * @return the {@link TrainingType} associated with the given specialization, or {@code null} if no
     * matching training type is found.
     */
    TrainingType findBySpecialization(Specialization specialization);
}