package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainingArgumentsProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class InMemoryTrainingDaoTest {

    @Autowired
    private InMemoryTrainingDao inMemoryTrainingDao;

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainingArgumentsProvider.class)
    void findAll(Map<String, String> filters, Trainee trainee, Training training, TrainingResponse trainingResponse) {
        inMemoryTrainingDao.save(training);
        List<Training> trainings = inMemoryTrainingDao.findAll();
        assertFalse(trainings.isEmpty());
        assertTrue(trainings.contains(training));
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainingArgumentsProvider.class)
    void findAllTrainingTypes(Map<String, String> filters, Trainee trainee, Training training, TrainingResponse trainingResponse) {
        inMemoryTrainingDao.save(training);
        List<TrainingType> trainingTypes = inMemoryTrainingDao.findAllTrainingTypes();
        assertFalse(trainingTypes.isEmpty());
        assertTrue(trainingTypes.contains(training.getType()));
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainingArgumentsProvider.class)
    void save(Map<String, String> filters, Trainee trainee, Training training, TrainingResponse trainingResponse) {
        Training savedTraining = inMemoryTrainingDao.save(training);
        assertNotNull(savedTraining);
        assertEquals(training, savedTraining);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainingArgumentsProvider.class)
    void delete(Map<String, String> filters, Trainee trainee, Training training, TrainingResponse trainingResponse) {
        inMemoryTrainingDao.save(training);
        inMemoryTrainingDao.delete(training);
        Optional<Training> deletedTraining = inMemoryTrainingDao.findByUsername(training.getTrainingName());
        assertTrue(deletedTraining.isEmpty());
    }
}