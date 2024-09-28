package com.epam.esm.gym.workload.dao.jpa;

import com.epam.esm.gym.workload.entity.TrainerWorkload;
import com.epam.esm.gym.workload.entity.TrainingWorkload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for accessing TrainingWorkload entities.
 */
@Repository
public interface TrainingWorkloadRepository extends JpaRepository<TrainingWorkload, Long> {

    /**
     * Finds all TrainingWorkload entries by workload (trainer).
     *
     * @param workload the associated TrainerWorkload
     * @return a list of TrainingWorkload entities
     */
    List<TrainingWorkload> findByWorkload(TrainerWorkload workload);

    /**
     * Finds TrainingWorkload entries by training name.
     *
     * @param trainingName the name of the training
     * @return a list of TrainingWorkload entities
     */
    List<TrainingWorkload> findByTrainingName(String trainingName);

    /**
     * Finds TrainingWorkload entries by training date.
     *
     * @param trainingDate the date of the training
     * @return a list of TrainingWorkload entities
     */
    List<TrainingWorkload> findByTrainingDate(LocalDate trainingDate);

    /**
     * Finds TrainingWorkload entries by username and training date range.
     *
     * @param username  the username of the trainer
     * @param startDate the start date of the training
     * @param endDate   the end date of the training
     * @return a list of TrainingWorkload entities
     */
    List<TrainingWorkload> findByWorkload_TrainerNameAndTrainingDateBetween(
            @Param("username") String username,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
