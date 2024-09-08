package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for managing {@link Trainee} entities using JDBC.
 *
 * <p>This DAO provides methods to interact with {@link Trainee} entities in the database.
 * It extends {@link AbstractDao} for common CRUD operations and implements
 * {@link TraineeDao} for DAO-specific methods. The DAO is conditionally created based
 * on the property "strategy.dao.type" being set to "jdbc", as specified by the
 * {@link ConditionalOnProperty} annotation.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.1
 * @see AbstractDao
 * @see TraineeDao
 * @see Trainee
 * @since 1.0
 */
@Repository
@ConditionalOnProperty(name = "strategy.dao.type", havingValue = "jdbc")
public class JDBCTraineeDao extends AbstractDao<Trainee> implements TraineeDao {

    /**
     * Constructs a {@link JDBCTraineeDao} with the specified {@link SessionFactory}.
     *
     * <p>This constructor initializes the DAO with the session factory and the entity class
     * type for {@link Trainee}. It is used by Spring to inject the necessary dependencies
     * for DAO operations. The session factory is critical for obtaining Hibernate sessions
     * required for database interactions. The constructor ensures that the DAO operates with
     * the correct entity type and session management.</p>
     *
     * @param sessionFactory the {@link SessionFactory} used for obtaining Hibernate sessions.
     */
    public JDBCTraineeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Retrieves a {@link Trainee} entity by the associated username.
     *
     * <p>This method performs an HQL query to find a {@link Trainee} based on the username
     * of the associated user. It uses a join fetch to eagerly load the user details, ensuring
     * that all related data is retrieved in a single query. The result is returned as an
     * {@link Optional} to handle cases where no matching trainee is found. The method throws
     * an {@link IllegalArgumentException} if the username is null.</p>
     *
     * @param username the username of the user associated with the trainee.
     * @return an {@link Optional} containing the {@link Trainee} if found, otherwise an empty {@link Optional}.
     * @throws IllegalArgumentException if the username is null.
     */
    @Override
    public Optional<Trainee> findByUsername(String username) {
        String hql = """
                SELECT t.id, t.dateOfBirth, t.address, t.id
                FROM Trainee t
                JOIN User u ON u.id = t.id
                WHERE u.username = :username
                """;
        return getSession().createQuery(hql, Trainee.class)
                .setParameter(USERNAME, username)
                .uniqueResultOptional();
    }

    /**
     * Retrieves a list of {@link Trainer} entities that are not assigned to any training
     * and do not match the specified username.
     *
     * <p>This method performs an HQL query to find all {@link Trainer} entities that are
     * not assigned to any training session and whose username is different from the provided
     * username. The query uses a left join to identify trainers without assignments. The result
     * is a list of trainers who meet the criteria, and it excludes trainers assigned to trainings.
     * The method throws an {@link IllegalArgumentException} if the username is null.</p>
     *
     * @param username the username to exclude from the results.
     * @return a {@link List} of {@link Trainer} entities that are not assigned and do not match the provided username.
     * @throws IllegalArgumentException if the username is null.
     */
    @Override
    public List<Trainer> findNotAssignedTrainers(String username) {
        String hql = """
                    SELECT t
                    FROM Trainer t
                    LEFT JOIN Training tr ON tr.trainer.id = t.id
                    JOIN t.user u
                    WHERE tr.id IS NULL
                      AND u.username != :username
                """;

        return getSession().createQuery(hql, Trainer.class)
                .setParameter(USERNAME, username)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> findAll() {
        String hql = """
                SELECT DISTINCT t
                FROM Trainee t
                LEFT JOIN FETCH t.user u
                LEFT JOIN FETCH t.trainings tr
                LEFT JOIN FETCH tr.type
                LEFT JOIN FETCH tr.trainer tn
                 """;
        return getSession()
                .createQuery(hql, Trainee.class)
                .getResultList();
    }
}
