package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrainingMapperTest {

    private final TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);

    @Test
    void testToDto() {
        Training training = Training.builder()
                .trainingName("Advanced Transfiguration")
                .trainingDate(LocalDate.of(2024, 1, 10))
                .trainingDuration(60)
                .build();

        TrainingResponse dto = trainingMapper.toDto(training);

        assertNotNull(dto);
        assertEquals("Advanced Transfiguration", dto.getTrainingName());
        assertEquals(LocalDate.of(2024, 1, 10), dto.getTrainingDate());
        assertEquals(60, dto.getTrainingDuration());
    }

    @Test
    void testToEntity() {
        TrainingRequest dto = TrainingRequest.builder()
                .trainingName("Basic Charms")
                .trainingDate("2024-02-02")
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
    void testToDtos() {
        Training training1 = Training.builder()
                .trainingName("Potions")
                .trainingDate(LocalDate.of(2024, 3, 10))
                .trainingDuration(90)
                .build();

        Training training2 = Training.builder()
                .trainingName("Herbology")
                .trainingDate(LocalDate.of(2024, 4, 20))
                .trainingDuration(75)
                .build();

        List<Training> trainings = List.of(training1, training2);

        List<TrainingResponse> dtos = trainingMapper.toDtos(trainings);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Potions", dtos.get(0).getTrainingName());
        assertEquals("Herbology", dtos.get(1).getTrainingName());
    }

    @Test
    void testToType() {
        TrainingType trainingType = TrainingType.builder()
                .id(1L)
                .trainingType(Specialization.TRANSFIGURATION)
                .build();

        TrainingTypeResponse response = trainingMapper.toType(trainingType);

        assertNotNull(response);
        assertEquals(Specialization.TRANSFIGURATION, response.getTrainingType());
        assertEquals(1L, response.getTrainingTypeId());
    }
}
