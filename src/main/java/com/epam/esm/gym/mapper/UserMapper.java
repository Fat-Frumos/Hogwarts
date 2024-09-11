package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.UserProfile;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between User entities and related DTOs.
 *
 * <p>This interface includes methods for mapping {@link User} entities
 * to {@link UserProfile} and {@link com.epam.esm.gym.dto.trainer.TrainerProfile} DTOs,
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
    static UserProfile toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserProfile.builder()
                .id(user.getId().longValue())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
                .active(user.getActive())
                .build();
    }

    /**
     * Creates a {@link User} entity from provided user data.
     *
     * <p>This method builds a {@link User} entity using basic user details such as
     * first name, last name, username, password, and role.</p>
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param username  the username of the user
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
                .password(password)
                .username(username)
                .active(false)
                .build();
    }
}
