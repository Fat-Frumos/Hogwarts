package com.epam.esm.gym.user.dao;

import com.epam.esm.gym.user.entity.RoleType;
import com.epam.esm.gym.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods for accessing user data, including finding users by username,
 * checking the existence of usernames, and retrieving users with specific permissions.
 */
@Repository
public interface JpaUserDao extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return an Optional containing the found user, or empty if no user is found
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds users by their username.
     *
     * @param baseUsernamePattern the username to search for
     * @return a list of users matching the given username
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE CONCAT(:baseUsernamePattern, '%')")
    List<User> findUsersByUsername(@Param("baseUsernamePattern") String baseUsernamePattern);

    /**
     * Retrieves all users with the specified role and whose usernames are in the provided list.
     *
     * @param roleTrainee the role to filter users by
     * @param usernames   a list of usernames to filter the users
     * @return a list of users matching the specified role and usernames
     */
    @Query("FROM User u WHERE u.permission = :roleTrainee AND u.username IN :usernames")
    List<User> findAllTraineesByUsername(
            @Param("roleTrainee") RoleType roleTrainee,
            @Param("usernames") List<String> usernames);

    /**
     * Checks if a user with the specified username exists.
     *
     * @param username the username to check
     * @return true if a user with the given username exists, false otherwise
     */
    boolean existsByUsername(String username);
}
