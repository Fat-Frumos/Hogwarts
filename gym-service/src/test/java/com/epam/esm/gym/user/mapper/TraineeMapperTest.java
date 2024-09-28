package com.epam.esm.gym.user.mapper;

import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse;
import com.epam.esm.gym.user.dto.trainee.PostTraineeRequest;
import com.epam.esm.gym.user.dto.trainee.PutTraineeRequest;
import com.epam.esm.gym.user.dto.trainee.TraineeProfileResponse;
import com.epam.esm.gym.user.dto.trainer.TrainerResponse;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.Training;
import com.epam.esm.gym.user.entity.TrainingType;
import com.epam.esm.gym.user.entity.User;
import com.epam.esm.gym.user.provider.trainee.TraineeProfileArgumentsProvider;
import com.epam.esm.gym.user.provider.trainee.UpdateTraineeArgumentsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
@ExtendWith(MockitoExtension.class)
class TraineeMapperTest {
    @InjectMocks
    private TraineeMapper traineeMapper;
    private PostTraineeRequest dto;
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

        dto = PostTraineeRequest.builder()
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


        Trainee actualTrainee = traineeMapper.toTrainee(user, dto);
        assertEquals(expectedTrainee, actualTrainee);
    }

    @Test
    void toSlimDto() {
        TraineeProfileResponse expectedProfile = TraineeProfileResponse.builder()
                .username("harry.potter")
                .firstName("Harry")
                .lastName("Potter")
                .address("Hogwarts")
                .active(true)
                .build();


        TraineeProfileResponse actualProfile = TraineeMapper.toSlimDto(trainee);
        assertEquals(expectedProfile, actualProfile);
    }

    @Test
    void toTrainee_WithNullAddress() {
        PostTraineeRequest dtoWithNullAddress = PostTraineeRequest.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        Trainee expectedTrainee = Trainee.builder()
                .user(user)
                .address("")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();


        Trainee actualTrainee = traineeMapper.toTrainee(user, dtoWithNullAddress);
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

        TraineeProfileResponse expectedProfile = TraineeProfileResponse.builder()
                .username("harry.potter")
                .firstName("")
                .lastName("")
                .address("")
                .active(true)
                .build();

        TraineeProfileResponse actualProfile = TraineeMapper.toSlimDto(traineeWithNullFields);
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

        FullTraineeProfileResponse profile = traineeMapper.toTraineeProfile(trainee);
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
        TrainerResponse profile = TrainerResponse.builder()
                .username("Harry.Potter")
                .firstName("Harry")
                .lastName("Potter")
                .build();

        User user = traineeMapper.toUser(profile);

        assertNotNull(user);
        assertEquals("Harry", user.getFirstName());
        assertEquals("Potter", user.getLastName());
        assertEquals("Harry.Potter", user.getUsername());
    }

    @ParameterizedTest
    @MethodSource("provideUserAndPassword")
    @DisplayName("Test toProfile method")
    void testToProfile(String username, String password, ProfileResponse expectedResponse) {
        ProfileResponse result = traineeMapper.toProfile(username, password);
        assertEquals(expectedResponse, result);
    }

    @ParameterizedTest
    @MethodSource("provideTrainerProfileAndExpectedUser")
    @DisplayName("Test toUser method")
    void testToUser(TrainerResponse profile, User expectedUser) {
        User result = traineeMapper.toUser(profile);
        assertEquals(expectedUser, result);
    }


    @Test
    void testToProfile() {
        String username = "Harry.Potter";
        String password = "expelliarmus";
        ProfileResponse expectedResponse = new ProfileResponse(username, password);
        ProfileResponse result = traineeMapper.toProfile(username, password);
        assertEquals(expectedResponse.username(), result.username());
        assertEquals(expectedResponse.password(), result.password());
    }

    @Test
    void testUpdate_withNonNullRequestFields() {
        PutTraineeRequest request = PutTraineeRequest.builder()
                .username("Harry.Potter")
                .firstName("Harry")
                .lastName("Potter")
                .address("Hogwarts")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .active(true)
                .build();

        User user = User.builder()
                .username("OldUsername")
                .firstName("OldFirstName")
                .lastName("OldLastName")
                .active(false)
                .build();

        Trainee trainee = Trainee.builder()
                .user(user)
                .address("OldAddress")
                .dateOfBirth(LocalDate.parse("1970-01-01"))
                .build();

        Trainee updatedTrainee = traineeMapper.update(request, trainee);
        assertEquals(request.username(), updatedTrainee.getUser().getUsername());
        assertEquals(request.firstName(), updatedTrainee.getUser().getFirstName());
        assertEquals(request.lastName(), updatedTrainee.getUser().getLastName());
        assertEquals(request.address(), updatedTrainee.getAddress());
        assertEquals(request.dateOfBirth(), updatedTrainee.getDateOfBirth());
        assertEquals(request.active(), updatedTrainee.getUser().getActive());
    }

    @Test
    void testUpdateWithNullRequestDoesNotUpdate() {
        PutTraineeRequest dto = PutTraineeRequest.builder().build();
        User user = User.builder().username("Harry.Potter").build();
        Trainee trainee = Trainee.builder().user(user).build();
        Trainee result = traineeMapper.update(dto, trainee);
        assertSame(trainee, result);
    }

    @Test
    void testUpdateWithNullUserThrowsException() {
        PutTraineeRequest dto = PutTraineeRequest.builder().build();
        Trainee trainee = Trainee.builder().user(null).build();
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> traineeMapper.update(dto, trainee));
        assertEquals("User cannot be null", thrown.getMessage());
    }

    @Test
    void testToTrainee() {
        PostTraineeRequest dto = PostTraineeRequest.builder()
                .address("Hogwarts")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .build();

        User user = User.builder()
                .username("Harry.Potter")
                .build();

        Trainee expectedTrainee = Trainee.builder()
                .user(user)
                .address("Hogwarts")
                .dateOfBirth(dto.dateOfBirth())
                .build();

        Trainee result = traineeMapper.toTrainee(user, dto);

        assertEquals(expectedTrainee.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(expectedTrainee.getAddress(), result.getAddress());
        assertEquals(expectedTrainee.getDateOfBirth(), result.getDateOfBirth());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void testUpdate(String username, FullTraineeProfileResponse profile, PutTraineeRequest request) {
        User user = User.builder()
                .username(username)
                .firstName("Harry")
                .lastName("Potter")
                .active(true)
                .build();

        Trainee trainee = Trainee.builder()
                .user(user)
                .address("Hogwarts")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .build();

        Trainee updatedTrainee = traineeMapper.update(request, trainee);

        assertNotNull(updatedTrainee);
        assertEquals("Harry.Potter", updatedTrainee.getUser().getUsername());
        assertEquals("Harry", updatedTrainee.getUser().getFirstName());
        assertEquals("Potter", updatedTrainee.getUser().getLastName());
        assertTrue(updatedTrainee.getUser().getActive());
        assertEquals("Hogwarts", updatedTrainee.getAddress());
        assertEquals(LocalDate.parse("1980-07-31"), updatedTrainee.getDateOfBirth());
    }

    @Test
    void testToProfileNullPasswordThrowsIllegalArgumentException() {
        String username = "Harry.Potter";
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> traineeMapper.toProfile(username, null));
        assertEquals("Password cannot be null", thrown.getMessage());
    }

    @Test
    void testToProfile_nullUsername_throwsIllegalArgumentException() {
        String password = "expelliarmus";
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> traineeMapper.toProfile(null, password));
        assertEquals("Username cannot be null", thrown.getMessage());
    }

    @Test
    void testToTrainee_happyPath() {
        User user = User.builder().username("Harry.Potter").build();
        PostTraineeRequest request = PostTraineeRequest.builder()
                .address("Hogwarts")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .build();

        Trainee trainee = traineeMapper.toTrainee(user, request);
        assertEquals("Hogwarts", trainee.getAddress());
        assertEquals(LocalDate.parse("1980-07-31"), trainee.getDateOfBirth());
        assertEquals(user, trainee.getUser());
    }

    @Test
    void testToTrainee_nullUser_throwsNullPointerException() {
        PostTraineeRequest request = PostTraineeRequest.builder().build();

        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> traineeMapper.toTrainee(null, request));
        assertEquals("User cannot be null", thrown.getMessage());
    }

    @Test
    void testToTrainee_nullRequest_throwsNullPointerException() {
        User user = User.builder().username("Harry.Potter").build();
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> traineeMapper.toTrainee(user, null));
        assertEquals("PostTrainee Request cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @ArgumentsSource(UpdateTraineeArgumentsProvider.class)
    void testUpdateVariousNullField(
            PutTraineeRequest request,
            Trainee initialTrainee, Trainee expectedTrainee) {
        Trainee updatedTrainee = traineeMapper.update(request, initialTrainee);
        assertEquals(expectedTrainee, updatedTrainee);
    }

    @Test
    void testUpdate_nullRequest_throwsNullPointerException() {
        Trainee trainee = Trainee.builder()
                .user(User.builder().username("Harry.Potter").build())
                .build();

        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> traineeMapper.update(null, trainee));
        assertEquals("TraineeRequest cannot be null", thrown.getMessage());
    }

    @Test
    void testUpdate_nullTrainee_throwsNullPointerException() {
        PutTraineeRequest request = PutTraineeRequest.builder().build();

        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> traineeMapper.update(request, null));
        assertEquals("Trainee cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void testToTraineeProfile(
            String username,
            FullTraineeProfileResponse expectedResponse,
            PutTraineeRequest request) {

        User user = User.builder()
                .username(username)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .active(request.active())
                .build();

        Trainee trainee = Trainee.builder()
                .user(user)
                .address(request.address())
                .dateOfBirth(request.dateOfBirth())
                .trainers(null)
                .build();

        FullTraineeProfileResponse result = traineeMapper.toTraineeProfile(trainee);

        assertEquals(expectedResponse.username(), result.username());
        assertEquals(expectedResponse.firstName(), result.firstName());
        assertEquals(expectedResponse.lastName(), result.lastName());
        assertEquals(expectedResponse.address(), result.address());
        assertEquals(expectedResponse.active(), result.active());
        assertEquals(expectedResponse.dateOfBirth(), result.dateOfBirth());

        assertNotNull(result.trainers());
        assertTrue(result.trainers().isEmpty());
    }

    @Test
    void testToTrainersWithNullInput() {
        Set<Trainer> result = traineeMapper.toTrainers(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToTrainersWithEmptyList() {
        Set<Trainer> result = traineeMapper.toTrainers(Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    void testToTrainerProfileWithNullTrainer() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> traineeMapper.toTrainerProfile(null));
        assertEquals("Trainer cannot be null", thrown.getMessage());
    }

    private static Stream<Arguments> provideTrainerProfileAndExpectedUser() {
        TrainerResponse profile = TrainerResponse.builder()
                .username("professor.snape")
                .firstName("Severus")
                .lastName("Snape")
                .build();

        User expectedUser = User.builder()
                .username("professor.snape")
                .firstName("Severus")
                .lastName("Snape")
                .build();

        return Stream.of(
                Arguments.of(profile, expectedUser)
        );
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

    private static Stream<Arguments> provideTrainerResponses() {
        TrainerResponse response1 = TrainerResponse.builder()
                .username("professor.snape")
                .firstName("Severus")
                .lastName("Snape")
                .build();

        User user1 = User.builder()
                .username("professor.snape")
                .firstName("Severus")
                .lastName("Snape")
                .build();

        Trainer trainer1 = Trainer.builder()
                .user(user1)
                .build();

        return Stream.of(
                Arguments.of(List.of(response1), Set.of(trainer1))
        );
    }
}
