package com.epam.esm.gym.user.dao;

import com.epam.esm.gym.user.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link com.epam.esm.gym.user.entity.Training} entities.
 * This interface provides methods to perform CRUD operations and custom queries on Trainer data.
 */
@Repository
public interface JpaTrainingDao extends JpaRepository<Training, Long> {

    /**
     * Finds a list of trainings based on the trainer's username, trainee's username,
     * training type, and the training date range.
     *
     * @param username   the username of the trainer
     * @param periodFrom the start date of the training period
     * @param periodTo   the end date of the training period
     * @return a list of trainings that match the specified criteria
     */
    @Query("SELECT t FROM Training t JOIN t.trainer tr JOIN tr.user u WHERE u.username = :username " +
            "AND (:periodFrom IS NULL OR t.trainingDate >= :periodFrom) " +
            "AND (:periodTo IS NULL OR t.trainingDate <= :periodTo)")
    List<Training> findTrainingsByTrainerName(
            @Param("username") String username,
            @Param("periodFrom") LocalDate periodFrom,
            @Param("periodTo") LocalDate periodTo);

    /**
     * Finds a training by its name.
     *
     * @param trainingName the name of the training
     * @return an {@link Optional} containing the training if found, otherwise empty
     */
    Optional<Training> findByTrainingName(String trainingName);

    /**
     * Finds a training by its name.
     *
     * @return an {@link Optional} containing the training if found, otherwise empty
     */
    @Query("SELECT t FROM Training t " +
            "WHERE t.trainer.id = (SELECT tr.id FROM Trainer tr WHERE tr.user.username = :username) " +
            "AND t.trainingDate = :trainingDate")
    List<Training> findByTrainerAndDate(
            @Param("username") String username,
            @Param("trainingDate") LocalDate trainingDate);

}
