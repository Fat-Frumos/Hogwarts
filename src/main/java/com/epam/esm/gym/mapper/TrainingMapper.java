package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
    TrainingResponse toDto(Training training);

    Training toEntity(TrainingResponse trainingDto);

    private TrainingTypeResponse toResponse(Training training) {
        return TrainingTypeResponse.builder()
                .trainingType(training.getType().getTrainingType())
                .trainingTypeId(training.getType().getId())
                .build();
    }

    default List<TrainingResponse> toDtos(List<Training> trainings) {
        return trainings.stream().map(this::toDto).toList();
    }


    default TrainingTypeResponse toType(TrainingType trainingType) {
        return TrainingTypeResponse.builder()
                .trainingType(trainingType.getTrainingType())
                .trainingTypeId(trainingType.getId())
                .build();
    }

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
