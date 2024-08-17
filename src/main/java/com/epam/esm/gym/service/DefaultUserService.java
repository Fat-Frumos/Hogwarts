package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.UserProfile;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService {

    Set<ProfileRequest> users = new HashSet<>();

    @Override
    public void createUser(ProfileRequest request) {
        users.add(request);
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
    public void authenticate(ProfileRequest request) {

    }
}
