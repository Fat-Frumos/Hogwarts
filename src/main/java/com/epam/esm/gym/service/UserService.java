package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.profile.UserDto;

public interface UserService {
    void createUser(String firstName, String lastName);

    void updateUser(UserDto userDto);

    void deleteUser(String username);

    UserDto getUserByUsername(String username);

    void changePassword(String username, String newPassword);

    void activateUser(String username);

    void deactivateUser(String username);
}
