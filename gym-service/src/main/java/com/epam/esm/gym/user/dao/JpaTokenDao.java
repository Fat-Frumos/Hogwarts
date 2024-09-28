package com.epam.esm.gym.user.dao;

import com.epam.esm.gym.user.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for accessing {@link Token} entities.
 * Provides methods to perform operations related to tokens.
 */
@Repository
public interface JpaTokenDao extends JpaRepository<Token, Long> {

    /**
     * Retrieves all valid access tokens for a given user ID.
     *
     * @param id the user ID to search for valid tokens
     * @return a set of valid tokens associated with the specified user ID
     */
    Set<Token> findAllValidAccessTokenByUserId(Integer id);

    /**
     * Finds a token by its access token string.
     *
     * @param jwt the access token string to search for
     * @return an {@link Optional} containing the token if found, otherwise empty
     */
    Optional<Token> findByAccessToken(String jwt);
}
