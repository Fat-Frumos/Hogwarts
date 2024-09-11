package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.exception.UserNotFoundException;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainerNameArgumentsProvider;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link JDBCTrainerDao}.
 *
 * <p>This class tests the functionality of the {@link JDBCTrainerDao} class, which is responsible for
 * managing operations related to the Trainer entity in the database. The tests ensure that methods for
 * finding trainers by username, activating trainers, assigning trainees to trainers, and handling
 * transactional behavior are functioning correctly.</p>
 *
 * <p>The class uses Mockito to create mock instances of {@link SessionFactory}, {@link Session},
 * {@link Query}, and {@link MutationQuery} to isolate the tests from actual database interactions.
 * JUnit 5's {@link ParameterizedTest} is used to run tests with different sets of input data provided
 * by {@link TraineeTrainerNameArgumentsProvider}. Each test method validates the correct behavior of
 * the DAO methods under various scenarios.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class JDBCTrainerDaoTest {

    @Mock
    private Transaction transaction;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Trainer> query;

    @Mock
    private MutationQuery mutationQuery;

    @InjectMocks
    private JDBCTrainerDao dao;

    /**
     * Sets up the test environment by initializing mocks and configuring the session factory.
     *
     * <p>This method is called before each test is executed. It ensures that the {@link SessionFactory}
     * returns a mock {@link Session} when {@link SessionFactory#getCurrentSession()} is called.</p>
     */
    @BeforeEach
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    /**
     * Tests finding a trainer by username.
     *
     * <p>This method verifies that {@link JDBCTrainerDao#findByName(String)} correctly retrieves
     * a trainer based on the provided username. It ensures that the result matches the expected trainer
     * when a valid username is given.</p>
     *
     */
    @Test
    void testFindByUsername() {
        String username = "trainer1";
        Trainer trainer = new Trainer();
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = dao.findByName(username);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    /**
     * Tests activating a trainer.
     *
     * <p>This method verifies that {@link JDBCTrainerDao#activateTrainer(String, Boolean)} correctly
     * sets the activation status of a trainer based on the provided username and active status. It
     * ensures that the mutation query executes successfully.</p>
     *
     * @param username the username of the trainer to activate.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testActivateTrainer(String username) {
        Boolean active = true;
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("active", active)).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("username", username)).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(1);
        dao.activateTrainer(username, active);
        verify(mutationQuery).executeUpdate();
    }

    /**
     * Tests finding trainers who are not assigned to any trainee.
     *
     * <p>This method verifies that {@link JDBCTrainerDao#findNotAssigned(String)} correctly retrieves
     * a list of trainers who are not assigned to a specific trainee. It ensures that the result contains
     * the expected number of trainers.</p>
     *
     * @param username the username of the trainee to check for unassigned trainers.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testFindNotAssigned(String username) {
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Trainer(), new Trainer()));
        List<Trainer> result = dao.findNotAssigned(username);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    /**
     * Tests failure scenario when assigning a trainee to a trainer.
     *
     * <p>This method verifies that {@link JDBCTrainerDao#assignTraineeToTrainer(String, String)} handles
     * the case where the assignment fails due to no rows being updated in the database. It ensures that
     * a {@link UserNotFoundException} is thrown in this case.</p>
     *
     * @param trainerUsername the username of the trainer to assign the trainee to.
     * @param traineeUsername the username of the trainee to be assigned.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testAssignTraineeToTrainerFailure(String trainerUsername, String traineeUsername) {
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("trainerUsername", trainerUsername)).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("traineeUsername", traineeUsername)).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(0);
        assertThrows(UserNotFoundException.class, () -> dao.assignTraineeToTrainer(trainerUsername, traineeUsername));
        verify(mutationQuery).executeUpdate();
    }

    /**
     * Tests successful assignment of a trainee to a trainer.
     *
     * <p>This method verifies that {@link JDBCTrainerDao#assignTraineeToTrainer(String, String)} correctly
     * assigns a trainee to a trainer and commits the transaction successfully. It ensures that no exception
     * is thrown and the transaction is committed.</p>
     *
     * @param trainerUsername the username of the trainer to assign the trainee to.
     * @param traineeUsername the username of the trainee to be assigned.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testAssignTraineeToTrainerSuccess(String trainerUsername, String traineeUsername) {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.createMutationQuery(anyString())).thenReturn(query);
        when(query.setParameter("trainerUsername", trainerUsername)).thenReturn(query);
        when(query.setParameter("traineeUsername", traineeUsername)).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);
        assertDoesNotThrow(() -> dao.assignTraineeToTrainer(trainerUsername, traineeUsername));
        verify(transaction).commit();
    }

    /**
     * Tests assigning a trainee to a trainer when the trainee is not found.
     *
     * <p>This method verifies that {@link JDBCTrainerDao#assignTraineeToTrainer(String, String)} correctly
     * handles the case where the assignment fails because the trainee could not be found. It ensures that
     * an {@link EntityNotFoundException} with an appropriate message is thrown.</p>
     *
     * @param trainerUsername the username of the trainer to assign the trainee to.
     * @param traineeUsername the username of the trainee to be assigned.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testAssignTraineeToTrainerTraineeNotFound(String trainerUsername, String traineeUsername) {
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.createMutationQuery(anyString())).thenReturn(query);
        when(query.setParameter("trainerUsername", trainerUsername)).thenReturn(query);
        when(query.setParameter("traineeUsername", traineeUsername)).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                dao.assignTraineeToTrainer(trainerUsername, traineeUsername));
        assertEquals("Failed to assign trainee. Trainee might not exist.", exception.getMessage());
        verify(transaction).commit();
    }

    /**
     * Tests exception handling during the assignment of a trainee to a trainer.
     *
     * <p>This method verifies that {@link JDBCTrainerDao#assignTraineeToTrainer(String, String)} properly
     * handles exceptions thrown during the database update process. It ensures that the exception message
     * is correctly propagated and the mutation query is executed.</p>
     *
     * @param trainerUsername the username of the trainer to assign the trainee to.
     * @param traineeUsername the username of the trainee to be assigned.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testAssignTraineeToTrainerExceptionHandling(String trainerUsername, String traineeUsername) {
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("trainerUsername", trainerUsername)).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("traineeUsername", traineeUsername)).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(UserNotFoundException.class,
                () -> dao.assignTraineeToTrainer(trainerUsername, traineeUsername));
        assertTrue(exception.getMessage().contains("Database error"));
        verify(mutationQuery).executeUpdate();
    }

    /**
     * Tests the case when no transaction rollback is performed.
     *
     * <p>This method verifies that {@link JDBCTrainerDao#assignTraineeToTrainer(String, String)} does not
     * perform a transaction rollback if the update is successful. It ensures that the mutation query is
     * executed and the transaction rollback is not triggered.</p>
     *
     * @param trainerUsername the username of the trainer to assign the trainee to.
     * @param traineeUsername the username of the trainee to be assigned.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testAssignTraineeToTrainerTransactionNotActive(String trainerUsername, String traineeUsername) {
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("trainerUsername", trainerUsername)).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("traineeUsername", traineeUsername)).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(1);
        dao.assignTraineeToTrainer(trainerUsername, traineeUsername);
        verify(mutationQuery).executeUpdate();
        verify(transaction, never()).rollback();
    }
}
