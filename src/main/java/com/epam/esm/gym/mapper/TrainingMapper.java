package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeDto;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * Mapper interface for converting between Training entities and related DTOs.
 *
 * <p>This interface provides methods for mapping {@link Training} entities to {@link TrainingResponse} DTOs,
 * and converting {@link TrainingType} entities to {@link com.epam.esm.gym.dto.training.TrainingTypeDto}.
 * It also includes methods to create {@link Training} entities from request data.</p>
 */
@Mapper(componentModel = "spring")
public interface TrainingMapper {
    /**
     * Converts a {@link Training} entity to a {@link TrainingResponse} DTO.
     *
     * <p>This method maps the details of a {@link Training} entity to a {@link TrainingResponse} DTO.</p>
     *
     * @param training the training entity to convert
     * @return the converted {@link TrainingResponse}
     */
    TrainingResponse toDto(Training training);

    /**
     * Converts a list of {@link Training} entities to a list of {@link TrainingResponse} DTOs.
     *
     * <p>This method maps a list of {@link Training} entities to a list of {@link TrainingResponse} DTOs.</p>
     *
     * @param trainings the list of training entities to convert
     * @return the list of converted {@link TrainingResponse} DTOs
     */
    default List<TrainingResponse> toDtos(List<Training> trainings) {
        return trainings.stream().map(this::toDto).toList();
    }

    /**
     * Converts a {@link TrainingType} entity to a {@link com.epam.esm.gym.dto.training.TrainingTypeDto} DTO.
     *
     * <p>This method maps the details of a {@link TrainingType}
     * entity to a {@link com.epam.esm.gym.dto.training.TrainingTypeDto} DTO.</p>
     *
     * @param trainingType the training type entity to convert
     * @return the converted {@link com.epam.esm.gym.dto.training.TrainingTypeDto}
     */
    default TrainingTypeDto toType(TrainingType trainingType) {
        return TrainingTypeDto.builder()
                .trainingType(trainingType.getSpecialization())
                .trainingTypeId(trainingType.getId())
                .build();
    }

    /**
     * Converts a {@link TrainingRequest} DTO and associated {@link Trainee}
     * and {@link Trainer} entities to a {@link Training} entity.
     *
     * <p>This method creates a {@link Training} entity using data from a {@link TrainingRequest} DTO and
     * associated {@link Trainee} and {@link Trainer} entities.</p>
     *
     * @param request the training request containing training details
     * @param trainee the associated trainee
     * @param trainer the associated trainer
     * @return the converted {@link Training} entity
     */
    default Training toEntity(TrainingRequest request, Trainee trainee, Trainer trainer) {
        return Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName(request.getTrainingName())
                .trainingDate(LocalDate.parse(request.getTrainingDate()))
                .trainingDuration(request.getTrainingDuration())
                .build();
    }
}
