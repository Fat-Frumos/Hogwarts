package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.User;

import java.util.List;

/**
 * Data Access Object (DAO) interface for managing {@link User} entities.
 *
 * <p>This interface extends the generic {@link Dao} interface to provide additional methods specific
 * to {@link User} operations. It includes methods to check if a user exists by username, retrieve a
 * user by username, and get a user by its username.</p>
 */
public interface UserDao extends Dao<User> {

    /**
     * Checks if a user exists with the specified username.
     *
     * @param username the username to check
     * @return true if the user exists, otherwise false
     */
    boolean existsByUsername(String username);

    /**
     * Retrieves a {@link User} by its username.
     *
     * @param username the username of the user
     * @return the {@link User} associated with the username
     */
    User getUserBy(String username);

    /**
     * Retrieves a list of users whose usernames start with the specified base username.
     * <p>
     * This method performs a query to find users where their username begins with the provided
     * base username. It is useful for searching or autocomplete functionalities where a user may
     * want to find usernames that match a particular pattern.
     * </p>
     *
     * @param baseUsername the base username to match against the beginning of usernames
     * @return a list of {@link User} objects with usernames starting with the given base username
     */
    List<User> findUsernamesByBaseName(String baseUsername);
}
