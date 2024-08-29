package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.mapper.UserMapper;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class UserProfileService implements UserService {
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int PASSWORD_LENGTH = 10;
    private final Random random = new SecureRandom();
    private final UserMapper mapper;
    private final UserDao dao;

    @Override
    @Transactional
    public TraineeProfile saveTrainee(TraineeRequest dto) {
        String username = generateUsername(dto.getFirstName(), dto.getLastName());
        String password = generateRandomPassword();
        User user = mapper.toUser(dto.getFirstName(), dto.getLastName(), username, password);
        User save = dao.save(user);
        return mapper.toTraineeProfile(save);
    }


    @Override
    @Transactional
    public TrainerProfile saveTrainer(TrainerRequest dto) {
        String username = generateUsername(dto.getFirstName(), dto.getLastName());
        String password = generateRandomPassword();
        User user = mapper.toUser(dto.getFirstName(), dto.getLastName(), username, password);
        User save = dao.save(user);
        return mapper.toTrainerProfile(save);
    }

    @Override
    public void updateUser(User user) {
        dao.update(user);
    }

    @Override
    public void deleteUser(String username) {
        dao.delete(getUser(username));
    }

    @Override
    public UserProfile getUserByUsername(String username) {
        return mapper.toDto(getUser(username));
    }

    private User getUser(String username) {
        return dao.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(username));
    }

    @Override
    public void activateUser(String username) {
        User user = getUser(username);
        user.setActive(true);
        dao.save(user);
    }

    @Override
    public void deactivateUser(String username) {
        User user = getUser(username);
        user.setActive(false);
        dao.save(user);
    }

    @Override
    public ResponseEntity<MessageResponse> changePassword(ProfileRequest request) {
        Optional<User> userOptional = dao.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) {
            return getResponseEntity(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        if (!(request.getPassword().equals(user.getPassword()))) {
            return getResponseEntity(HttpStatus.PAYMENT_REQUIRED);
        }

        if (request.getNewPassword().equals(request.getPassword())) {
            return getResponseEntity(HttpStatus.BAD_REQUEST);
        }

        user.setPassword(request.getNewPassword());
        updateUser(user);
        return getResponseEntity(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<MessageResponse> authenticate(String username, String password) {
        Optional<User> userOptional = dao.findByUsername(username);
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        if (userOptional.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
        } else if (validateUser(userOptional.get(), username, password)) {
            status = HttpStatus.OK;
        }
        return getResponseEntity(status);
    }

    private ResponseEntity<MessageResponse> getResponseEntity(HttpStatus status) {
        String message = switch (status) {
            case OK -> "Authentication successful";
            case ACCEPTED -> "Password updated successfully";
            case NOT_FOUND -> "User not found";
            case PAYMENT_REQUIRED -> "Old password is incorrect";
            case BAD_REQUEST -> "New password is the same as the old password";
            default -> "Invalid credentials";
        };
        return ResponseEntity.status(status).body(new MessageResponse(message, status));
    }

    private boolean validateUser(User user, String username, String password) {
        return user.getPassword().equals(password) && user.getUsername().equals(username);
    }

    private String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;
        while (dao.existsByUsername(username)) {
            username = baseUsername + "." + suffix++;
        }
        return username;
    }

    private String generateRandomPassword() {
        return IntStream.range(0, PASSWORD_LENGTH)
                .mapToObj(i -> String.valueOf(ALPHANUMERIC_STRING.charAt(
                        random.nextInt(ALPHANUMERIC_STRING.length()))))
                .collect(Collectors.joining());
    }
}
