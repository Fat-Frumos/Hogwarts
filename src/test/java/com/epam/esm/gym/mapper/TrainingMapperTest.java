package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
class TrainingMapperTest {

    private final TrainingMapper mapper = Mappers.getMapper(TrainingMapper.class);

    @Test
    void testToEntity() {
        TrainingRequest dto = TrainingRequest.builder()
                .trainingName("Basic Charms")
                .trainingDate("2024-02-02")
                .trainingDuration(45)
                .build();

        Trainee trainee = Trainee.builder().build();
        Trainer trainer = Trainer.builder().build();

        Training training = mapper.toEntity(dto, trainee, trainer);

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
        assertEquals(1L, response.getId());
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
        List<TrainingResponse> responses = mapper.toResponses(trainings);

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
}
