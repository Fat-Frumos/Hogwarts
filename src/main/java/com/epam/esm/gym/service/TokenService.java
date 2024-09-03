package com.epam.esm.gym.service;

import com.epam.esm.gym.domain.Token;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing tokens.
 * <p>
 * The `TokenService` interface provides methods for handling tokens used in authentication and authorization processes.
 * Implementations of this interface should manage token storage, retrieval, and validation to ensure proper access
 * control within the application.
 * </p>
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public interface TokenService {

    /**
     * Finds all valid access tokens associated with a specific user ID.
     * <p>
     * This method retrieves a list of tokens that are currently valid for the given user ID. Valid tokens are those
     * that have not expired or been revoked. The list returned by this method can be used for various purposes, such
     * as checking the current tokens associated with a user or performing batch operations on tokens.
     * </p>
     *
     * @param id the user ID for which to find valid access tokens
     * @return a list of valid tokens associated with the specified user ID
     */
    List<Token> findAllValidAccessTokenByUserId(Integer id);

    /**
     * Saves a list of tokens to the storage.
     * <p>
     * This method persists the provided tokens to the data storage. It can be used for bulk insertion or updating
     * of token records in the database. Implementations should handle the storage of each token and ensure consistency
     * and integrity of the data.
     * </p>
     *
     * @param tokens the list of tokens to be saved
     */
    void saveAll(List<Token> tokens);

    /**
     * Finds a token by its access token string.
     * <p>
     * This method searches for a token based on its JWT string. It returns an `Optional` that contains the token if
     * it is found and valid, or an empty `Optional` if no such token exists or it is invalid. This method is useful
     * for token validation and management operations.
     * </p>
     *
     * @param jwt the JWT string of the token to be found
     * @return an `Optional` containing the token if found and valid, or an empty `Optional` if not found
     */
    Optional<Token> findByAccessToken(String jwt);

    /**
     * Saves a token to the storage.
     * <p>
     * This method persists a single token to the data storage. It is typically used for
     * storing newly created or updated tokens. Implementations should handle the storage process
     * and ensure the token's integrity and consistency.
     * </p>
     *
     * @param token the token to be saved
     * @return the saved token
     */
    Token save(Token token);
}
