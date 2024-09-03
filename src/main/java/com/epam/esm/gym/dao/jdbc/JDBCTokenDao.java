package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.TokenDao;
import com.epam.esm.gym.domain.Token;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) implementation for managing {@link Token} entities using JDBC.
 *
 * <p>This DAO provides methods to interact with {@link Token} entities in the database.
 * It extends {@link AbstractDao} to leverage common CRUD operations and implements
 * {@link TokenDao} to define additional methods specific to token management. The DAO
 * is only instantiated if the "strategy.dao.type" property is set to "jdbc", as specified
 * by the {@link ConditionalOnProperty} annotation.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.1
 * @see AbstractDao
 * @see TokenDao
 * @see Token
 * @since 1.0
 */
@Slf4j
@Repository
@ConditionalOnProperty(name = "strategy.dao.type", havingValue = "jdbc")
public class JDBCTokenDao extends AbstractDao<Token> implements TokenDao {
    /**
     * Constructs a {@link JDBCTokenDao} with the specified {@link SessionFactory}.
     *
     * <p>This constructor initializes the DAO with the session factory and the entity class
     * type for {@link Token}. It is used by Spring to inject the necessary dependencies.</p>
     *
     * @param sessionFactory the {@link SessionFactory} used for obtaining Hibernate sessions.
     */
    public JDBCTokenDao(SessionFactory sessionFactory) {
        super(Token.class, sessionFactory);
    }

    /**
     * Retrieves a {@link Token} by the associated username.
     *
     * <p>This method performs a query to find a token based on the username of the associated
     * user. It joins the {@link Token} entity with the {@link com.epam.esm.gym.domain.User} entity to filter by the
     * provided username. The result is returned as an {@link Optional} to handle the case
     * where no matching token is found.</p>
     *
     * @param username the username of the user associated with the token.
     * @return an {@link Optional} containing the {@link Token} if found, otherwise an empty {@link Optional}.
     * @throws IllegalArgumentException if the username is null.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Token> findByUsername(String username) {
        String hql = """
                select t
                from Token t
                inner join User u on t.user.id = u.id
                where u.username = :username
                """;
        return getSession().createQuery(
                        hql, Token.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    /**
     * Retrieves all valid access tokens for a given user ID.
     *
     * <p>This method performs a query to find all {@link Token} entities that are valid for
     * a specific user identified by the provided user ID. A token is considered valid if it
     * is neither expired nor revoked.</p>
     *
     * @param id the user ID to filter the tokens.
     * @return a {@link List} of valid {@link Token} entities for the specified user ID.
     * @throws IllegalArgumentException if the user ID is null.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Token> findAllValidAccessTokenByUserId(Integer id) {
        String hql = """
                select t
                from Token t
                inner join User u
                on t.user.id = u.id
                where u.id = :id and (t.expired = false or t.revoked = false)
                """;
        return getSession().createQuery(
                        hql, Token.class)
                .setParameter("id", id)
                .getResultList();
    }

    /**
     * Retrieves a {@link Token} by its access token string.
     *
     * <p>This method performs a query to find a token based on its access token string. The
     * result is returned as an {@link Optional} to handle cases where no matching token is
     * found.</p>
     *
     * @param jwt the access token string to search for.
     * @return an {@link Optional} containing the {@link Token} if found, otherwise an empty {@link Optional}.
     * @throws IllegalArgumentException if the access token string is null.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Token> findByAccessToken(String jwt) {
        String hql = "select t from Token t where t.accessToken = :jwt";
        return getSession().createQuery(hql, Token.class)
                .setParameter("jwt", jwt)
                .uniqueResultOptional();
    }

    /**
     * Saves a list of {@link Token} entities to the database.
     *
     * <p>This method persists each {@link Token} in the provided list to the database.
     * The operation is performed in a transaction to ensure that all tokens are saved
     * atomically.</p>
     *
     * @param tokens the list of {@link Token} entities to save. Must not be null.
     * @throws IllegalArgumentException if the list of tokens is null.
     */
    @Override
    @Transactional
    public void saveAll(List<Token> tokens) {
        tokens.forEach(token -> getSession().persist(token));
    }
}
