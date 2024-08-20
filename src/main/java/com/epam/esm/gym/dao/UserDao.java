package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.User;

public interface UserDao extends Dao<User, Long> {
    boolean existsByUsername(String username);
}
