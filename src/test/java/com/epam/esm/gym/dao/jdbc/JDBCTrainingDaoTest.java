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

    @BeforeEach
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }


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

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testFindTrainingsByTrainerUsername(String trainerUsername) {
        Training training = Training.builder().trainer(Trainer.builder().user(User.builder().username(trainerUsername).build()).build()).build();
        List<Training> mockTrainings = List.of(training);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("username", trainerUsername)).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(mockTrainings);
        List<Training> result = jdbcTrainingDao.findTrainingsByTrainerUsername(trainerUsername);
        assertEquals(mockTrainings, result);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testFindByUsernameFound(String username) {
        Training training = Training.builder().trainer(Trainer.builder().user(User.builder().username(username).build()).build()).build();
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("username", username)).thenReturn(trainingQuery);
        when(trainingQuery.uniqueResultOptional()).thenReturn(Optional.of(training));
        Optional<Training> result = jdbcTrainingDao.findByUsername(username);
        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testFindByUsernameNotFound(String username) {
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("username", username)).thenReturn(trainingQuery);
        when(trainingQuery.uniqueResultOptional()).thenReturn(Optional.empty());
        Optional<Training> result = jdbcTrainingDao.findByUsername(username);
        assertTrue(result.isEmpty());
    }

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

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testSave(String username) {
        Training training = Training.builder().trainer(Trainer.builder().user(User.builder().username(username).build()).build()).build();
        doNothing().when(session).persist(training);
        Training result = jdbcTrainingDao.save(training);
        assertEquals(training, result);
        verify(session).persist(training);
    }

    @Test
    void testUpdate() {
        Training training = new Training();
        when(session.merge(training)).thenReturn(training);
        Training result = jdbcTrainingDao.update(training);
        assertEquals(training, result);
        verify(session).merge(training);
    }

    @Test
    void testDelete() {
        Training training = new Training();
        jdbcTrainingDao.delete(training);
        verify(session).remove(training);
    }
}
