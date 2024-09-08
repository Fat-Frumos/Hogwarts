package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainerArgumentsProvider;
import com.epam.esm.gym.web.provider.trainer.TrainerArgumentsProvider;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link JDBCTraineeDao}.
 *
 * <p>This class uses Mockito to test the methods in the {@link JDBCTraineeDao} class. It verifies the
 * correctness of CRUD operations and query methods by mocking the {@link Session} and {@link Query}
 * objects provided by Hibernate.</p>
 *
 * @version 1.0.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class JDBCTraineeDaoTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Trainer> trainerQuery;

    @Mock
    private Query<Trainee> traineeQuery;

    @InjectMocks
    private JDBCTraineeDao jdbcTraineeDao;

    /**
     * Initializes the mocks and sets up the {@link Session} mock to return a mocked {@link Session} instance.
     */
    @BeforeEach
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    /**
     * Tests the {@link JDBCTraineeDao#findByUsername(String)} method.
     *
     * <p>This method tests that the DAO correctly retrieves a {@link Trainee} by username. It mocks the
     * query execution and verifies that the returned trainee matches the expected result.</p>
     *
     * @param trainee the {@link Trainee} object to use in the test.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testFindByUsername(Trainee trainee) {
        String hql = """
                SELECT t.id, t.dateOfBirth, t.address, t.id
                FROM Trainee t
                JOIN User u ON u.id = t.id
                WHERE u.username = :username
                """;
        Optional<Trainee> expectedTrainee = Optional.of(trainee);
        String username = trainee.getUser().getUsername();
        when(session.createQuery(hql, Trainee.class)).thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", username)).thenReturn(traineeQuery);
        when(traineeQuery.uniqueResultOptional()).thenReturn(expectedTrainee);
        Optional<Trainee> actualTrainee = jdbcTraineeDao.findByUsername(username);
        assertEquals(expectedTrainee, actualTrainee);
    }

    /**
     * Tests the {@link JDBCTraineeDao#save(Trainee)} method.
     *
     * <p>This method verifies that the DAO correctly saves a {@link Trainee} instance by calling the
     * {@link Session#persist(Object)} method. It uses Mockito to verify the interaction with the
     * persistence context.</p>
     *
     * @param trainee the {@link Trainee} object to save.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testSave(Trainee trainee) {
        jdbcTraineeDao.save(trainee);
        verify(session).persist(trainee);
    }

    /**
     * Tests the {@link JDBCTraineeDao#update(Trainee)} method.
     *
     * <p>This method verifies that the DAO correctly updates a {@link Trainee} instance. It mocks the
     * {@link Session#merge(Object)} method and checks that the updated trainee is as expected.</p>
     *
     * @param trainee the {@link Trainee} object to update.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testUpdate(Trainee trainee) {
        when(session.merge(trainee)).thenReturn(trainee);
        Trainee updatedTrainee = jdbcTraineeDao.update(trainee);
        assertEquals(trainee, updatedTrainee);
    }

    /**
     * Tests the {@link JDBCTraineeDao#delete(Trainee)} method.
     *
     * <p>This method verifies that the DAO correctly deletes a {@link Trainee} instance by calling the
     * {@link Session#remove(Object)} method. It uses Mockito to verify the interaction with the
     * persistence context.</p>
     *
     * @param trainee the {@link Trainee} object to delete.
     */
    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testDelete(Trainee trainee) {
        jdbcTraineeDao.delete(trainee);
        verify(session).remove(trainee);
    }

    /**
     * Tests the {@link JDBCTraineeDao#findNotAssignedTrainers(String)} method.
     *
     * <p>This method verifies that the DAO correctly retrieves trainers not assigned to a specific trainee.
     * It mocks the query execution and checks that the returned list of trainers matches the expected
     * result.</p>
     *
     * @param expectedTrainers a list of {@link Trainer} objects expected to be returned.
     * @param username         the username of the trainee whose assigned trainers are excluded.
     */
    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void testFindNotAssignedTrainers(List<Trainer> expectedTrainers, String username) {
        String hql = """
                    SELECT t
                    FROM Trainer t
                    LEFT JOIN Training tr ON tr.trainer.id = t.id
                    JOIN t.user u
                    WHERE tr.id IS NULL
                      AND u.username != :username
                """;

        when(session.createQuery(hql, Trainer.class)).thenReturn(trainerQuery);
        when(trainerQuery.setParameter("username", username)).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(expectedTrainers);
        List<Trainer> actualTrainers = jdbcTraineeDao.findNotAssignedTrainers(username);
        assertEquals(expectedTrainers, actualTrainers);
    }
}
