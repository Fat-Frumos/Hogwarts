package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between User entities and related DTOs.
 *
 * <p>This interface includes methods for mapping {@link User} entities
 * to {@link UserProfile} and {@link TrainerProfile} DTOs,
 * as well as creating a {@link User} entity from basic user data.</p>
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Converts a {@link User} entity to a {@link UserProfile} DTO.
     *
     * <p>This method maps the details of a {@link User} entity to a {@link UserProfile} DTO.</p>
     *
     * @param user the user entity to convert
     * @return the converted {@link UserProfile}
     */
    UserProfile toDto(User user);

    /**
     * Converts a {@link User} entity to a {@link TrainerProfile} DTO.
     *
     * <p>This method maps the details of a {@link User} entity to a {@link TrainerProfile} DTO.</p>
     *
     * @param user the user entity to convert
     * @return the converted {@link TrainerProfile}
     */
    TrainerProfile toTrainerProfile(User user);

    /**
     * Creates a {@link User} entity from provided user data.
     *
     * <p>This method builds a {@link User} entity using basic user details such as
     * first name, last name, username, password, and role.</p>
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param username  the username of the user
     * @param password  the password of the user
     * @param role      the role of the user
     * @return the created {@link User} entity
     */
    default User toUser(
            String firstName,
            String lastName,
            String username,
            String password,
            RoleType role) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .permission(role)
                .username(username)
                .password(password)
                .active(true)
                .build();
    }
}
