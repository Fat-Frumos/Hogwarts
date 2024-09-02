package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrainingMapperTest {

    private final TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);

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
