package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.web.provider.trainer.TrainerArgumentsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class InMemoryTrainerDaoTest {

    @Autowired
    private InMemoryTrainerDao inMemoryTrainerDao;

    @Autowired
    private StorageInitializer storageInitializer;

    @BeforeEach
    void setUp() {
        inMemoryTrainerDao = new InMemoryTrainerDao(storageInitializer);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void findAll(List<Trainer> expectedTrainers) {
        expectedTrainers.forEach(trainer -> inMemoryTrainerDao.save(trainer));
        List<Trainer> trainers = inMemoryTrainerDao.findAll();
        assertTrue(trainers.containsAll(expectedTrainers));
        expectedTrainers.forEach(trainer -> inMemoryTrainerDao.delete(trainer));
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void save(List<Trainer> expectedTrainers) {
        Trainer savedTrainer = inMemoryTrainerDao.save(expectedTrainers.get(0));
        assertEquals(expectedTrainers.get(0), savedTrainer);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void delete(List<Trainer> expectedTrainers, String username) {
        Trainer trainer = expectedTrainers.get(0);
        inMemoryTrainerDao.save(trainer);
        inMemoryTrainerDao.delete(trainer);
        Optional<Trainer> foundTrainer = inMemoryTrainerDao.findByUsername(username);
        assertFalse(foundTrainer.isPresent());
    }


    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void activateTrainer(List<Trainer> expectedTrainers) {
        Trainer trainer = expectedTrainers.get(0);
        inMemoryTrainerDao.save(trainer);

        trainer.getUser().setActive(true);
        Trainer updatedTrainer = inMemoryTrainerDao.update(trainer);

        assertNotNull(updatedTrainer);
        assertTrue(updatedTrainer.getUser().getActive());
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void assignTraineeToTrainer(List<Trainer> expectedTrainers) {
        User user = User.builder()
                .id(2)
                .firstName("Hermione")
                .lastName("Granger")
                .username("Hermione.Granger")
                .password("password456")
                .active(true)
                .permission(RoleType.ROLE_TRAINEE).build();
        Trainee trainee = Trainee.builder()
                .id(9L)
                .dateOfBirth(LocalDate.now())
                .address("Some Address")
                .user(user)
                .trainings(new HashSet<>())
                .build();

        expectedTrainers.forEach(trainer -> inMemoryTrainerDao.save(trainer));
        Trainer trainerToUpdate = expectedTrainers.get(0);
        trainerToUpdate.getTrainees().add(trainee);
        Trainer updatedTrainer = inMemoryTrainerDao.update(trainerToUpdate);

        assertNotNull(updatedTrainer);
        assertTrue(updatedTrainer.getTrainees().contains(trainee));
    }
}
