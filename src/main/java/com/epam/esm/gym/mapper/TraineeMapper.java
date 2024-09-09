package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.SlimTrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper interface for converting between various DTOs and domain models related to trainees, trainers, and training.
 *
 * <p>This interface provides methods to map between entities and data transfer objects (DTOs),
 * {@link TraineeProfile}, {@link TrainingResponse}, and {@link TrainerProfile}. It uses the {@link UserMapper} and
 * {@link TrainerMapper} components for mappings related to users and trainers.</p>
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, TrainerMapper.class})
public interface TraineeMapper {

    /**
     * Converts a {@link User} object to a {@link ProfileResponse}.
     *
     * <p>This method converts a {@link User} entity to a {@link ProfileResponse} DTO.
     * It validates that the user and required fields are not null before creating the response object.</p>
     *
     * @param username the user to convert
     * @param password the user to convert
     * @return the converted {@link ProfileResponse}
     * @throws IllegalArgumentException if user, username, or password is null
     */
    default ProfileResponse toProfile(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username, or password cannot be null");
        }
        return ProfileResponse.builder()
                .username(username)
                .password(password)
                .build();
    }

    /**
     * Updates a {@link Trainee} entity with values from a {@link TraineeRequest}.
     *
     * <p>This method updates the fields of an existing {@link Trainee}
     * entity based on the provided {@link TraineeRequest}.
     * It selectively updates fields if the corresponding request values are not null.</p>
     *
     * @param request the request containing update values
     * @param trainee the trainee to update
     * @return the updated {@link Trainee}
     */
    default Trainee update(TraineeRequest request, Trainee trainee) {
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

    /**
     * Converts a {@link Trainee} entity to a {@link TraineeProfile}.
     *
     * <p>This method transforms a {@link Trainee} entity into a {@link TraineeProfile} DTO,
     * including nested conversions for associated trainers and user details.</p>
     *
     * @param trainee the trainee to convert
     * @return the converted {@link TraineeProfile}
     */
    default BaseResponse toTraineeProfile(Trainee trainee) {
        return TraineeProfile.builder()
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .username(trainee.getUser().getUsername())
                .address(trainee.getAddress())
                .active(trainee.getUser().getActive())
                .dateOfBirth(trainee.getDateOfBirth())
                .trainers(trainee.getTrainers().stream()
                        .map(this::toTrainerProfile)
                        .toList())
                .build();
    }

    /**
     * Converts a {@link Training} entity to a {@link TrainingResponse}.
     *
     * <p>This method maps a {@link Training} entity to a {@link TrainingResponse} DTO,
     * including details such as trainer name, training type, and duration.</p>
     *
     * @param training the training to convert
     * @return the converted {@link TrainingResponse}
     */
    default TrainingResponse toResponse(Training training) {
        return TrainingResponse.builder()
                .trainerName(training.getTrainer().getUser().getUsername())
                .trainingName(training.getTrainingName())
                .trainingType(training.getType().getSpecialization().name())
                .trainingDuration(training.getTrainingDuration())
                .trainingDate(training.getTrainingDate())
                .build();
    }

    /**
     * Converts a list of {@link TrainerProfile} to a set of {@link Trainer} entities.
     *
     * <p>This method creates {@link Trainer} entities from a list of {@link TrainerProfile} DTOs,
     * setting properties based on the profile information.</p>
     *
     * @param trainers the list of trainer profiles
     * @return the set of {@link Trainer} entities
     */
    default Set<Trainer> toTrainers(List<SlimTrainerProfile> trainers) {
        return trainers.stream()
                .map(trainerProfile -> Trainer.builder()
                        .id(null)
                        .user(toUser(trainerProfile))
                        .specialization(trainerProfile.getSpecialization())
                        .build())
                .collect(Collectors.toSet());
    }

    /**
     * Converts a {@link TrainerProfile} to a {@link User} entity.
     *
     * <p>This method creates a {@link User} entity from a {@link TrainerProfile} DTO,
     * setting user-specific details.</p>
     *
     * @param profile the trainer profile to convert
     * @return the converted {@link User}
     */
    default User toUser(SlimTrainerProfile profile) {
        return User.builder()
                .username(profile.getUsername())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .active(profile.isActive())
                .build();
    }

    /**
     * Converts a {@link Trainer} entity to a {@link TrainerProfile} DTO.
     *
     * <p>This method transforms a {@link Trainer} entity into a {@link TrainerProfile} DTO,
     * including nested conversions for associated trainees.</p>
     *
     * @param trainer the trainer to convert
     * @return the converted {@link TrainerProfile}
     */
    default SlimTrainerProfile toTrainerProfile(Trainer trainer) {
        return TrainerProfile.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(trainer.getSpecialization())
                .active(trainer.getUser().getActive())
                .build();
    }

    /**
     * Converts a {@link User} and {@link TraineeRequest} to a {@link Trainee} entity.
     *
     * <p>This method constructs a {@link Trainee} entity using the provided {@link User} and {@link TraineeRequest}
     * DTO, setting properties accordingly.</p>
     *
     * @param user the user to associate with the trainee
     * @param dto  the request containing trainee details
     * @return the converted {@link Trainee}
     */
    default Trainee toTrainee(User user, TraineeRequest dto) {
        String address = dto.getAddress() != null ? dto.getAddress() : "";
        return Trainee.builder()
                .dateOfBirth(dto.getDateOfBirth())
                .address(address)
                .user(user).build();
    }

    /**
     * Converts a {@link Trainee} entity to a {@link TraineeProfile} DTO.
     *
     * @param trainee the {@link Trainee} entity to be converted.
     * @return a {@link TraineeProfile} DTO representing the trainee,
     * or {@code null} if the input entity is {@code null}.
     */
    static TraineeProfile toDto(Trainee trainee) {
        if (trainee == null) {
            return null;
        }

        return TraineeProfile.builder()
                .username(trainee.getUser().getUsername())
                .firstName(trainee.getUser().getFirstName() != null ? trainee.getUser().getFirstName() : "")
                .lastName(trainee.getUser().getLastName() != null ? trainee.getUser().getLastName() : "")
                .address(trainee.getAddress() != null ? trainee.getAddress() : "")
                .active(Boolean.TRUE.equals(trainee.getUser().getActive()))
                .build();
    }
}
