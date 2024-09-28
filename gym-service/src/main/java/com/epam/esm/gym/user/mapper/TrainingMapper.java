package com.epam.esm.gym.user.mapper;

import com.epam.esm.gym.user.dto.training.TrainingRequest;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.Training;
import com.epam.esm.gym.user.entity.TrainingType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Mapper interface for converting between Training entities and related DTOs.
 *
 * <p>This interface provides methods for mapping {@link Training} entities to {@link TrainingResponse} DTOs,
 * and converting {@link TrainingType} entities to {@link com.epam.esm.gym.user.dto.training.TrainingTypeDto}.
 * It also includes methods to create {@link Training} entities from request data.</p>
 */
@Service
public class TrainingMapper {

    /**
     * Converts a list of {@link TrainingType} entities to a list of {@link TrainingTypeDto} objects.
     *
     * @param trainingTypes a list of {@link TrainingType} entities to be converted
     * @return a list of {@link TrainingTypeDto} objects
     */
    static List<TrainingTypeDto> toTypesDto(List<TrainingType> trainingTypes) {
        Objects.requireNonNull(trainingTypes, "TrainingTypesDto cannot be null");
        return trainingTypes.stream().map(TrainingMapper::toType).toList();
    }

    /**
     * Converts a list of {@link TrainingTypeDto} objects to a list of {@link TrainingType} entities.
     *
     * @param specialization a list of {@link TrainingTypeDto} objects to be converted
     * @return a list of {@link TrainingType} entities
     */
    static List<TrainingType> toTypes(List<TrainingTypeDto> specialization) {
        Objects.requireNonNull(specialization, "TrainingTypesDto cannot be null");
        return specialization.stream().map(TrainingMapper::toEntityType).toList();
    }

    /**
     * Converts a {@link TrainingTypeDto} object to a {@link TrainingType} entity.
     *
     * @param dto the {@link TrainingTypeDto} object to be converted
     * @return a {@link TrainingType} entity
     */
    static TrainingType toEntityType(TrainingTypeDto dto) {
        Objects.requireNonNull(dto, "TrainingTypeDto cannot be null");
        return TrainingType.builder()
                .specialization(dto.getSpecialization())
                .build();
    }

    /**
     * Converts a {@link Training} entity to a {@link TrainingResponse} DTO.
     *
     * <p>This method maps the details of a {@link Training} entity to a {@link TrainingResponse} DTO.</p>
     *
     * @param training the training entity to convert
     * @return the converted {@link TrainingResponse}
     */
    public static TrainingResponse toDto(Training training) {
        Objects.requireNonNull(training, "Training cannot be null");

        String trainerName = training.getTrainer() != null
                ? training.getTrainer().getUser().getUsername() : null;
        String trainingType = training.getType() != null
                ? training.getType().getSpecialization().name()
                : Specialization.DEFAULT.name();
        int trainingDuration = training.getTrainingDuration() != null
                ? training.getTrainingDuration() : 0;

        return TrainingResponse.builder()
                .trainerName(trainerName)
                .trainingName(training.getTrainingName())
                .trainingType(trainingType)
                .trainingDate(training.getTrainingDate())
                .trainingDuration(trainingDuration)
                .build();
    }

    /**
     * Converts a list of {@link Training} entities to a list of {@link TrainingResponse} DTOs.
     *
     * <p>This method maps a list of {@link Training} entities to a list of {@link TrainingResponse} DTOs.</p>
     *
     * @param trainings the list of training entities to convert
     * @return the list of converted {@link TrainingResponse} DTOs
     */
    public static List<TrainingResponse> toResponses(List<Training> trainings) {
        Objects.requireNonNull(trainings, "Trainings cannot be null");
        return trainings.stream().map(TrainingMapper::toDto).toList();
    }

    /**
     * Converts a {@link TrainingType} entity to a {@link com.epam.esm.gym.user.dto.training.TrainingTypeDto} DTO.
     *
     * <p>This method maps the details of a {@link TrainingType}
     * entity to a {@link com.epam.esm.gym.user.dto.training.TrainingTypeDto} DTO.</p>
     *
     * @param trainingType the training type entity to convert
     * @return the converted {@link com.epam.esm.gym.user.dto.training.TrainingTypeDto}
     */
    public static TrainingTypeDto toType(TrainingType trainingType) {
        Objects.requireNonNull(trainingType, "TrainingType cannot be null");
        Objects.requireNonNull(trainingType.getSpecialization(), "Specialization cannot be null");

        return TrainingTypeDto.builder()
                .specialization(trainingType.getSpecialization())
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
    public Training toEntity(TrainingRequest request, Trainee trainee, Trainer trainer) {
        Objects.requireNonNull(request, "TrainingRequest cannot be null");
        Objects.requireNonNull(trainee, "Trainee cannot be null");
        Objects.requireNonNull(trainer, "Trainer cannot be null");

        return Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName(request.getTrainingName())
                .trainingDate(request.getTrainingDate())
                .trainingDuration(request.getTrainingDuration())
                .build();
    }
}
