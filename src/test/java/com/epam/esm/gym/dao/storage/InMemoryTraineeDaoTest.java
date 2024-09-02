package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainerArgumentsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class InMemoryTraineeDaoTest {

    @Autowired
    private InMemoryTraineeDao inMemoryTraineeDao;

    @Autowired
    private StorageInitializer storageInitializer;

    @BeforeEach
    void setUp() {
        inMemoryTraineeDao = new InMemoryTraineeDao(storageInitializer);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testFindByUsername(Trainee trainee) {
        inMemoryTraineeDao.save(trainee);

        Optional<Trainee> foundTrainee = inMemoryTraineeDao.findByUsername(trainee.getUser().getUsername());
        assertTrue(foundTrainee.isPresent());
        assertEquals(trainee, foundTrainee.get());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testSaveAndFindAll(Trainee trainee) {
        inMemoryTraineeDao.save(trainee);
        assertEquals(1, inMemoryTraineeDao.findAll().size());
        assertTrue(inMemoryTraineeDao.findAll().contains(trainee));
        inMemoryTraineeDao.delete(trainee);
        assertEquals(0, inMemoryTraineeDao.findAll().size());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testUpdate(Trainee trainee) {
        inMemoryTraineeDao.save(trainee);

        Trainee updatedTrainee = Trainee.builder()
                .id(trainee.getId())
                .dateOfBirth(LocalDate.parse("1980-08-01"))
                .address("New Address")
                .user(trainee.getUser())
                .trainings(trainee.getTrainings())
                .build();

        inMemoryTraineeDao.update(updatedTrainee);
        Optional<Trainee> foundTrainee = inMemoryTraineeDao.findByUsername(trainee.getUser().getUsername());

        assertTrue(foundTrainee.isPresent());
        assertEquals(updatedTrainee, foundTrainee.get());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testDelete(Trainee trainee) {
        inMemoryTraineeDao.save(trainee);
        inMemoryTraineeDao.delete(trainee);

        Optional<Trainee> foundTrainee = inMemoryTraineeDao.findByUsername(trainee.getUser().getUsername());
        assertFalse(foundTrainee.isPresent());
    }
}
