package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.profile.UserDto;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService {
    @Override
    public void createUser(String firstName, String lastName) {

    }

    @Override
    public void updateUser(UserDto userDto) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public UserDto getUserByUsername(String username) {
        return null;
    }

    @Override
    public void changePassword(String username, String newPassword) {

    }

    @Override
    public void activateUser(String username) {

    }

    @Override
    public void deactivateUser(String username) {

    }
}
