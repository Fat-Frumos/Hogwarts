package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper interface for converting between Trainer entities and related DTOs.
 *
 * <p>This interface includes methods to map {@link Trainer} entities to {@link TrainerProfile} DTOs and vice versa,
 * as well as methods to convert {@link User} to {@link ProfileResponse} and handle collections of trainers.</p>
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TrainerMapper {
    /**
     * Converts a {@link Trainer} entity to a {@link TrainerProfile} DTO.
     *
     * <p>This method maps the details of a {@link Trainer} entity to a {@link TrainerProfile} DTO.</p>
     *
     * @param trainer the trainer to convert
     * @return the converted {@link TrainerProfile}
     */
    default TrainerProfile toDto(Trainer trainer) {
        if (trainer == null) {
            return null;
        }

        List<TraineeProfile> traineeProfiles = trainer.getTrainees().stream()
                .map(trainee -> TraineeProfile.builder()
                        .firstName(trainee.getUser().getFirstName())
                        .lastName(trainee.getUser().getLastName())
                        .username(trainee.getUser().getUsername())
                        .address(trainee.getAddress())
                        .active(trainee.getUser().getActive())
                        .dateOfBirth(trainee.getDateOfBirth())
                        .trainers(null)
                        .build())
                .collect(Collectors.toList());

        return TrainerProfile.builder()
                .trainees(traineeProfiles)
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(trainer.getSpecialization())
                .active(trainer.getUser().getActive())
                .build();
    }

    /**
     * Converts a {@link TrainerUpdateRequest} DTO to a {@link Trainer} entity.
     *
     * <p>This method creates a {@link Trainer} entity from a {@link TrainerUpdateRequest} DTO.
     * The password field is ignored during conversion.</p>
     *
     * @param dto the update request containing trainer details
     * @return the converted {@link Trainer}
     */
    default Trainer toEntity(TrainerUpdateRequest dto) {
        if (dto == null) {
            return null;
        }

        return Trainer.builder()
                .user(User.builder()
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .active(dto.getActive())
                        .build())
                .build();
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
     * Converts a list of {@link Trainer} entities to a list of {@link TrainerProfile} DTOs.
     *
     * <p>This method maps a list of {@link Trainer} entities to a list of {@link TrainerProfile} DTOs.</p>
     *
     * @param trainers the list of trainers to convert
     * @return the list of converted {@link TrainerProfile} DTOs
     */
    default List<TrainerProfile> toTrainerProfiles(List<Trainer> trainers) {
        if (trainers == null || trainers.isEmpty()) {
            return Collections.emptyList();
        }
        return trainers.stream().map(trainer -> TrainerProfile.builder()
                        .username(trainer.getUser().getUsername())
                        .firstName(trainer.getUser().getFirstName() != null ? trainer.getUser().getFirstName() : "")
                        .lastName(trainer.getUser().getLastName() != null ? trainer.getUser().getLastName() : "")
                        .active(Boolean.TRUE.equals(trainer.getUser().getActive()))
                        .specialization(trainer.getSpecialization())
                        .trainees(trainer.getTrainees() != null ? trainer.getTrainees().stream()
                                .map(TraineeMapper::toDto)
                                .toList() : Collections.emptyList())
                        .build())
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
     * @param dto  the {@link TrainerRequest} containing the details needed to create the trainer.
     * @return a {@link Trainer} entity with the specified user and specialization.
     */
    default Trainer toTrainer(User user, TrainerRequest dto) {
        return Trainer.builder()
                .specialization(TrainingType.builder()
                        .specialization(Specialization.valueOf(dto.getSpecialization()))
                        .build())
                .user(user).build();
    }
}
