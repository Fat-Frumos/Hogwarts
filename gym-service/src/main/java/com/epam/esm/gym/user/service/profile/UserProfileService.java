package com.epam.esm.gym.user.service.profile;

import com.epam.esm.gym.user.dao.JpaUserDao;
import com.epam.esm.gym.user.entity.RoleType;
import com.epam.esm.gym.user.entity.User;
import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileRequest;
import com.epam.esm.gym.user.dto.profile.UserResponse;
import com.epam.esm.gym.user.dto.trainee.PostTraineeRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerRequest;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.mapper.UserMapper;
import com.epam.esm.gym.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.epam.esm.gym.user.entity.RoleType.ROLE_TRAINEE;

/**
 * Service for managing user profiles, including operations for saving, updating, deleting,
 * and authenticating users. This class handles both trainee and trainer users, generating
 * usernames and passwords, and ensuring secure password management.
 * <p>
 * The service uses a password encoder for hashing passwords, a mapper for converting between
 * entities and DTOs, and a DAO for database operations. It includes methods for activating
 * and deactivating users, as well as for password changes and authentication.
 * </p>
 */
@Service
@AllArgsConstructor
public class UserProfileService implements UserService {
    public static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int PASSWORD_LENGTH = 10;
    private final Random random = new SecureRandom();
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final JpaUserDao dao;

    /**
     * {@inheritDoc}
     * Registers a new trainee user by generating a username and a random password, encoding the password,
     * and saving the user. Returns the saved user with the raw password set for further use.
     *
     * @param dto The {@link com.epam.esm.gym.user.dto.trainee.PutTraineeRequest} containing the trainee's details.
     * @return The saved {@link User} entity with the raw password set.
     */
    @Override
    @Transactional
    public User createTraineeUser(PostTraineeRequest dto, String password) {
        String username = generateUsername(dto.firstName(), dto.lastName());
        return mapper.toUser(dto.firstName(), dto.lastName(), username, password, ROLE_TRAINEE);
    }

    /**
     * {@inheritDoc}
     * Registers a new trainer user by generating a username and a random password, encoding the password,
     * and saving the user. Returns a {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} based on the saved user.
     *
     * @param dto The {@link TrainerRequest} containing the trainer's details.
     * @return The {@link com.epam.esm.gym.user.dto.trainer.TrainerProfile} of the saved trainer.
     */

    @Override
    @Transactional
    public User createTrainerUser(TrainerRequest dto, String password) {
        String username = generateUsername(dto.getFirstName(), dto.getLastName());
        return mapper.toUser(dto.getFirstName(), dto.getLastName(), username, password, RoleType.ROLE_TRAINER);
    }

    /**
     * {@inheritDoc}
     * Updates the given user entity in the database.
     *
     * @param user The {@link User} entity to be updated.
     */
    @Override
    public void updateUser(User user) {
        dao.save(user);
    }

    /**
     * {@inheritDoc}
     * Deletes the user identified by the given username.
     *
     * @param username The username of the user to be deleted.
     */
    @Override
    @Transactional
    public ResponseEntity<Void> deleteUser(String username) {
        User user = getUser(username);
        dao.delete(user);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@inheritDoc}
     * Retrieves the user profile for the given username.
     *
     * @param username The username of the user whose profile is to be retrieved.
     * @return The {@link com.epam.esm.gym.user.dto.profile.UserResponse} associated with the given username.
     */
    @Override
    public UserResponse getUserByUsername(String username) {
        return mapper.toDto(getUser(username));
    }

    /**
     * Retrieves the {@link User} entity for the given username.
     *
     * @param username The username of the user to be retrieved.
     * @return The {@link User} entity.
     * @throws UserNotFoundException If no user with the given username is found.
     */
    @Override
    public User getUser(String username) {
        return dao.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User not found by name: " + username));
    }

