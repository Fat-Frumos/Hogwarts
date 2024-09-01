package com.epam.esm.gym.service;

import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    TrainerProfile saveTrainer(TrainerRequest dto);

    void updateUser(User user);

    void deleteUser(String username);

    UserProfile getUserByUsername(String username);

    ResponseEntity<MessageResponse> changePassword(ProfileRequest user);

    void activateUser(String username);

    void deactivateUser(String username);

    ResponseEntity<MessageResponse> authenticate(String username, String password);

    TraineeProfile saveTrainee(TraineeRequest dto);

    User getUser(String username);
}
