package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingSession;
import com.epam.esm.gym.domain.TrainingType;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * JDBC-based Data Access Object (DAO) for managing {@link Training} entities.
 *
 * <p>This DAO implementation interacts with the database using JDBC and Hibernate, extending the
 * {@link AbstractDao} class to provide CRUD operations for {@link Training} entities. It includes
 * methods for retrieving trainings by username, fetching all training types, and querying training
 * sessions based on start times. It is activated only when the application property
 * <code>strategy.dao.type</code> is set to <code>jdbc</code>.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @see AbstractDao
 * @see Training
 * @see TrainingType
 * @see TrainingSession
 * @since 1.0
 */
@Repository
@ConditionalOnProperty(name = "strategy.dao.type", havingValue = "jdbc")
public class JDBCTrainingDao extends AbstractDao<Training> implements TrainingDao {
    /**
     * Constructs a {@link JDBCTrainingDao} with the specified session factory.
     *
     * <p>This constructor initializes the DAO with the provided {@link SessionFactory} and sets
     * the entity class type for {@link Training}. It allows the DAO to interact with the database
     * and perform CRUD operations on {@link Training} entities.</p>
     *
     * @param sessionFactory the {@link SessionFactory} used for obtaining Hibernate sessions.
     */
    public JDBCTrainingDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Finds a {@link Training} entity by the username of its associated trainer or trainee.
     *
     * <p>This method performs a query to retrieve a {@link Training} based on the username of either
     * the trainer or the trainee. It uses Hibernate HQL to join and fetch associated entities to
     * ensure that all relevant data is loaded in a single query. The method returns an {@link Optional}
     * containing the result, which may be empty if no matching training is found.</p>
     *
     * @param username the username of the trainer or trainee.
     * @return an {@link Optional} containing the {@link Training} if found, otherwise an empty {@link Optional}.
     */
    @Override
    public Optional<Training> findByUsername(String username) {
        String hql = """
                SELECT tr
                FROM Training tr
                LEFT JOIN FETCH tr.trainer trainer
                LEFT JOIN FETCH tr.trainee trainee
                LEFT JOIN FETCH trainer.user trainerUser
                LEFT JOIN FETCH trainee.user traineeUser
                WHERE trainerUser.username = :username
                   OR traineeUser.username = :username
                """;
        return getSession().createQuery(hql, Training.class)
                .setParameter(USERNAME, username)
                .uniqueResultOptional();
    }

    /**
     * Retrieves a list of all {@link TrainingType} entities.
     *
     * <p>This method performs a query to fetch all {@link TrainingType} records from the database.
     * It uses Hibernate HQL to select all instances of {@link TrainingType} and returns them as
     * a list. This method is useful for obtaining a comprehensive list of available training types.</p>
     *
     * @return a {@link List} of all {@link TrainingType} entities.
     */
    @Override
    public List<TrainingType> findAllTrainingTypes() {
        return getSession().createQuery("FROM TrainingType", TrainingType.class)
                .getResultList();
    }

    /**
     * Finds all {@link Training} entities associated with a specific trainer identified by username.
     *
     * <p>This method queries the database for all {@link Training} instances where the associated
     * trainer's username matches the provided username. The method joins with the trainer's user entity
     * to filter by username and returns a list of matching {@link Training} entities.</p>
     *
     * @param username the username of the trainer.
     * @return a {@link List} of {@link Training} entities associated with the specified trainer.
     */
    @Override
    public List<Training> findTrainingsByTrainerUsername(String username) {
        String hql = """
                    SELECT t
                    FROM Training t
                    JOIN t.trainer tr
                    JOIN tr.user u
                    WHERE u.username = :username
                """;
        return getSession().createQuery(hql, Training.class)
                .setParameter(USERNAME, username)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> findAll() {
        String hql = "FROM Training";
        return getSession()
                .createQuery(hql, Training.class)
                .getResultList();
    }
}