    /**
     * {@inheritDoc}
     * Changes the user's password based on the provided {@link ProfileRequest}.
     * If the current password matches, and the new password is different, it updates the password.
     *
     * @param request The {@link ProfileRequest} containing the old and new passwords.
     * @return A {@link ResponseEntity} indicating the result of the password change operation.
     */
    @Override
    public MessageResponse changePassword(ProfileRequest request) {
        Optional<User> userOptional = dao.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) {
            return getResponseEntity(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return getResponseEntity(HttpStatus.PAYMENT_REQUIRED);
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            return getResponseEntity(HttpStatus.BAD_REQUEST);
        }

        user.setPassword(encodePassword(request.getNewPassword()));
        updateUser(user);
        return getResponseEntity(HttpStatus.ACCEPTED);
    }

    /**
     * {@inheritDoc}
     * Authenticates the user by checking the username and password.
     * Returns an appropriate HTTP status based on the authentication result.
     *
     * @param username The username of the user attempting to authenticate.
     * @param password The password of the user attempting to authenticate.
     * @return A {@link MessageResponse} indicating the authentication result.
     */
    @Override
    public MessageResponse authenticate(String username, String password) {
        Optional<User> userOptional = dao.findByUsername(username);
        HttpStatus status;
        if (userOptional.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
        } else {
            User user = userOptional.get();
            if (validateUser(user, username, password)) {
                user.setActive(true);
                dao.save(user);
                status = HttpStatus.OK;
            } else {
                status = HttpStatus.UNAUTHORIZED;
            }
        }
        return getResponseEntity(status);
    }

    /**
     * Generates a response entity with a message based on the provided HTTP status.
     *
     * @param status The HTTP status to be used in the response.
     * @return A {@link ResponseEntity} with a message corresponding to the HTTP status.
     */
    private MessageResponse getResponseEntity(HttpStatus status) {
        String message = switch (status) {
            case OK -> "Authentication successful";
            case ACCEPTED -> "Password updated successfully";
            case NOT_FOUND -> "User not found";
            case PAYMENT_REQUIRED -> "Old password is incorrect";
            case BAD_REQUEST -> "New password is the same as the old password";
            default -> "Invalid data";
        };
        return new MessageResponse(message);
    }

    /**
     * Validates the user credentials against the provided username and password.
     *
     * @param user     The {@link User} entity to be validated.
     * @param username The username to check.
     * @param password The password to check.
     * @return True if the credentials are valid; otherwise, false.
     */
    private boolean validateUser(User user, String username, String password) {
        return passwordEncoder.matches(password, user.getPassword())
                && user.getUsername().equals(username);
    }

    /**
     * Generates a unique username based on the user's first and last name.
     * If a username already exists, a numerical suffix is appended to create a unique username.
     *
     * @param firstName The user's first name.
     * @param lastName  The user's last name.
     * @return A unique username.
     */
    private String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        int suffix = dao.findUsersByUsername(baseUsername).stream()
                .map(username -> username.getUsername().replace(baseUsername + ".", ""))
                .filter(s -> s.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;
        return baseUsername + "." + suffix;
    }

    /**
     * Generates a random password with a fixed length using alphanumeric characters.
     *
     * @return A random password.
     */
    @Override
    public String generateRandomPassword() {
        return IntStream.range(0, PASSWORD_LENGTH)
                .mapToObj(i -> String.valueOf(ALPHANUMERIC_STRING.charAt(
                        random.nextInt(ALPHANUMERIC_STRING.length()))))
                .collect(Collectors.joining());
    }

    /**
     * {@inheritDoc}
     * Encodes a plain-text password.
     */
    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * {@inheritDoc}
     * Saves a {@link User} entity.
     */
    @Override
    @Transactional
    public User saveUser(User user) {
        return dao.save(user);
    }

    @Override
    public List<UserResponse> findAll() {
        return dao.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    public List<UserResponse> findAllTraineeByName(List<String> usernames) {
        return dao.findAllTraineesByUsername(ROLE_TRAINEE, usernames).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
