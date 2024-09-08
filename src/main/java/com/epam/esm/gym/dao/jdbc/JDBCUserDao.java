package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

/**
 * JDBC-based Data Access Object (DAO) for managing {@link User} entities.
 *
 * <p>This DAO implementation uses JDBC and Hibernate to perform CRUD operations on {@link User} entities.
 * It provides methods to check if a user exists by username, retrieve a user by username, and update user
 * details. The class is activated when the application property <code>strategy.dao.type</code> is set to
 * <code>jdbc</code>.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @see AbstractDao
 * @see User
 * @since 1.0
 */
@Slf4j
@Repository
@ConditionalOnProperty(name = "strategy.dao.type", havingValue = "jdbc")
public class JDBCUserDao extends AbstractDao<User> implements UserDao {

    /**
     * Constructs a {@link JDBCUserDao} with the specified session factory.
     *
     * <p>This constructor initializes the DAO with the provided {@link SessionFactory} and sets the
     * entity class type for {@link User}. It allows the DAO to interact with the database and perform
     * CRUD operations on {@link User} entities.</p>
     *
     * @param sessionFactory the {@link SessionFactory} used for obtaining Hibernate sessions.
     */
    public JDBCUserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Checks if a {@link User} with the specified username exists in the database.
     *
     * <p>This method executes a query to count the number of {@link User} entities with the provided username.
     * It returns <code>true</code> if at least one user is found, otherwise <code>false</code>.
     * The method is useful for validating the existence of a user before
     * performing operations that require the user to be present.</p>
     *
     * @param username the username of the user to check.
     * @return <code>true</code> if a user with the specified username exists, otherwise <code>false</code>.
     */
    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM User WHERE username = :username";
        Query<Long> query = getSession().createQuery(sql, Long.class);
        query.setParameter(USERNAME, username);
        return query.getSingleResult() > 0;
    }

    /**
     * Retrieves a {@link User} entity by its username.
     *
     * <p>This method finds a user by the provided username. If the user is found, it is returned;
     * otherwise, a {@link UserNotFoundException} is thrown. The method uses a query to fetch the user
     * entity based on the username, which should be unique.</p>
     *
     * @param username the username of the user to retrieve.
     * @return the {@link User} entity corresponding to the specified username.
     * @throws UserNotFoundException if no user with the specified username is found.
     */
    @Override
    @Transactional(readOnly = true)
    public User getUserBy(String username) {
        String sql = "SELECT u FROM User u WHERE username = :username";
        Query<User> query = getSession().createQuery(sql, User.class);
        query.setParameter(USERNAME, username);
        return query.uniqueResultOptional().orElseThrow(
                () -> new UserNotFoundException(format("User not found: %s", username)));
    }

    @Override
    public List<User> findUsernamesByBaseName(String baseUsername) {
        String hql = "SELECT u FROM User u WHERE u.username LIKE :baseUsernamePattern";
        return getSession()
                .createQuery(hql, User.class)
                .setParameter("baseUsernamePattern", baseUsername + "%")
                .getResultList();
    }

    /**
     * Finds a {@link User} entity by username.
     *
     * <p>This method performs a query to retrieve a {@link User} entity based on the username. The method
     * returns an {@link Optional} containing the user if found, otherwise an empty {@link Optional}.
     * It uses Hibernate query mechanism to perform the search.</p>
     *
     * @param username the username of the user to find.
     * @return an {@link Optional} containing the {@link User} if found, otherwise an empty {@link Optional}.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT u FROM User u WHERE username = :username";
        Query<User> query = getSession().createQuery(sql, User.class);
        query.setParameter(USERNAME, username);
        return query.uniqueResultOptional();
    }

    /**
     * Updates the details of an existing {@link User}.
     *
     * <p>This method performs an update operation on the {@link User} entity, modifying fields such as
     * active status, first name, last name, password, and username. It uses HQL to specify the update
     * parameters and handles any exceptions by logging an error message.
     * The method returns the updated user entity.</p>
     *
     * @param user the {@link User} entity with updated details.
     * @return the updated {@link User} entity.
     */
    @Override
    @Transactional
    public User update(User user) {
        try {
            String hql = "UPDATE User SET active = :active, firstName = :firstName, " +
                    "lastName = :lastName, password = :password, username = :username WHERE id = :id";
            MutationQuery query = getSession().createMutationQuery(hql);
            query.setParameter("active", user.getActive());
            query.setParameter("firstName", user.getFirstName());
            query.setParameter("lastName", user.getLastName());
            query.setParameter("password", user.getPassword());
            query.setParameter(USERNAME, user.getUsername());
            query.setParameter("id", user.getId());
            query.executeUpdate();
        } catch (Exception e) {
            log.error("Error updating user: " + e.getMessage());
        }
        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        String hql = "SELECT u FROM User u LEFT JOIN FETCH u.tokens";
        Query<User> query = getSession().createQuery(hql, User.class);
        return query.getResultList();
    }
}
