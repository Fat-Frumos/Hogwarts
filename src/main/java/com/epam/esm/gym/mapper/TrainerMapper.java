package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.trainer.TrainerResponseDto;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.SlimTraineeProfileResponse;
import com.epam.esm.gym.dto.trainer.PutTrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper interface for converting between Trainer entities and related DTOs.
 *
 * <p>This interface includes methods to map {@link Trainer} entities
 * to {@link com.epam.esm.gym.dto.trainer.TrainerProfile} DTOs and vice versa,
 * as well as methods to convert {@link User} to {@link ProfileResponse} and handle collections of trainers.</p>
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TrainerMapper {
    /**
     * Converts a {@link Trainer} entity to a {@link com.epam.esm.gym.dto.trainer.TrainerProfile} DTO.
     *
     * <p>This method maps the details of a {@link Trainer} entity
     * to a {@link com.epam.esm.gym.dto.trainer.TrainerProfile} DTO.</p>
     *
     * @param trainer the trainer to convert
     * @return the converted {@link com.epam.esm.gym.dto.trainer.TrainerProfile}
     */
    default BaseResponse toDto(Trainer trainer) {
        if (trainer == null) {
            return null;
        }

        List<SlimTraineeProfileResponse> traineeProfiles = trainer.getTrainees()
                .stream()
                .map(trainee -> SlimTraineeProfileResponse.builder()
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
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specializations(TrainingMapper.toTypesDto(trainer.getTrainingTypes()))
                .build();
    }

    /**
     * Converts a {@link com.epam.esm.gym.dto.trainer.PutTrainerRequest} DTO to a {@link Trainer} entity.
     *
     * <p>The method creates a {@link Trainer} entity from a {@link com.epam.esm.gym.dto.trainer.PutTrainerRequest} DTO.
     * The password field is ignored during conversion.</p>
     *
     * @param request the update request containing trainer details
     * @return the converted {@link Trainer}
     */
    default Trainer toEntity(PutTrainerRequest request, Trainer trainer) {
        if (request == null || trainer == null) {
            return null;
        }
        User user = trainer.getUser();

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }
        trainer.setUser(user);
        return trainer;
    }

    /**
     * Converts a {@link User} to a {@link ProfileResponse} DTO.
     *
     * <p>This method maps user details to a {@link ProfileResponse} DTO,
     * which is used for exposing user profile information.</p>
     *
     * @param username the username to convert
     * @param password the password to convert
     * @return the converted {@link ProfileResponse}
     */
    default ProfileResponse toProfileDto(String username, String password) {
        return ProfileResponse.builder().username(username).password(password).build();
    }

    /**
     * Converts a list of {@link Trainer} entities to a list of {@link com.epam.esm.gym.dto.trainer.TrainerProfile}.
     *
     * <p>This method maps a list of {@link Trainer} entities
     * to a list of {@link com.epam.esm.gym.dto.trainer.TrainerProfile} DTOs.</p>
     *
     * @param trainers the list of trainers to convert
     * @return the list of converted {@link com.epam.esm.gym.dto.trainer.TrainerProfile} DTOs
     */
    default List<TrainerProfile> toTrainerProfiles(List<Trainer> trainers) {
        if (trainers == null || trainers.isEmpty()) {
            return Collections.emptyList();
        }
        return trainers.stream().map(trainer -> TrainerProfile.builder()
                        .username(trainer.getUser().getUsername())
                        .firstName(trainer.getUser().getFirstName())
                        .lastName(trainer.getUser().getLastName())
                        .specializations(TrainingMapper.toTypesDto(trainer.getTrainingTypes()))
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
    default Trainer toTrainer(User user, List<TrainingType> trainingTypes) {
        return Trainer.builder()
                .trainingTypes(trainingTypes)
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
    default TrainerResponseDto toResponseDto(Trainer trainer) {
        return TrainerResponseDto.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(trainer.getTrainingTypes().get(0).getSpecialization().name())
                .build();
    }
}
