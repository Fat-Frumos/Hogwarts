package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeUpdateRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TrainerMapper.class})
public interface TraineeMapper {

    TraineeProfile toDto(Trainee trainee);

    Trainee toEntity(TraineeProfile traineeProfile);

    ProfileResponse toProfile(User user);

    default Trainee update(TraineeUpdateRequest request, Trainee trainee) {
        if (request == null || trainee == null) {
            return trainee;
        }

        if (request.getFirstName() != null) {
            trainee.setUser(User.builder()
                    .firstName(request.getFirstName())
                    .build());
        }

        if (request.getLastName() != null) {
            trainee.setUser(User.builder()
                    .lastName(request.getLastName())
                    .build());
        }

        if (request.getDateOfBirth() != null) {
            trainee.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getAddress() != null) {
            trainee.setAddress(request.getAddress());
        }

        if (request.getActive() != null) {
            trainee.setUser(User.builder()
                    .active(request.getActive())
                    .build());
        }

        return trainee;
    }

    default TrainingResponse toResponse(Training training) {
        return TrainingResponse.builder()
                .trainerName(training.getTrainer().getUser().getUsername())
                .trainingName(training.getTrainingName())
                .trainingType(training.getType().getTrainingType().name())
                .trainingDuration(training.getTrainingDuration())
                .trainingDate(training.getTrainingDate())
                .build();
    }

    default Set<Trainer> toTrainers(List<TrainerProfile> trainers) {
        return trainers.stream()
                .map(trainerProfile -> Trainer.builder()
                        .id(null)
                        .user(User.builder()
                                .username(trainerProfile.getUsername())
                                .firstName(trainerProfile.getFirstName())
                                .lastName(trainerProfile.getLastName())
                                .active(trainerProfile.isActive())
                                .build())
                        .specialization(trainerProfile.getSpecialization())
                        .build())
                .collect(Collectors.toSet());
    }

    default TrainerProfile toTrainer(Trainer trainer) {
        return TrainerProfile.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(trainer.getSpecialization())
                .active(trainer.getUser().getActive())
                .trainees(trainer.getTrainees().stream()
                        .map(trainee -> TraineeProfile.builder()
                                .firstName(trainee.getUser().getFirstName())
                                .lastName(trainee.getUser().getLastName())
                                .username(trainee.getUser().getUsername())
                                .address(trainee.getAddress())
                                .active(trainee.getUser().getActive())
                                .dateOfBirth(trainee.getDateOfBirth())
                                .build())
                        .toList())
                .build();
    }
}
