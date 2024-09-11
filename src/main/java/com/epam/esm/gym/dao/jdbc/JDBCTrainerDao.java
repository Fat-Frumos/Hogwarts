package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.exception.UserNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for managing {@link Trainer} entities using JDBC.
 *
 * <p>This DAO extends {@link AbstractDao} for common CRUD operations and implements
 * {@link TrainerDao} to handle trainer-specific data access. It is conditionally created
 * based on the property "strategy.dao.type" being set to "jdbc", as specified by the
 * {@link ConditionalOnProperty} annotation. This class provides methods to find, activate,
 * and manage trainers. It operates with the {@link Trainer} entity and leverages Hibernate
 * for session management and queries.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.1
 * @see AbstractDao
 * @see TrainerDao
 * @see Trainer
 * @since 1.0
 */
@Repository
@ConditionalOnProperty(name = "strategy.dao.type", havingValue = "jdbc")
public class JDBCTrainerDao extends AbstractDao<Trainer> implements TrainerDao {

    /**
     * Constructs a {@link JDBCTrainerDao} with the specified {@link SessionFactory}.
     *
     * <p>This constructor initializes the DAO with the session factory and the entity class
     * type for {@link Trainer}. It is used by Spring to inject the necessary dependencies
     * for DAO operations. The session factory is essential for obtaining Hibernate sessions
     * required for database interactions. The constructor ensures that the DAO operates with
     * the correct entity type and session management.</p>
     *
     * @param sessionFactory the {@link SessionFactory} used for obtaining Hibernate sessions.
     */
    public JDBCTrainerDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Retrieves a {@link Trainer} entity by the associated username.
     *
     * <p>This method performs an HQL query to find a {@link Trainer} based on the username
     * of the associated user. It uses a join fetch to eagerly load the user details, ensuring
     * that all related data is retrieved in a single query. The result is returned as an
     * {@link Optional} to handle cases where no matching trainer is found. If the username
     * is null, an {@link IllegalArgumentException} is thrown.</p>
     *
     * @param username the username of the user associated with the trainer.
     * @return an {@link Optional} containing the {@link Trainer} if found, otherwise an empty {@link Optional}.
     * @throws IllegalArgumentException if the username is null.
     */
    @Override
    public Optional<Trainer> findByName(String username) {
        String hql = """
                    SELECT t
                    FROM Trainer t
                    JOIN User u ON u.id = t.user.id
                    WHERE u.username = :username
                """;
        return getSession().createQuery(hql, Trainer.class)
                .setParameter(USERNAME, username)
                .uniqueResultOptional();
    }

    /**
     * Updates the active status of a trainer based on the provided username.
     *
     * <p>This method performs an HQL update query to set the active status of the user associated
     * with a trainer. The update affects all trainers with the given username, setting the active
     * flag to the specified boolean value. The method uses a sub query to target the user by username
     * and performs the update in a single operation. If an exception occurs during the update, it
     * is propagated to the caller.</p>
     *
     * @param username the username of the trainer whose active status is to be updated.
     * @param active   the new active status to be set.
     */
    @Override
    public void activateTrainer(String username, Boolean active) {
        String hql = """
                    UPDATE User u
                    SET u.active = :active
                    WHERE u.id IN (
                        SELECT t.user.id
                        FROM Trainer t
                        WHERE t.user.username = :username
                    )
                """;
        getSession().createMutationQuery(hql)
                .setParameter("active", active)
                .setParameter(USERNAME, username)
                .executeUpdate();
    }

    /**
     * Finds trainers who are not assigned to any training sessions and whose username is not
     * equal to the specified username.
     *
     * <p>This method performs an HQL query to retrieve all {@link Trainer} entities that are
     * not associated with any training session and have a username different from the provided
     * username. The query uses a sub query to filter out trainers already assigned to a training.
     * The result is a list of trainers who meet the criteria. If no trainers meet the criteria,
     * an empty list is returned.</p>
     *
     * @param username the username to exclude from the results.
     * @return a {@link List} of {@link Trainer} entities that are not assigned to any training
     * and do not match the provided username.
     * @throws IllegalArgumentException if the username is null.
     */
    @Override
    public List<Trainer> findNotAssigned(String username) {
        String hql = """
                    SELECT t
                    FROM Trainer t
                    WHERE t.id NOT IN (
                        SELECT tr.trainer.id
                        FROM Training tr
                    )
                    AND t.user.username != :username
                """;
        return getSession().createQuery(hql, Trainer.class)
                .setParameter(USERNAME, username)
                .getResultList();
    }

    /**
     * Assigns a trainee to a trainer based on the provided usernames.
     *
     * <p>This method performs an HQL update query to assign a trainee to a trainer using their
     * respective usernames. It starts a transaction to ensure atomicity and commit the changes
     * if successful. If no trainees are updated, it throws an {@link UserNotFoundException}
     * to indicate that the assignment might have failed. If an error occurs during the operation,
     * the transaction is rolled back to maintain data integrity.</p>
     *
     * @param trainerUsername the username of the trainer to whom the trainee is to be assigned.
     * @param traineeUsername the username of the trainee to be assigned to the trainer.
     * @throws UserNotFoundException if the trainee cannot be found or if the assignment fails.
     */
    public void assignTraineeToTrainer(String trainerUsername, String traineeUsername) {
        Session session = getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            String hql = """
                    UPDATE Trainee t SET t.trainers = (
                    SELECT tr FROM Trainer tr WHERE tr.user.username = :trainerUsername)
                    WHERE t.user.username = :traineeUsername
                    """;
            int updatedEntities = session.createMutationQuery(hql)
                    .setParameter("trainerUsername", trainerUsername)
                    .setParameter("traineeUsername", traineeUsername)
                    .executeUpdate();

            transaction.commit();

            if (updatedEntities == 0) {
                throw new UserNotFoundException("Failed to assign trainee. Trainee might not exist.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new UserNotFoundException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findAll() {
        Session session = getSession();
        String hql = """
                    SELECT t
                    FROM Trainer t
                    LEFT JOIN FETCH t.user u
                    LEFT JOIN FETCH t.trainingTypes tt
                    LEFT JOIN FETCH t.trainingSessions ts
                    WHERE u.permission = :rolePermission
                """;
        Query<Trainer> query = session.createQuery(hql, Trainer.class);
        query.setParameter("rolePermission", RoleType.ROLE_TRAINER);

        return query.getResultList();
    }
}
