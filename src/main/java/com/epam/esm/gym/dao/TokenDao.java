package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.Token;

import java.util.Optional;
import java.util.Set;

/**
 * Data Access Object (DAO) interface for managing {@link Token} entities.
 *
 * <p>This interface extends the generic {@link Dao} interface to provide additional methods specific
 * to {@link Token} operations. It includes methods to find all valid access tokens by user ID, find a
 * token by its JWT, and save a list of tokens.</p>
 */
public interface TokenDao extends Dao<Token> {

    /**
     * Finds all valid access tokens associated with a specific user ID.
     *
     * @param id the user ID
     * @return a set of valid {@link Token} objects
     */
    Set<Token> findAllValidAccessTokenByUserId(Integer id);

    /**
     * Finds a {@link Token} by its access token (JWT).
     *
     * @param jwt the access token (JWT)
     * @return an {@link Optional} containing the {@link Token} if found, otherwise empty
     */
    Optional<Token> findByAccessToken(String jwt);

    /**
     * Saves a list of {@link Token} objects.
     *
     * @param tokens the list of {@link Token} objects to save
     */
    void saveAll(Set<Token> tokens);
}
