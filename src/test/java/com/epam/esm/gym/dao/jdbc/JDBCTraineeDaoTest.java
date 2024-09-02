package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.web.provider.trainer.TrainerArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainerArgumentsProvider;
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

    @BeforeEach
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }


    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testFindByUsername(Trainee trainee) {
        String hql = """
                SELECT t
                FROM Trainee t
                JOIN FETCH t.user u
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

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testFindAll(Trainee trainee) {
        List<Trainee> expectedList = List.of(trainee);
        when(session.createQuery("FROM com.epam.esm.gym.domain.Trainee", Trainee.class)).thenReturn(traineeQuery);
        when(traineeQuery.getResultList()).thenReturn(expectedList);
        List<Trainee> result = jdbcTraineeDao.findAll();
        assertEquals(expectedList, result);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testSave(Trainee trainee) {
        jdbcTraineeDao.save(trainee);
        verify(session).persist(trainee);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testUpdate(Trainee trainee) {
        when(session.merge(trainee)).thenReturn(trainee);
        Trainee updatedTrainee = jdbcTraineeDao.update(trainee);
        assertEquals(trainee, updatedTrainee);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testDelete(Trainee trainee) {
        jdbcTraineeDao.delete(trainee);
        verify(session).remove(trainee);
    }

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
