package com.epam.esm.gym.user.dao;

import com.epam.esm.gym.user.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Trainer} entities.
 * This interface provides methods to perform CRUD operations and custom queries on Trainer data.
 */
@Repository
public interface JpaTrainerDao extends JpaRepository<Trainer, Long> {

    /**
     * Finds a trainer by their username.
     *
     * @param username the username of the trainer to be found
     * @return an Optional containing the Trainer if found, or empty if not
     */
    @Query("SELECT t FROM Trainer t LEFT JOIN FETCH t.trainees WHERE t.user.username = :username")
    Optional<Trainer> findByUsername(@Param("username") String username);

    /**
     * Retrieves a list of trainers that are not assigned to the specified username and have no associated trainings.
     *
     * @param username the username to filter out assigned trainers
     * @return a list of trainers that are not assigned and have an empty training set
     */
    @Query("SELECT t FROM Trainer t JOIN User u ON t.id = u.id " +
            "WHERE u.username <> :username AND t.trainings IS EMPTY")
    List<Trainer> findNotAssignedTrainers(@Param("username") String username);

    /**
     * Activates or deactivates a trainer based on their username.
     *
     * @param username the username of the trainer to be activated or deactivated
     * @param active   the active status to be set for the trainer
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.active = :active WHERE u.id = " +
            "(SELECT t.user.id FROM Trainer t WHERE t.user.username = :username)")
    void activateTrainer(@Param("username") String username, @Param("active") Boolean active);

    /**
     * Assigns a trainee to a trainer based on their usernames.
     *
     * @param trainerUsername the username of the trainer
     * @param traineeUsername the username of the trainee to be assigned
     */
    @Modifying
    @Transactional
    @Query("UPDATE Trainee t SET t.trainers = " +
            "(SELECT tr FROM Trainer tr WHERE tr.user.username = :trainerUsername) " +
            "WHERE t.user.username = :traineeUsername")
    void assignTraineeToTrainer(
            @Param("trainerUsername") String trainerUsername,
            @Param("traineeUsername") String traineeUsername);
    /**
     * Fetches all Trainers along with their associated Trainings, Trainees, and Training Sessions.
     *
     * @return a list of Trainers with their related entities.
     */
    @Query("SELECT t FROM Trainer t " +
            "LEFT JOIN FETCH t.user u " +
            "LEFT JOIN FETCH t.trainingType tt " +
            "LEFT JOIN FETCH t.trainingSessions ts " +
            "WHERE u.permission = 'ROLE_TRAINER'")
    List<Trainer> findAllWithUsers();
}