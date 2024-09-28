package com.epam.esm.gym.user.mapper;

import com.epam.esm.gym.jms.dto.TrainerStatus;
import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.trainee.TraineeProfileResponse;
import com.epam.esm.gym.user.dto.trainer.UpdateTrainerRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import com.epam.esm.gym.user.dto.trainer.TrainerRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerResponseDto;
import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.TrainingType;
import com.epam.esm.gym.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper interface for converting between Trainer entities and related DTOs.
 *
 * <p>This interface includes methods to map {@link Trainer} entities
 * to {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} DTOs and vice versa, as well as methods to convert
 * {@link User} to {@link com.epam.esm.gym.user.dto.profile.UserProfile} and handle collections of trainers.</p>
 */
@Service
public class TrainerMapper {
    /**
     * Converts a {@link Trainer} entity to a {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} DTO.
     *
     * <p>This method maps the details of a {@link Trainer} entity
     * to a {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} DTO.</p>
     *
     * @param trainer the trainer to convert
     * @return the converted {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile}
     */
    public TrainerProfile toDto(Trainer trainer) {
        Objects.requireNonNull(trainer, "Trainer cannot be null");

        List<TraineeProfileResponse> traineeProfiles = trainer.getTrainees()
                .stream()
                .map(trainee -> TraineeProfileResponse.builder()
                        .firstName(trainee.getUser().getFirstName())
                        .lastName(trainee.getUser().getLastName())
                        .username(trainee.getUser().getUsername())
                        .address(trainee.getAddress())
                        .active(trainee.getUser().getActive())
                        .dateOfBirth(trainee.getDateOfBirth())
                        .build())
                .collect(Collectors.toList());

        return TrainerProfile.builder()
                .trainees(traineeProfiles)
                .trainings(TrainingMapper.toResponses(trainer.getTrainings()))
                .trainerStatus(trainer.getUser().getActive() ? TrainerStatus.ACTIVE : TrainerStatus.INACTIVE)
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(TrainingTypeDto.builder()
                        .specialization(trainer.getTrainingType().getSpecialization())
                        .build())
                .build();
    }

    /**
     * Converts a {@link com.epam.esm.gym.user.dto.trainer.UpdateTrainerRequest} DTO to a {@link Trainer} entity.
     *
     * <p>The method creates a {@link Trainer} entity
     * from a {@link com.epam.esm.gym.user.dto.trainer.UpdateTrainerRequest} DTO.
     * The password field is ignored during conversion.</p>
     *
     * @param request the update request containing trainer details
     * @return the converted {@link Trainer}
     */
    public Trainer toEntity(UpdateTrainerRequest request, Trainer trainer) {
        Objects.requireNonNull(request, "PutTrainerRequest cannot be null");
        Objects.requireNonNull(trainer, "Trainer cannot be null");
        User user = trainer.getUser();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setActive(request.getActive());
        trainer.setUser(user);
        return trainer;
    }

    /**
     * Converts a {@link User} to a {@link com.epam.esm.gym.user.dto.profile.UserProfile} DTO.
     *
     * <p>This method maps user details to a {@link com.epam.esm.gym.user.dto.profile.UserProfile} DTO,
     * which is used for exposing user profile information.</p>
     *
     * @param username the username to convert
     * @param password the password to convert
     * @return the converted {@link com.epam.esm.gym.user.dto.profile.UserProfile}
     */
    public ProfileResponse toProfileDto(String username, String password) {
        Objects.requireNonNull(username, "Username cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");
        return new ProfileResponse(username, password);
    }

    /**
     * Converts a list of {@link Trainer} entities
     * to a list of {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile}.
     *
     * <p>This method maps a list of {@link Trainer} entities
     * to a list of {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} DTOs.</p>
     *
     * @param trainers the list of trainers to convert
     * @return the list of converted {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} DTOs
     */
    public List<TrainerProfile> toTrainerProfiles(List<Trainer> trainers) {
        if (trainers == null || trainers.isEmpty()) {
            return Collections.emptyList();
        }
        return trainers.stream().map(trainer -> TrainerProfile.builder()
                        .username(trainer.getUser().getUsername())
                        .trainerStatus(trainer.getUser().getActive() ? TrainerStatus.ACTIVE : TrainerStatus.INACTIVE)
                        .firstName(trainer.getUser().getFirstName())
                        .lastName(trainer.getUser().getLastName())
                        .trainings(TrainingMapper.toResponses(trainer.getTrainings()))
                        .specialization(TrainingTypeDto.builder().specialization(
                                trainer.getTrainingType().getSpecialization()).build())
                        .trainees(trainer.getTrainees() != null
                                ? trainer.getTrainees().stream()
                                .map(TraineeMapper::toSlimDto)
                                .toList()
                                : Collections.emptyList()).build())
                .collect(Collectors.toList());
    }

    /**
     * Converts a {@link User} and {@link TrainerRequest} into a {@link Trainer} entity.
     *
     * <p>This method maps the provided {@link User} and {@link TrainerRequest} to a {@link Trainer} entity.
     * It uses the data from the {@code TrainerRequest} to set the specialization of the trainer and associates
     * the provided {@code User} with the trainer. The specialization is set based on the {@code Specialization}
     * enum value derived from the request.</p>
     *
     * @param user the {@link User} entity to be associated with the trainer.
     * @return a {@link Trainer} entity with the specified user and specialization.
     */
    public Trainer toTrainer(User user, TrainingType trainingType) {
        Objects.requireNonNull(user, "User cannot be null");
        if (trainingType == null) {
            trainingType = TrainingType.builder().specialization(Specialization.DEFAULT).build();
        }

        return Trainer.builder()
                .trainingType(trainingType)
                .user(user).build();
    }

    /**
     * Converts a {@link Trainer} entity to a {@link TrainerResponseDto}.
     *
     * <p>This method extracts the relevant details from the provided {@link Trainer} object
     * and maps them to a {@link TrainerResponseDto}.
     * It assumes that the trainer has at least one associated training type to retrieve the specialization from.</p>
     *
     * @param trainer the {@link Trainer} entity to convert. Must not be {@code null}.
     * @return a {@link TrainerResponseDto} containing the trainer's username, firstname, lastname, and specialization.
     * @throws IllegalArgumentException if the {@code trainer} is {@code null}
     *                                  or if the trainer has no associated training types.
     */
    public TrainerResponseDto toResponseDto(Trainer trainer) {
        Objects.requireNonNull(trainer, "Trainer cannot be null");

        return TrainerResponseDto.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(trainer.getTrainingType()
                        .getSpecialization().name())
                .build();
    }
}
