package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.LoginRequest;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.mapper.UserMapper;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        return dao.findByUserName(username).orElseThrow(
                () -> new EntityNotFoundException(username));
    }

    @Override
    public void changePassword(ProfileRequest request) {
        User user = getUser(request.getUsername());
        log.info(user.toString());
        if (!(request.getPassword().equals(user.getPassword()))) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }

        if (!request.getNewPassword().equals(request.getPassword())) {
            user.setPassword(request.getNewPassword());
            updateUser(user);
        } else {
            throw new IllegalArgumentException("New password the same as old password .");
        }

    }

    @Override
    public void activateUser(String username) {
        User user = dao.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setActive(true);
        dao.save(user);
    }


    @Override
    public void deactivateUser(String username) {
        User user = dao.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setActive(false);
        dao.save(user);
    }

    @Override
    public void authenticate(LoginRequest request) {
        log.info(request.toString());
        User user = dao.findByUserName(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        log.info(user.toString());
        if (!(request.getPassword().equals(user.getPassword()))) {
            throw new IllegalArgumentException("Invalid username or password");
        }
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
