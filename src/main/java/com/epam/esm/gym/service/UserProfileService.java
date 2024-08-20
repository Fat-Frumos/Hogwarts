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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        UserProfile profile = createUser(dto, username, password);
        User user = mapper.toEntity(profile);
        User save = dao.save(user);
        return mapper.toTraineeProfile(save);
    }


    @Override
    @Transactional
    public TrainerProfile saveTrainer(TrainerRequest dto) {
        String username = generateUsername(dto.getFirstName(), dto.getLastName());
        String password = generateRandomPassword();

        UserProfile profile = createUser(dto, username, password);
        User user = mapper.toEntity(profile);
        User save = dao.save(user);
        return mapper.toTrainerProfile(save);
    }

    @Override
    public void updateUser(UserProfile userProfile) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public UserProfile getUserByUsername(String username) {
        return null;
    }

    @Override
    public void changePassword(ProfileRequest user) {

    }

    @Override
    public void activateUser(String username) {

    }

    @Override
    public void deactivateUser(String username) {

    }

    @Override
    public void authenticate(LoginRequest request) {
    }

    private UserProfile createUser(TrainerRequest dto, String username, String password) {
        return UserProfile.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(username)
                .password(password)
                .active(true)
                .build();
    }

    private UserProfile createUser(TraineeRequest dto, String username, String password) {
        return UserProfile.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(username)
                .password(password)
                .active(true)
                .build();
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
