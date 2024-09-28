package com.epam.esm.gym.user.dao;

import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.provider.trainee.TraineeTrainerArgumentsProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Unit test class for {@link JpaTraineeDao}.
 *
 * <p>This class uses Mockito to test the methods in the {@link JpaTraineeDao} class.
 * It verifies the correctness of CRUD operations objects provided by Hibernate.</p>
 *
 * @version 1.0.0
 * @since 1.0
 */
@DataJpaTest
@Sql("/test-data-trainee.sql")
class JpaTraineeDaoTest {

    @Autowired
    private JpaTraineeDao traineeDao;

    @Test
    void testFindByUsernameUserExists() {
        Optional<Trainee> trainee = traineeDao.findByUsername("draco.malfoy.unique");
        assertThat(trainee).isPresent();
        assertThat(trainee.get().getUser().getUsername()).isEqualTo("draco.malfoy.unique");
    }

    @Test
    void testFindByUsernameUserNotFound() {
        Optional<Trainee> trainee = traineeDao.findByUsername("non.existent.user");
        assertThat(trainee).isNotPresent();
    }

    @Test
    void testFindAllByUsernamesUsersExist() {
        List<Trainee> trainees = traineeDao.findAllByUsernames(List.of("draco.malfoy.unique", "harry.potter.unique"));
        assertThat(trainees.size()).isEqualTo(2);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testFindByUsername(Trainee trainee) {
        traineeDao.save(trainee);
        Optional<Trainee> foundTrainee = traineeDao.findByUsername(trainee.getUser().getUsername());
        assertThat(foundTrainee).isPresent();
        assertThat(foundTrainee.get()).isEqualTo(trainee);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testSave(Trainee trainee) {
        Trainee savedTrainee = traineeDao.save(trainee);
        assertThat(savedTrainee).isNotNull();
        assertThat(savedTrainee.getId()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testUpdate(Trainee trainee) {
        traineeDao.save(trainee);
        trainee.setAddress("New Address");
        Trainee updatedTrainee = traineeDao.save(trainee);
        assertThat(updatedTrainee.getAddress()).isEqualTo("New Address");
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerArgumentsProvider.class)
    void testDelete(Trainee trainee) {
        traineeDao.save(trainee);
        traineeDao.delete(trainee);
        Optional<Trainee> deletedTrainee = traineeDao.findByUsername(trainee.getUser().getUsername());
        assertThat(deletedTrainee).isNotPresent();
    }
}
