package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.web.provider.trainer.TrainerArgumentsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryTrainerDaoTest {
    @Mock
    private InMemoryTrainerDao trainerDao;
    @InjectMocks
    private InMemoryTrainerDao inMemoryTrainerDao;
    @Mock
    private StorageInitializer storageInitializer;
    @Mock
    private Map<String, Trainer> trainerStorage;
    @Mock
    private Map<String, Trainee> traineeStorage;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = Trainer.builder()
                .user(User.builder().username("Hermione.Granger").active(true).build())
                .trainees(new HashSet<>())
                .build();
        when(storageInitializer.getTrainerStorage()).thenReturn(trainerStorage);
        when(storageInitializer.getTraineeStorage()).thenReturn(traineeStorage);
        trainerDao = new InMemoryTrainerDao(storageInitializer);
        inMemoryTrainerDao = new InMemoryTrainerDao(storageInitializer);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void save(List<Trainer> expectedTrainers) {
        Trainer savedTrainer = inMemoryTrainerDao.save(expectedTrainers.get(0));
        assertEquals(expectedTrainers.get(0), savedTrainer);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void delete(List<Trainer> trainers, String username) {
        Trainer hermione = trainers.get(0);
        inMemoryTrainerDao.save(hermione);
        inMemoryTrainerDao.delete(hermione);
        Optional<Trainer> foundTrainer = inMemoryTrainerDao.findByName(username);
        assertFalse(foundTrainer.isPresent());
    }

    @Test
    void testFindAll() {
        when(trainerStorage.values()).thenReturn(List.of(trainer));
        List<Trainer> trainers = trainerDao.findAll();
        assertEquals(1, trainers.size());
        assertEquals("Hermione.Granger", trainers.get(0).getUser().getUsername());
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void findAll(List<Trainer> expectedTrainers) {
        when(trainerStorage.values()).thenReturn(expectedTrainers);
        List<Trainer> actualTrainers = inMemoryTrainerDao.findAll();
        assertEquals(expectedTrainers, actualTrainers);
    }

    @Test
    void testFindByUsername() {
        when(trainerStorage.get("Hermione.Granger")).thenReturn(trainer);
        Optional<Trainer> foundTrainer = trainerDao.findByName("Hermione.Granger");
        assertTrue(foundTrainer.isPresent());
        assertEquals("Hermione.Granger", foundTrainer.get().getUser().getUsername());
    }

    @Test
    void testSave() {
        when(trainerStorage.put(trainer.getUser().getUsername(), trainer)).thenReturn(null);
        Trainer savedTrainer = trainerDao.save(trainer);
        verify(trainerStorage).put("Hermione.Granger", trainer);
        assertEquals("Hermione.Granger", savedTrainer.getUser().getUsername());
    }

    @Test
    void testUpdateExistingTrainer() {
        when(trainerStorage.containsKey(trainer.getUser().getUsername())).thenReturn(true);
        when(trainerStorage.put(trainer.getUser().getUsername(), trainer)).thenReturn(trainer);
        Trainer updatedTrainer = trainerDao.update(trainer);
        verify(trainerStorage).put("Hermione.Granger", trainer);
        assertNotNull(updatedTrainer);
    }

    @Test
    void testUpdateNonExistingTrainer() {
        when(trainerStorage.containsKey(trainer.getUser().getUsername())).thenReturn(false);
        Trainer updatedTrainer = trainerDao.update(trainer);
        assertNull(updatedTrainer);
    }

    @Test
    void testDeleteTrainer() {
        when(trainerStorage.remove(trainer.getUser().getUsername())).thenReturn(trainer);
        trainerDao.delete(trainer);
        verify(trainerStorage).remove("Hermione.Granger");
    }

    @Test
    void testActivateTrainer() {
        when(trainerStorage.get("Hermione.Granger")).thenReturn(trainer);
        trainerDao.activateTrainer("Hermione.Granger", false);
        assertFalse(trainer.getUser().getActive());
    }

    @Test
    void testActivateTrainerNotFound() {
        when(trainerStorage.get("Hermione.Granger")).thenReturn(null);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> trainerDao.activateTrainer("Hermione.Granger", false));
        assertEquals("No trainer found with username: Hermione.Granger", exception.getMessage());
    }

    @Test
    void testAssignTraineeToNonExistingTrainer() {
        when(trainerStorage.get("Hermione.Granger")).thenReturn(null);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> trainerDao.assignTraineeToTrainer("Hermione.Granger", "trainee"));
        assertEquals("No trainer found with username: Hermione.Granger", exception.getMessage());
    }

    @Test
    void testAssignNonExistingTraineeToTrainer() {
        when(trainerStorage.get("Hermione.Granger")).thenReturn(trainer);
        when(traineeStorage.get("trainee")).thenReturn(null);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> trainerDao.assignTraineeToTrainer("Hermione.Granger", "trainee"));
        assertEquals("No trainee found with username: trainee", exception.getMessage());
    }
}
