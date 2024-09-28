package com.epam.esm.gym.user.service.profile;

import com.epam.esm.gym.user.dao.JpaTokenDao;
import com.epam.esm.gym.user.entity.Token;
import com.epam.esm.gym.user.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * Implementation of {@link TokenService} that uses JDBC for token management.
 * <p>
 * This class provides methods for managing tokens using JDBC. It includes functionalities for finding valid tokens,
 * saving tokens, and managing token data in the underlying database. The implementation relies on a DAO for database
 * interactions.
 * </p>
 */
@Service
@AllArgsConstructor
public class TokenProfileService implements TokenService {

    private final JpaTokenDao dao;

    /**
     * {@inheritDoc}
     * Finds all valid access tokens associated with a specific user ID.
     *
     * @param id the user ID for which to find valid access tokens
     * @return a set of valid tokens associated with the specified user ID
     */
    @Override
    public Set<Token> findAllValidAccessTokenByUserId(Integer id) {
        return dao.findAllValidAccessTokenByUserId(id);
    }

    /**
     * {@inheritDoc}
     * Saves a list of tokens to the database.
     *
     * @param tokens the list of tokens to be saved
     */
    @Override
    public void saveAll(Set<Token> tokens) {
        dao.saveAll(tokens);
    }

    /**
     * {@inheritDoc}
     * Finds a token by its access token string.
     *
     * @param jwt the JWT string of the token to be found
     * @return an optional containing the token if found and valid, or an empty optional if not found
     */
    @Override
    public Optional<Token> findByAccessToken(String jwt) {
        return dao.findByAccessToken(jwt);
    }

    /**
     * {@inheritDoc}
     * Saves a token to the database.
     *
     * @param token the token to be saved
     * @return the saved token
     */
    @Override
    public Token save(Token token) {
        return dao.save(token);
    }
}
