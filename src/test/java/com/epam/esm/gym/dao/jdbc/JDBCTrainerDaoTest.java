package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.domain.Trainer;
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
    private JDBCTrainerDao jdbcTrainerDao;

    @BeforeEach
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testFindByUsername() {
        String username = "trainer1";
        Trainer trainer = new Trainer();
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = jdbcTrainerDao.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    public void testActivateTrainer(String username) {
        Boolean active = true;
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("active", active)).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("username", username)).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(1);
        jdbcTrainerDao.activateTrainer(username, active);
        verify(mutationQuery).executeUpdate();
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    public void testFindNotAssigned(String username) {
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Trainer(), new Trainer()));
        List<Trainer> result = jdbcTrainerDao.findNotAssigned(username);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    public void testAssignTraineeToTrainerFailure(String trainerUsername, String traineeUsername) {
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("trainerUsername", trainerUsername)).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("traineeUsername", traineeUsername)).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(0);
        assertThrows(EntityNotFoundException.class, () -> jdbcTrainerDao.assignTraineeToTrainer(trainerUsername, traineeUsername));
        verify(mutationQuery).executeUpdate();
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    public void testAssignTraineeToTrainerSuccess(String trainerUsername, String traineeUsername) {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.createMutationQuery(anyString())).thenReturn(query);
        when(query.setParameter("trainerUsername", trainerUsername)).thenReturn(query);
        when(query.setParameter("traineeUsername", traineeUsername)).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);
        assertDoesNotThrow(() -> jdbcTrainerDao.assignTraineeToTrainer(trainerUsername, traineeUsername));
        verify(transaction).commit();
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    public void testAssignTraineeToTrainerTraineeNotFound(String trainerUsername, String traineeUsername) {
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.createMutationQuery(anyString())).thenReturn(query);
        when(query.setParameter("trainerUsername", trainerUsername)).thenReturn(query);
        when(query.setParameter("traineeUsername", traineeUsername)).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                jdbcTrainerDao.assignTraineeToTrainer(trainerUsername, traineeUsername));
        assertEquals("Failed to assign trainee. Trainee might not exist.", exception.getMessage());
        verify(transaction).commit();
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    public void testAssignTraineeToTrainer_ExceptionDuringTransaction(String trainerUsername, String traineeUsername) {
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.createMutationQuery(anyString())).thenReturn(query);
        when(query.setParameter("trainerUsername", trainerUsername)).thenReturn(query);
        when(query.setParameter("traineeUsername", traineeUsername)).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Database error"));
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                jdbcTrainerDao.assignTraineeToTrainer(trainerUsername, traineeUsername));
        assertEquals("Database error", exception.getMessage());
        verify(transaction).rollback();
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    public void testAssignTraineeToTrainer_NoUpdate(String trainerUsername, String traineeUsername) {
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.createMutationQuery(anyString())).thenReturn(query);
        when(query.setParameter("trainerUsername", trainerUsername)).thenReturn(query);
        when(query.setParameter("traineeUsername", traineeUsername)).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                jdbcTrainerDao.assignTraineeToTrainer(trainerUsername, traineeUsername));
        assertEquals("Failed to assign trainee. Trainee might not exist.", exception.getMessage());
        verify(transaction).commit();
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    public void testAssignTraineeToTrainerExceptionHandling(String trainerUsername, String traineeUsername) {
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("trainerUsername", trainerUsername)).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("traineeUsername", traineeUsername)).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(EntityNotFoundException.class, () -> jdbcTrainerDao.assignTraineeToTrainer(trainerUsername, traineeUsername));
        assertTrue(exception.getMessage().contains("Database error"));
        verify(mutationQuery).executeUpdate();
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    public void testAssignTraineeToTrainerTransactionNotActive(String trainerUsername, String traineeUsername) {
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("trainerUsername", trainerUsername)).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("traineeUsername", traineeUsername)).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(1);
        jdbcTrainerDao.assignTraineeToTrainer(trainerUsername, traineeUsername);
        verify(mutationQuery).executeUpdate();
        verify(transaction, never()).rollback();
    }
}
