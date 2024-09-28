package com.epam.esm.gym.user.mapper;

import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse;
import com.epam.esm.gym.user.dto.trainee.PostTraineeRequest;
import com.epam.esm.gym.user.dto.trainee.PutTraineeRequest;
import com.epam.esm.gym.user.dto.trainee.TraineeProfileResponse;
import com.epam.esm.gym.user.dto.trainer.TrainerResponse;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.Training;
import com.epam.esm.gym.user.entity.TrainingType;
import com.epam.esm.gym.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper interface for converting between various DTOs and domain models related to trainees, trainers, and training.
 *
 * <p>This interface provides methods to map between entities and data transfer objects (DTOs),
 * {@link com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse}, {@link TrainingResponse},
 * and {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile}.
 * It uses the {@link UserMapper} and {@link TrainerMapper} components for mappings related to users and trainers.</p>
 */
@Service
public class TraineeMapper {

    /**
     * Converts a {@link User} object to a {@link com.epam.esm.gym.user.dto.profile.UserProfile}.
     *
     * <p>This method converts a {@link User} entity to a {@link com.epam.esm.gym.user.dto.profile.UserProfile} DTO.
     * It validates that the user and required fields are not null before creating the response object.</p>
     *
     * @param username the user to convert
     * @param password the user to convert
     * @return the converted {@link com.epam.esm.gym.user.dto.profile.UserProfile}
     * @throws IllegalArgumentException if user, username, or password is null
     */
    public ProfileResponse toProfile(String username, String password) {
        Objects.requireNonNull(username, "Username cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");
        return new ProfileResponse(username, password);
    }

    /**
     * Updates a {@link Trainee} entity with values from a {@link com.epam.esm.gym.user.dto.trainee.PutTraineeRequest}.
     *
     * <p>This method updates the fields of an existing {@link Trainee}
     * entity based on the provided {@link com.epam.esm.gym.user.dto.trainee.PutTraineeRequest}.
     * It selectively updates fields if the corresponding request values are not null.</p>
     *
     * @param request the request containing update values
     * @param trainee the trainee to update
     * @return the updated {@link Trainee}
     */
    public Trainee update(PutTraineeRequest request, Trainee trainee) {
        Objects.requireNonNull(request, "TraineeRequest cannot be null");
        Objects.requireNonNull(trainee, "Trainee cannot be null");
        User user = trainee.getUser();
        Objects.requireNonNull(user, "User cannot be null");

        if (request.username() != null) {
            user.setUsername(request.username());
        }

        if (request.firstName() != null) {
            user.setFirstName(request.firstName());
        }

        if (request.lastName() != null) {
            user.setLastName(request.lastName());
        }

        if (request.dateOfBirth() != null) {
            trainee.setDateOfBirth(request.dateOfBirth());
        }

        if (request.address() != null) {
            trainee.setAddress(request.address());
        }

        if (request.active() != null) {
            user.setActive(request.active());
        }

        trainee.setUser(user);
        return trainee;
    }

