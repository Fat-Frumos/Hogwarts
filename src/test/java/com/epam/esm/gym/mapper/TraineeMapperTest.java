package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.PutTraineeRequest;
import com.epam.esm.gym.dto.trainee.SlimTraineeProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerResponse;
import com.epam.esm.gym.dto.training.TrainingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    private final TraineeMapper mapper = Mappers.getMapper(TraineeMapper.class);
    private PutTraineeRequest dto;
    private Trainee trainee;
    private User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .username("harry.potter")
                .firstName("Harry")
                .lastName("Potter")
                .active(true)
                .build();

        dto = PutTraineeRequest.builder()
                .address("Hogwarts")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        trainee = Trainee.builder()
                .user(user)
                .address("Hogwarts")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();
    }

    @Test
    void toTrainee() {
        Trainee expectedTrainee = Trainee.builder()
                .user(user)
                .address("Hogwarts")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();


        Trainee actualTrainee = mapper.toTrainee(user, dto);
        assertEquals(expectedTrainee, actualTrainee);
    }

    @Test
    void toSlimDto() {
        SlimTraineeProfileResponse expectedProfile = SlimTraineeProfileResponse.builder()
                .username("harry.potter")
                .firstName("Harry")
                .lastName("Potter")
                .address("Hogwarts")
                .active(true)
                .build();


        SlimTraineeProfileResponse actualProfile = TraineeMapper.toSlimDto(trainee);
        assertEquals(expectedProfile, actualProfile);
    }

    @Test
    void toTrainee_WithNullAddress() {
        PutTraineeRequest dtoWithNullAddress = PutTraineeRequest.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        Trainee expectedTrainee = Trainee.builder()
                .user(user)
                .address("")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();


        Trainee actualTrainee = mapper.toTrainee(user, dtoWithNullAddress);
        assertEquals(expectedTrainee, actualTrainee);
    }

    @Test
    void toSlimDto_WithNullFields() {
        Trainee traineeWithNullFields = Trainee.builder()
                .user(User.builder()
                        .username("harry.potter")
                        .firstName(null)
                        .lastName(null)
                        .active(true)
                        .build())
                .address(null)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        SlimTraineeProfileResponse expectedProfile = SlimTraineeProfileResponse.builder()
                .username("harry.potter")
                .firstName("")
                .lastName("")
                .address("")
                .active(true)
                .build();

        SlimTraineeProfileResponse actualProfile = TraineeMapper.toSlimDto(traineeWithNullFields);
        assertEquals(expectedProfile, actualProfile);
    }

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

        BaseResponse profile = mapper.toTraineeProfile(trainee);
        assertNotNull(profile);
    }

    @Test
    void testToResponse() {
        Training training = Training.builder()
                .trainer(Trainer.builder().user(User.builder().username("Minerva McGonagall").build()).build())
                .trainingName("Advanced Transfiguration")
                .type(TrainingType.builder().specialization(Specialization.TRANSFIGURATION).build())
                .trainingDuration(60)
                .trainingDate(LocalDate.of(2024, 1, 10))
                .build();

        TrainingResponse response = mapper.toResponse(training);

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
                .build();

        User user = mapper.toUser(profile);

        assertNotNull(user);
        assertEquals("Harry", user.getFirstName());
        assertEquals("Potter", user.getLastName());
        assertEquals("Harry.Potter", user.getUsername());
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

        TrainerResponse profile = mapper.toTrainerProfile(trainer);

        assertNotNull(profile);
        assertEquals("Harry", profile.getFirstName());
        assertEquals("Potter", profile.getLastName());
        assertEquals("Harry.Potter", profile.getUsername());
    }

    @ParameterizedTest
    @MethodSource("provideUserAndPassword")
    @DisplayName("Test toProfile method")
    void testToProfile(String username, String password, ProfileResponse expectedResponse) {
        TraineeMapper mapper = Mappers.getMapper(TraineeMapper.class);
        ProfileResponse result = mapper.toProfile(username, password);
        assertEquals(expectedResponse, result);
    }

    private static Stream<Arguments> provideUserAndPassword() {
        return Stream.of(
                Arguments.of("harry.potter", "password123", ProfileResponse.builder()
                        .username("harry.potter")
                        .password("password123")
                        .build()
                ),
                Arguments.of("hermione.granger", "password456", ProfileResponse.builder()
                        .username("hermione.granger")
                        .password("password456")
                        .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideTrainerProfileAndExpectedUser")
    @DisplayName("Test toUser method")
    void testToUser(TrainerResponse profile, User expectedUser) {
        TraineeMapper mapper = Mappers.getMapper(TraineeMapper.class);
        User result = mapper.toUser(profile);
        assertEquals(expectedUser, result);
    }

    private static Stream<Arguments> provideTrainerProfileAndExpectedUser() {
        TrainerResponse profile = new TrainerResponse();
        profile.setUsername("professor.snape");
        profile.setFirstName("Severus");
        profile.setLastName("Snape");

        User expectedUser = User.builder()
                .username("professor.snape")
                .firstName("Severus")
                .lastName("Snape")
                .build();

        return Stream.of(
                Arguments.of(profile, expectedUser)
        );
    }

}
