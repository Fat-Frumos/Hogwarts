package com.epam.esm.gym.service;

import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.LoginRequest;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;

public interface UserService {

    TrainerProfile saveTrainer(TrainerRequest dto);

    void updateUser(User user);

    void deleteUser(String username);

    UserProfile getUserByUsername(String username);

    void changePassword(ProfileRequest user);

    void activateUser(String username);

    void deactivateUser(String username);

    void authenticate(LoginRequest request);

    TraineeProfile saveTrainee(TraineeRequest dto);
}