    /**
     * Converts a {@link Trainee} entity to a {@link com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse}.
     *
     * <p>This method transforms a {@link Trainee} entity
     * into a {@link com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse} DTO,
     * including nested conversions for associated trainers and user details.</p>
     *
     * @param trainee the trainee to convert
     * @return the converted {@link com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse}
     */
    public FullTraineeProfileResponse toTraineeProfile(Trainee trainee) {
        Objects.requireNonNull(trainee, "Trainee cannot be null");

        return new FullTraineeProfileResponse(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getUser().getUsername(),
                trainee.getAddress(),
                trainee.getUser().getActive(),
                trainee.getDateOfBirth(),
                trainee.getTrainers() == null
                        ? Collections.emptyList()
                        : trainee.getTrainers().stream().map(this::toTrainerProfile).toList()
        );
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
    public TrainingResponse toResponse(Training training) {
        Objects.requireNonNull(training, "Training cannot be null");

        return TrainingResponse.builder()
                .trainerName(training.getTrainer().getUser().getUsername())
                .trainingName(training.getTrainingName())
                .trainingType(training.getType().getSpecialization().name())
                .trainingDuration(training.getTrainingDuration())
                .trainingDate(training.getTrainingDate())
                .build();
    }

    /**
     * Converts a list of {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} to a set of {@link Trainer} entities.
     *
     * <p>This method creates {@link Trainer} entities from a list
     * of {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} DTOs,
     * setting properties based on the profile information.</p>
     *
     * @param trainers the list of trainer profiles
     * @return the set of {@link Trainer} entities
     */
    public Set<Trainer> toTrainers(List<TrainerResponse> trainers) {
        if (trainers == null || trainers.isEmpty()) {
            return new HashSet<>();
        }

        return trainers.stream()
                .map(trainerProfile -> Trainer.builder()
                        .user(toUser(trainerProfile))
                        .trainingType(TrainingType.builder()
                                .specialization(trainerProfile.getSpecialization().getSpecialization())
                                .build())
                        .build())
                .collect(Collectors.toSet());
    }

    /**
     * Converts a {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} to a {@link User} entity.
     *
     * <p>This method creates a {@link User} entity from a {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} DTO,
     * setting user-specific details.</p>
     *
     * @param profile the trainer profile to convert
     * @return the converted {@link User}
     */
    public User toUser(TrainerResponse profile) {
        Objects.requireNonNull(profile, "Trainer Response cannot be null");

        return User.builder()
                .username(profile.getUsername())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .build();
    }

    /**
     * Converts a {@link Trainer} entity to a {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} DTO.
     *
     * <p>This method transforms a {@link Trainer} entity into a
     * {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile},
     * including nested conversions for associated trainees.</p>
     *
     * @param trainer the trainer to convert
     * @return the converted {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile}
     */
    public TrainerResponse toTrainerProfile(Trainer trainer) {
        Objects.requireNonNull(trainer, "Trainer cannot be null");

        return TrainerResponse.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(TrainingTypeDto.builder()
                        .specialization(trainer.getTrainingType().getSpecialization())
                        .build())
                .build();
    }

    /**
     * Converts a {@link User} and {@link com.epam.esm.gym.user.dto.trainee.PutTraineeRequest}
     * to a {@link Trainee} entity. This method constructs a {@link Trainee} entity using the provided {@link User}
     * and {@link com.epam.esm.gym.user.dto.trainee.PutTraineeRequest} DTO, setting properties accordingly.
     *
     * @param user the user to associate with the trainee
     * @param dto  the request containing trainee details
     * @return the converted {@link Trainee}
     */
    public Trainee toTrainee(User user, PostTraineeRequest dto) {
        Objects.requireNonNull(dto, "PostTrainee Request cannot be null");
        Objects.requireNonNull(user, "User cannot be null");
        String address = dto.address() != null ? dto.address() : "";
        return Trainee.builder()
                .dateOfBirth(dto.dateOfBirth())
                .address(address)
                .user(user).build();
    }

    /**
     * Converts a {@link Trainee} entity to
     * a {@link com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse} DTO.
     *
     * @param trainee the {@link Trainee} entity to be converted.
     * @return a {@link com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse}
     * DTO representing the trainee, or {@code null} if the input entity is {@code null}.
     */
    static TraineeProfileResponse toSlimDto(Trainee trainee) {
        Objects.requireNonNull(trainee, "Trainee cannot be null");
        return TraineeProfileResponse.builder()
                .username(trainee.getUser().getUsername())
                .firstName(trainee.getUser().getFirstName() != null ? trainee.getUser().getFirstName() : "")
                .lastName(trainee.getUser().getLastName() != null ? trainee.getUser().getLastName() : "")
                .address(trainee.getAddress() != null ? trainee.getAddress() : "")
                .active(Boolean.TRUE.equals(trainee.getUser().getActive()))
                .build();
    }
}
