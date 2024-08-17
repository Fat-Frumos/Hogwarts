package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.UserProfile;

public interface UserService {
    void createUser(ProfileRequest user);

    void updateUser(UserProfile userProfile);

    void deleteUser(String username);

    UserProfile getUserByUsername(String username);

    void changePassword(ProfileRequest user);

    void activateUser(String username);

    void deactivateUser(String username);

    void authenticate(ProfileRequest request);
}
