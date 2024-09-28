package com.epam.esm.gym.user.dao;

import com.epam.esm.gym.user.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link com.epam.esm.gym.user.entity.Trainer} entities.
 * This interface provides methods to perform CRUD operations and custom queries on Trainer data.
 */
@Repository
public interface JpaTraineeDao extends JpaRepository<Trainee, Long> {
    /**
     * Finds a trainee by the username of the associated user.
     *
     * @param username the username of the user to search for
     * @return an {@link Optional} containing the trainee if found, otherwise empty
     */
    @Query("SELECT t FROM Trainee t WHERE t.user.username = :username")
    Optional<Trainee> findByUsername(@Param("username") String username);

    /**
     * Retrieves a list of trainees associated with the specified usernames.
     *
     * @param usernames a list of usernames to search for
     * @return a list of trainees associated with the specified usernames
     */
    @Query("SELECT t FROM Trainee t JOIN User u ON t.user.id = u.id WHERE u.username IN :usernames")
    List<Trainee> findAllByUsernames(@Param("usernames") List<String> usernames);
}
