package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

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
    TrainerProfile toDto(Trainer trainer);

    /**
     * Converts a {@link TrainerProfile} DTO to a {@link Trainer} entity.
     *
     * <p>This method creates a {@link Trainer} entity from a {@link TrainerProfile} DTO.
     * The password field is ignored during conversion.</p>
     *
     * @param profile the trainer profile to convert
     * @return the converted {@link Trainer}
     */
    @Mapping(target = "password", ignore = true)
    Trainer toEntity(TrainerProfile profile);

    /**
     * Converts a {@link TrainerUpdateRequest} DTO to a {@link Trainer} entity.
     *
     * <p>This method creates a {@link Trainer} entity from a {@link TrainerUpdateRequest} DTO.
     * The password field is ignored during conversion.</p>
     *
     * @param dto the update request containing trainer details
     * @return the converted {@link Trainer}
     */
    @Mapping(target = "password", ignore = true)
    Trainer toEntity(TrainerUpdateRequest dto);

    /**
     * Converts a {@link User} to a {@link ProfileResponse} DTO.
     *
     * <p>This method maps user details to a {@link ProfileResponse} DTO,
     * which is used for exposing user profile information.</p>
     *
     * @param user the user to convert
     * @return the converted {@link ProfileResponse}
     */
    ProfileResponse toProfileDto(User user);

    /**
     * Converts a list of {@link Trainer} entities to a list of {@link TrainerProfile} DTOs.
     *
     * <p>This method maps a list of {@link Trainer} entities to a list of {@link TrainerProfile} DTOs.</p>
     *
     * @param notAssigned the list of trainers to convert
     * @return the list of converted {@link TrainerProfile} DTOs
     */
    List<TrainerProfile> toDtos(List<Trainer> notAssigned);
}
