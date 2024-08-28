package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.User;

public interface UserDao extends Dao<User> {

    boolean existsByUsername(String username);
}
