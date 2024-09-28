package com.epam.esm.gym.user.mapper;

import com.epam.esm.gym.user.dto.profile.UserResponse;
import com.epam.esm.gym.user.entity.RoleType;
import com.epam.esm.gym.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Mapper interface for converting between User entities and related DTOs.
 *
 * <p>This interface includes methods for mapping {@link User} entities
 * to {@link com.epam.esm.gym.user.dto.profile.UserResponse}
 * and {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} DTOs,
 * as well as creating a {@link User} entity from basic user data.</p>
 */
@Service
public class UserMapper {
    /**
     * Converts a {@link User} entity to a {@link com.epam.esm.gym.user.dto.profile.UserResponse} DTO.
     *
     * <p>This method maps the details of a {@link User} entity
     * to a {@link com.epam.esm.gym.user.dto.profile.UserResponse} DTO.</p>
     *
     * @param user the user entity to convert
     * @return the converted {@link com.epam.esm.gym.user.dto.profile.UserResponse}
     */
    public UserResponse toDto(User user) {
        Objects.requireNonNull(user, "User cannot be null");

        return UserResponse.builder()
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
    public User toUser(
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
