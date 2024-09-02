package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class InMemoryUserDao extends AbstractInMemoryDao<User> implements UserDao {
    private final Map<String, User> userMap;

    public InMemoryUserDao(StorageInitializer storageInitializer) {
        super(storageInitializer.getUserStorage());
        this.userMap = storageInitializer.getUserStorage();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userMap.get(username));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userMap.containsKey(username);
    }

    @Override
    public User save(User entity) {
        if (entity == null || userMap.containsKey(entity.getUsername())) {
            throw new IllegalArgumentException("User cannot be null and must be unique");
        }
        userMap.put(entity.getUsername(), entity);
        return entity;
    }

    @Override
    public User update(User entity) {
        if (entity == null || !userMap.containsKey(entity.getUsername())) {
            throw new NoSuchElementException("User does not exist");
        }
        userMap.put(entity.getUsername(), entity);
        return entity;
    }

    @Override
    public void delete(User entity) {
        if (entity == null || !userMap.containsKey(entity.getUsername())) {
            throw new NoSuchElementException("User does not exist");
        }
        userMap.remove(entity.getUsername());
    }
}
