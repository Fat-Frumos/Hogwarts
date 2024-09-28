package com.epam.esm.gym.user.mapper;

import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.trainer.UpdateTrainerRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import com.epam.esm.gym.user.dto.trainer.TrainerResponseDto;
import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.TrainingType;
import com.epam.esm.gym.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerMapperTest {

    private TrainerMapper trainerMapper;

    @BeforeEach
    void setUp() {
        trainerMapper = new TrainerMapper();
    }

    @ParameterizedTest
    @MethodSource("provideNullTrainerForToDto")
    void testToDto_unhappyPath_nullTrainer() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> trainerMapper.toDto(null));
        assertEquals("Trainer cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideRequestsForToEntity")
    void testToEntity_happyPath(UpdateTrainerRequest request, Trainer existingTrainer, Trainer expectedTrainer) {
        Trainer result = trainerMapper.toEntity(request, existingTrainer);
        assertEquals(expectedTrainer, result);
    }

    @ParameterizedTest
    @MethodSource("provideUserForToProfileDto")
    void testToProfileDto_happyPath(String username, String password, ProfileResponse expectedProfile) {
        ProfileResponse result = trainerMapper.toProfileDto(username, password);
        assertEquals(expectedProfile, result);
    }

    @ParameterizedTest
    @MethodSource("provideNullTrainersForToTrainerProfiles")
    void testToTrainerProfiles_unhappyPath_nullTrainers() {
        assertEquals(Collections.emptyList(), trainerMapper.toTrainerProfiles(null));
    }

    @ParameterizedTest
    @MethodSource("provideNullTrainersForToResponseDto")
    void testToResponseDto_unhappyPath_nullTrainer() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> trainerMapper.toResponseDto(null));
        assertEquals("Trainer cannot be null", thrown.getMessage());
    }

    @Test
    void testToDtoWithNullTrainer() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> trainerMapper.toDto(null));
        assertEquals("Trainer cannot be null", thrown.getMessage());
    }

    @Test
    void testToTrainerProfilesWithEmptyList() {
        List<TrainerProfile> result = trainerMapper.toTrainerProfiles(Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    void testToResponseDtoWithValidTrainer() {
        User user = User.builder().username("Harry.Potter").firstName("Harry").lastName("Potter").build();
        TrainingType type = TrainingType.builder().specialization(Specialization.DEFAULT).build();
        Trainer trainer = Trainer.builder().user(user).trainingType(type).build();
        TrainerResponseDto result = trainerMapper.toResponseDto(trainer);

        assertNotNull(result);
        assertEquals("Harry.Potter", result.getUsername());
        assertEquals("Harry", result.getFirstName());
        assertEquals("Potter", result.getLastName());
        assertEquals("DEFAULT", result.getSpecialization());
    }

    @Test
    void testToResponseDtoWithNullTrainer() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> trainerMapper.toResponseDto(null));
        assertEquals("Trainer cannot be null", thrown.getMessage());
    }

    private static Stream<Arguments> provideNullTrainerForToDto() {
        return Stream.of(Arguments.of((Trainer) null));
    }

    private static Stream<Arguments> provideRequestsForToEntity() {
        return Stream.of(
                Arguments.of(
                        UpdateTrainerRequest.builder()
                                .username("trainer1")
                                .firstName("John")
                                .lastName("Doe")
                                .active(true)
                                .build(),
                        Trainer.builder()
                                .user(User.builder().username("oldTrainer")
                                        .firstName("Old").lastName("Trainer").active(false).build())
                                .build(),
                        Trainer.builder()
                                .user(User.builder().username("trainer1")
                                        .firstName("John").lastName("Doe").active(true).build())
                                .build()
                )
        );
    }

    private static Stream<Arguments> provideUserForToProfileDto() {
        return Stream.of(
                Arguments.of("username", "password", new ProfileResponse(
                        "username", "password"))
        );
    }

    private static Stream<Arguments> provideNullTrainersForToTrainerProfiles() {
        return Stream.of(Arguments.of((List<Trainer>) null));
    }

    private static Stream<Arguments> provideUserAndTrainingTypesForToTrainer() {
        return Stream.of(
                Arguments.of(
                        User.builder().username("user1").build(),
                        Collections.singletonList(TrainingType.builder()
                                .specialization(Specialization.DEFENSE).build()),
                        Trainer.builder()
                                .user(User.builder().username("user1").build())
                                .trainingType(TrainingType.builder()
                                        .specialization(Specialization.DEFENSE).build())
                                .build()
                )
        );
    }

    private static Stream<Arguments> provideNullTrainersForToResponseDto() {
        return Stream.of(Arguments.of((Trainer) null));
    }
}
