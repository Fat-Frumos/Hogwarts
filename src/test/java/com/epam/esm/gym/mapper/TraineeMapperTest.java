package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link TraineeMapper} class.
 *
 * <p>This class is responsible for verifying the functionality of the {@link TraineeMapper},
 * which is used to map between {@link Trainee} entities and their corresponding DTOs.
 * The tests ensure that the mapping logic correctly converts entities to DTOs and vice versa,
 * maintaining data integrity and proper field mapping.</p>
 *
 * <p>Tests include verification of field mappings, handling of nested objects, and
 * confirmation that custom mapping logic is executed as expected. This class ensures
 * that the mapper functions correctly across different scenarios and data sets.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
class TraineeMapperTest {

    private final TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);

    @Test
    void testToTraineeProfile() {
        Trainee trainee = Trainee.builder()
                .user(User.builder()
                        .firstName("Harry")
                        .lastName("Potter")
                        .username("Harry.Potter")
                        .active(true)
                        .build())
                .address("Hogwarts")
                .dateOfBirth(LocalDate.of(1980, 7, 31))
                .trainers(Set.of())
                .build();

        TraineeProfile profile = traineeMapper.toTraineeProfile(trainee);

        assertNotNull(profile);
        assertEquals("Harry", profile.getFirstName());
        assertEquals("Potter", profile.getLastName());
        assertEquals("Harry.Potter", profile.getUsername());
        assertEquals("Hogwarts", profile.getAddress());
        assertTrue(profile.getActive());
        assertEquals(LocalDate.of(1980, 7, 31), profile.getDateOfBirth());
    }

    @Test
    void testToResponse() {
        Training training = Training.builder()
                .trainer(Trainer.builder().user(User.builder().username("Minerva McGonagall").build()).build())
                .trainingName("Advanced Transfiguration")
                .type(TrainingType.builder().trainingType(Specialization.TRANSFIGURATION).build())
                .trainingDuration(60)
                .trainingDate(LocalDate.of(2024, 1, 10))
                .build();

        TrainingResponse response = traineeMapper.toResponse(training);

        assertNotNull(response);
        assertEquals("Minerva McGonagall", response.getTrainerName());
        assertEquals("Advanced Transfiguration", response.getTrainingName());
        assertEquals("TRANSFIGURATION", response.getTrainingType());
        assertEquals(60, response.getTrainingDuration());
        assertEquals(LocalDate.of(2024, 1, 10), response.getTrainingDate());
    }


    @Test
    void testToUser() {
        TrainerProfile profile = TrainerProfile.builder()
                .username("Harry.Potter")
                .firstName("Harry")
                .lastName("Potter")
                .active(true)
                .build();

        User user = traineeMapper.toUser(profile);

        assertNotNull(user);
        assertEquals("Harry", user.getFirstName());
        assertEquals("Potter", user.getLastName());
        assertEquals("Harry.Potter", user.getUsername());
        assertTrue(user.getActive());
    }

    @Test
    void testToTrainerProfile() {
        Trainer trainer = Trainer.builder()
                .user(User.builder()
                        .username("Harry.Potter")
                        .firstName("Harry")
                        .lastName("Potter")
                        .active(true)
                        .build())
                .trainees(Set.of())
                .build();

        TrainerProfile profile = traineeMapper.toTrainerProfile(trainer);

        assertNotNull(profile);
        assertEquals("Harry", profile.getFirstName());
        assertEquals("Potter", profile.getLastName());
        assertEquals("Harry.Potter", profile.getUsername());
    }
}
