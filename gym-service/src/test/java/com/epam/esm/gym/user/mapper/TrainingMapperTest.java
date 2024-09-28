package com.epam.esm.gym.user.mapper;

import com.epam.esm.gym.user.dto.training.TrainingRequest;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.Training;
import com.epam.esm.gym.user.entity.TrainingType;
import com.epam.esm.gym.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link TrainingMapper} class.
 *
 * <p>This class tests the functionality of the {@link TrainingMapper}, which is responsible
 * for mapping between {@link Training} entities and their corresponding DTOs.
 * The tests ensure that the mapping logic is correctly implemented and that the
 * resulting DTOs or entities accurately reflect the data from the source.</p>
 *
 * <p>Tests cover various aspects of mapping, including the conversion of fields between
 * entities and DTOs, handling of nested objects, and any custom mapping logic that may
 * be implemented in the {@link TrainingMapper}.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class TrainingMapperTest {

    @InjectMocks
    private TrainingMapper trainingMapper;

    @Test
    void testToEntity() {
        TrainingRequest dto = TrainingRequest.builder()
                .trainingName("Basic Charms")
                .trainingDate(LocalDate.parse("2024-02-02"))
                .trainingDuration(45)
                .build();

        Trainee trainee = Trainee.builder().build();
        Trainer trainer = Trainer.builder().build();

        Training training = trainingMapper.toEntity(dto, trainee, trainer);

        assertNotNull(training);
        assertEquals("Basic Charms", training.getTrainingName());
        assertEquals(LocalDate.parse("2024-02-02"), training.getTrainingDate());
        assertEquals(45, training.getTrainingDuration());
        assertEquals(trainee, training.getTrainee());
        assertEquals(trainer, training.getTrainer());
    }

    @Test
    void testToType() {
        TrainingType trainingType = TrainingType.builder()
                .id(1L)
                .specialization(Specialization.TRANSFIGURATION)
                .build();

        TrainingTypeDto response = TrainingMapper.toType(trainingType);

        assertNotNull(response);
        assertEquals(Specialization.TRANSFIGURATION, response.getSpecialization());
    }

    @Test
    void testToEntityType() {
        TrainingTypeDto dto = TrainingTypeDto.builder()
                .specialization(Specialization.TRANSFIGURATION)
                .build();
        TrainingType entity = TrainingMapper.toEntityType(dto);

        assertEquals(Specialization.TRANSFIGURATION, entity.getSpecialization());
    }

    @Test
    void testToTypes() {
        TrainingTypeDto dto1 = TrainingTypeDto.builder()
                .specialization(Specialization.TRANSFIGURATION)
                .build();

        TrainingTypeDto dto2 = TrainingTypeDto.builder()
                .specialization(Specialization.TRANSFIGURATION)
                .build();

        List<TrainingTypeDto> dtos = List.of(dto1, dto2);
        List<TrainingType> entities = TrainingMapper.toTypes(dtos);
        assertEquals(2, entities.size());
        TrainingType entity1 = entities.get(0);
        assertEquals(Specialization.TRANSFIGURATION, entity1.getSpecialization());

        TrainingType entity2 = entities.get(1);
        assertEquals(Specialization.TRANSFIGURATION, entity2.getSpecialization());
    }

    @Test
    void testToResponses() {
        Training training1 = Training.builder()
                .trainer(Trainer.builder()
                        .user(User.builder()
                                .username("trainer1")
                                .build())
                        .build())
                .type(TrainingType.builder()
                        .specialization(Specialization.DEFENSE)
                        .build())
                .trainingName("Training1")
                .trainingDuration(60)
                .build();

        Training training2 = Training.builder()
                .trainer(Trainer.builder()
                        .user(User.builder()
                                .username("trainer2")
                                .build())
                        .build())
                .type(TrainingType.builder()
                        .specialization(Specialization.DEFAULT)
                        .build())
                .trainingName("Training2")
                .trainingDuration(90)
                .build();

        List<Training> trainings = List.of(training1, training2);
        List<TrainingResponse> responses = TrainingMapper.toResponses(trainings);

        assertEquals(2, responses.size());
        assertEquals(TrainingResponse.builder()
                .trainerName("trainer1")
                .trainingName("Training1")
                .trainingType("DEFENSE")
                .trainingDuration(60)
                .build(), responses.get(0));
        assertEquals(TrainingResponse.builder()
                .trainerName("trainer2")
                .trainingName("Training2")
                .trainingType("DEFAULT")
                .trainingDuration(90)
                .build(), responses.get(1));
    }

    @ParameterizedTest
    @MethodSource("provideTrainingTypes")
    void testToTypesDto_happyPath2(List<TrainingType> trainingTypes, List<TrainingTypeDto> expectedDtos) {
        List<TrainingTypeDto> result = TrainingMapper.toTypesDto(trainingTypes);
        assertEquals(expectedDtos, result);
    }

    @ParameterizedTest
    @MethodSource("provideNullTrainingTypes")
    void testToTypesDto_unhappyPath_nullTrainingTypes(List<TrainingType> trainingTypes) {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> TrainingMapper.toTypesDto(trainingTypes));
        assertEquals("TrainingTypesDto cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTrainingTypeDtos")
    void testToTypes_happyPath1(List<TrainingTypeDto> dtos, List<TrainingType> expectedEntities) {
        List<TrainingType> result = TrainingMapper.toTypes(dtos);
        assertEquals(expectedEntities, result);
    }

    @ParameterizedTest
    @MethodSource("provideNullTrainingTypeDtos")
    void testToTypes_unhappyPath_nullTrainingTypeDtos(List<TrainingTypeDto> dtos) {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> TrainingMapper.toTypes(dtos));
        assertEquals("TrainingTypesDto cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideValidTrainingTypeDtos")
    void testToEntityType_happyPath3(TrainingTypeDto dto, TrainingType expectedEntity) {
        TrainingType result = TrainingMapper.toEntityType(dto);
        assertEquals(expectedEntity, result);
    }

    @ParameterizedTest
    @MethodSource("provideNullTrainingTypeDto")
    void testToEntityType_unhappyPath_nullDto(TrainingTypeDto dto) {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> TrainingMapper.toEntityType(dto));
        assertEquals("TrainingTypeDto cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTrainingEntities")
    void testToDto_happyPath1(Training training, TrainingResponse expectedResponse) {
        TrainingResponse result = TrainingMapper.toDto(training);
        assertEquals(expectedResponse, result);
    }

    @ParameterizedTest
    @MethodSource("provideNullTraining")
    void testToDto_unhappyPath_nullTraining(Training training) {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> TrainingMapper.toDto(training));
        assertEquals("Training cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTrainingList")
    void testToResponses_happyPath4(List<Training> trainings, List<TrainingResponse> expectedResponses) {
        List<TrainingResponse> result = TrainingMapper.toResponses(trainings);
        assertEquals(expectedResponses, result);
    }

    @ParameterizedTest
    @MethodSource("provideNullTrainings")
    void testToResponses_unhappyPath_nullTrainings(List<Training> trainings) {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> TrainingMapper.toResponses(trainings));
        assertEquals("Trainings cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTrainingRequestData")
    void testToEntity_happyPath4(TrainingRequest request, Trainee trainee, Trainer trainer, Training expectedTraining) {
        Training result = trainingMapper.toEntity(request, trainee, trainer);
        assertEquals(expectedTraining, result);
    }

    private static Stream<Arguments> provideNullTrainingTypes() {
        return Stream.of(
                Arguments.of((List<TrainingType>) null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTrainingTypes")
    void testToTypesDto_happyPath(List<TrainingType> trainingTypes, List<TrainingTypeDto> expectedDtos) {
        List<TrainingTypeDto> result = TrainingMapper.toTypesDto(trainingTypes);
        assertEquals(expectedDtos, result);
    }

    @ParameterizedTest
    @MethodSource("provideNullTrainingTypes")
    void testToTypesDto_unhappyPathNullTrainingTypes() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> TrainingMapper.toTypesDto(null));
        assertEquals("TrainingTypesDto cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTrainingTypeDtos")
    void testToTypes_happyPath(List<TrainingTypeDto> dtos, List<TrainingType> expectedEntities) {
        List<TrainingType> result = TrainingMapper.toTypes(dtos);
        assertEquals(expectedEntities, result);
    }

    @ParameterizedTest
    @MethodSource("provideNullTrainingTypeDtos")
    void testToTypes_unhappyPath_nullTrainingTypeDtos() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> TrainingMapper.toTypes(null));
        assertEquals("TrainingTypesDto cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideValidTrainingTypeDtos")
    void testToEntityType_happyPath(TrainingTypeDto dto, TrainingType expectedEntity) {
        TrainingType result = TrainingMapper.toEntityType(dto);
        assertEquals(expectedEntity, result);
    }

    @ParameterizedTest
    @MethodSource("provideNullTrainingTypeDto")
    void testToEntityType_unhappyPath_nullDto() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> TrainingMapper.toEntityType(null));
        assertEquals("TrainingTypeDto cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTrainingEntities")
    void testToDto_happyPath(Training training, TrainingResponse expectedResponse) {
        TrainingResponse result = TrainingMapper.toDto(training);
        assertEquals(expectedResponse, result);
    }

    @ParameterizedTest
    @MethodSource("provideNullTraining")
    void testToDto_unhappyPath_nullTraining() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> TrainingMapper.toDto(null));
        assertEquals("Training cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTrainingList")
    void testToResponses_happyPath(List<Training> trainings, List<TrainingResponse> expectedResponses) {
        List<TrainingResponse> result = TrainingMapper.toResponses(trainings);
        assertEquals(expectedResponses, result);
    }

    @ParameterizedTest
    @MethodSource("provideNullTrainings")
    void testToResponses_unhappyPath_nullTrainings() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> TrainingMapper.toResponses(null));
        assertEquals("Trainings cannot be null", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTrainingRequestData")
    void testToEntity_happyPath(TrainingRequest request, Trainee trainee, Trainer trainer, Training expectedTraining) {
        Training result = trainingMapper.toEntity(request, trainee, trainer);
        assertEquals(expectedTraining, result);
    }

    private static Stream<Arguments> provideTrainingTypes() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList(TrainingType.builder().specialization(Specialization.DEFENSE).build(),
                                TrainingType.builder().specialization(Specialization.DEFENSE).build()),
                        Arrays.asList(TrainingTypeDto.builder().specialization(Specialization.DEFENSE).build(),
                                TrainingTypeDto.builder().specialization(Specialization.DEFENSE).build())
                )
        );
    }

    private static Stream<Arguments> provideTrainingTypeDtos() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList(TrainingTypeDto.builder().specialization(Specialization.CARDIO).build(),
                                TrainingTypeDto.builder().specialization(Specialization.CARDIO).build()),
                        Arrays.asList(TrainingType.builder().specialization(Specialization.CARDIO).build(),
                                TrainingType.builder().specialization(Specialization.CARDIO).build())
                )
        );
    }

    private static Stream<Arguments> provideNullTrainingTypeDtos() {
        return Stream.of(
                Arguments.of((List<TrainingTypeDto>) null)
        );
    }

    private static Stream<Arguments> provideValidTrainingTypeDtos() {
        return Stream.of(
                Arguments.of(TrainingTypeDto.builder().specialization(Specialization.DEFENSE).build(),
                        TrainingType.builder().specialization(Specialization.DEFENSE).build()),
                Arguments.of(TrainingTypeDto.builder().specialization(Specialization.DEFENSE).build(),
                        TrainingType.builder().specialization(Specialization.DEFENSE).build())
        );
    }

    private static Stream<Arguments> provideNullTrainingTypeDto() {
        return Stream.of(
                Arguments.of((TrainingTypeDto) null)
        );
    }

    private static Stream<Arguments> provideTrainingEntities() {
        return Stream.of(
                Arguments.of(
                        Training.builder()
                                .trainingName("Yoga Class")
                                .trainingDate(LocalDate.now())
                                .trainingDuration(60)
                                .trainer(Trainer.builder().user(User.builder().username("John").build()).build())
                                .trainee(Trainee.builder().user(User.builder().username("Alice").build()).build())
                                .build(),
                        TrainingResponse.builder()
                                .trainerName("John")
                                .trainingName("Yoga Class")
                                .trainingType("DEFAULT")
                                .trainingDate(LocalDate.now())
                                .trainingDuration(60)
                                .build()
                )
        );
    }

    private static Stream<Arguments> provideNullTraining() {
        return Stream.of(
                Arguments.of((Training) null)
        );
    }

    private static Stream<Arguments> provideTrainingList() {
        return Stream.of(
                Arguments.of(
                        Collections.singletonList(Training.builder()
                                .trainingName("Yoga Class")
                                .trainingDate(LocalDate.now())
                                .trainingDuration(60)
                                .trainer(Trainer.builder().user(User.builder().username("John").build()).build())
                                .trainee(Trainee.builder().user(User.builder().username("Alice").build()).build())
                                .build()),
                        Collections.singletonList(TrainingResponse.builder()
                                .trainerName("John")
                                .trainingName("Yoga Class")
                                .trainingType("DEFAULT")
                                .trainingDate(LocalDate.now())
                                .trainingDuration(60)
                                .build())
                )
        );
    }

    private static Stream<Arguments> provideNullTrainings() {
        return Stream.of(
                Arguments.of((List<Training>) null)
        );
    }

    private static Stream<Arguments> provideTrainingRequestData() {
        return Stream.of(
                Arguments.of(
                        TrainingRequest.builder()
                                .trainingName("Yoga Class")
                                .trainingDate(LocalDate.now())
                                .trainingDuration(60)
                                .build(),
                        Trainee.builder().user(User.builder().username("John").build()).build(),
                        Trainer.builder().user(User.builder().username("John").build()).build(),
                        Training.builder()
                                .trainingName("Yoga Class")
                                .trainingDate(LocalDate.now())
                                .trainingDuration(60)
                                .trainer(Trainer.builder().user(User.builder().username("John").build()).build())
                                .trainee(Trainee.builder().user(User.builder().username("Alice").build()).build())
                                .build()
                )
        );
    }
}
