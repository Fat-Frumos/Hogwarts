package com.epam.esm.gym.workload.dao.jpa;

import com.epam.esm.gym.workload.entity.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link com.epam.esm.gym.workload.entity.TrainerWorkload} entities.
 * This interface extends {@link JpaRepository}, providing methods for CRUD operations and custom queries
 * on the {@code trainer_workload} table.
 */
@Repository
public interface TrainerWorkloadRepository extends JpaRepository<TrainerWorkload, Long> {

    /**
     * Finds all TrainerWorkload entries by trainer name and active status.
     *
     * @param trainerName the name of the trainer
     * @param active the active status of the trainer
     * @return a list of TrainerWorkload entities
     */
    List<TrainerWorkload> findByTrainerNameAndActive(String trainerName, boolean active);
}
