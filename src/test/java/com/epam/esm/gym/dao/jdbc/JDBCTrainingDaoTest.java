package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainerNameArgumentsProvider;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link JDBCTrainingDao}.
 *
 * <p>This class contains test cases for verifying the behavior of the {@link JDBCTrainingDao}
 * implementation. It uses Mockito to create mock instances of {@link SessionFactory}, {@link Session},
 * and {@link Query} to isolate the testing environment from actual database interactions. The tests
 * cover methods for finding trainings by username, retrieving all training types, and saving, updating,
 * and deleting training records.</p>
 *
 * <p>The class uses JUnit 5's {@link ParameterizedTest} to run tests with multiple input values
 * provided by {@link TraineeTrainerNameArgumentsProvider}. It ensures the correct handling of
 * different scenarios, including cases where training records are found or not found.</p>
 *
 * <p>Each test method verifies the behavior of the {@link JDBCTrainingDao} methods by setting up
 * mock responses and asserting the results. The tests also verify interactions with the mock objects
 * to ensure that the correct methods are called with the expected parameters.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class JDBCTrainingDaoTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Training> trainingQuery;

    @Mock
    private Query<TrainingType> trainingTypeQuery;

    @InjectMocks
    private JDBCTrainingDao jdbcTrainingDao;

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
     * Tests finding training records by trainer and trainee usernames.
     *
     * <p>This method verifies that {@link JDBCTrainingDao#findByUsername(String)} correctly retrieves
     * training records where either the trainer or trainee's username matches the provided value.
     * It checks that the result contains the expected usernames.</p>
     *
     * @param trainerUsername the username of the trainer to search for.
     * @param traineeUsername the username of the trainee to search for.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testFindByUsername(String trainerUsername, String traineeUsername) {
        Training mockTraining = Training.builder()
                .trainee(Trainee.builder().user(User.builder().username(traineeUsername).build()).build())
                .trainer(Trainer.builder().user(User.builder().username(trainerUsername).build()).build()).build();

        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("username", trainerUsername)).thenReturn(trainingQuery);
        when(trainingQuery.uniqueResultOptional()).thenReturn(Optional.of(mockTraining));

        Optional<Training> result = jdbcTrainingDao.findByUsername(trainerUsername);

        assertTrue(result.isPresent());
        assertEquals(trainerUsername, result.get().getTrainer().getUser().getUsername());
        assertEquals(traineeUsername, result.get().getTrainee().getUser().getUsername());
    }

    /**
     * Tests finding all training records by trainer username.
     *
     * <p>This method verifies that {@link JDBCTrainingDao#findTrainingsByTrainerUsername(String)} correctly
     * retrieves a list of trainings associated with the given trainer username.</p>
     *
     * @param trainerUsername the username of the trainer to search for.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testFindTrainingsByTrainerUsername(String trainerUsername) {
        Training training = Training.builder().trainer(Trainer.builder()
                        .user(User.builder().username(trainerUsername)
                                .build())
                        .build())
                .build();
        List<Training> mockTrainings = List.of(training);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("username", trainerUsername)).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(mockTrainings);
        List<Training> result = jdbcTrainingDao.findTrainingsByTrainerUsername(trainerUsername);
        assertEquals(mockTrainings, result);
    }

    /**
     * Tests finding training records when the training is found.
     *
     * <p>This method verifies that {@link JDBCTrainingDao#findByUsername(String)} correctly returns
     * the training record when it exists for the provided username.</p>
     *
     * @param username the username to search for.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testFindByUsernameFound(String username) {
        Training training = Training.builder()
                .trainer(Trainer.builder()
                        .user(User.builder().username(username).build()).build())
                .build();
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("username", username)).thenReturn(trainingQuery);
        when(trainingQuery.uniqueResultOptional()).thenReturn(Optional.of(training));
        Optional<Training> result = jdbcTrainingDao.findByUsername(username);
        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    /**
     * Tests finding training records when no training is found.
     *
     * <p>This method verifies that {@link JDBCTrainingDao#findByUsername(String)} correctly returns
     * an empty result when no training record exists for the provided username.</p>
     *
     * @param username the username to search for.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testFindByUsernameNotFound(String username) {
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("username", username)).thenReturn(trainingQuery);
        when(trainingQuery.uniqueResultOptional()).thenReturn(Optional.empty());
        Optional<Training> result = jdbcTrainingDao.findByUsername(username);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests retrieving all training types.
     *
     * <p>This method verifies that {@link JDBCTrainingDao#findAllTrainingTypes()} correctly retrieves
     * all training types from the database.</p>
     */
    @Test
    void testFindAllTrainingTypes() {
        TrainingType mockType1 = new TrainingType();
        TrainingType mockType2 = new TrainingType();
        List<TrainingType> mockTypes = List.of(mockType1, mockType2);
        when(session.createQuery("FROM TrainingType", TrainingType.class)).thenReturn(trainingTypeQuery);
        when(trainingTypeQuery.getResultList()).thenReturn(mockTypes);
        List<TrainingType> result = jdbcTrainingDao.findAllTrainingTypes();
        assertEquals(mockTypes, result);
    }

    /**
     * Tests saving a training record.
     *
     * <p>This method verifies that {@link JDBCTrainingDao#save(Training)} correctly saves a training
     * record to the database by calling {@link Session#persist(Object)}.</p>
     *
     * @param username the username to associate with the training record.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testSave(String username) {
        Training training = Training
                .builder().trainer(Trainer.builder()
                        .user(User.builder().username(username).build())
                        .build()).build();
        doNothing().when(session).persist(training);
        Training result = jdbcTrainingDao.save(training);
        assertEquals(training, result);
        verify(session).persist(training);
    }

    /**
     * Tests updating a training record.
     *
     * <p>This method verifies that {@link JDBCTrainingDao#update(Training)} correctly updates a training
     * record by calling {@link Session#merge(Object)}.</p>
     */
    @Test
    void testUpdate() {
        Training training = new Training();
        when(session.merge(training)).thenReturn(training);
        Training result = jdbcTrainingDao.update(training);
        assertEquals(training, result);
        verify(session).merge(training);
    }

    /**
     * Tests deleting a training record.
     *
     * <p>This method verifies that {@link JDBCTrainingDao#delete(Training)} correctly deletes a training
     * record by calling {@link Session#remove(Object)}.</p>
     */
    @Test
    void testDelete() {
        Training training = new Training();
        jdbcTrainingDao.delete(training);
        verify(session).remove(training);
    }
}
