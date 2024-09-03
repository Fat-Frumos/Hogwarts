package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * In-memory Data Access Object (DAO) for managing {@link User} entities.
 *
 * <p>This DAO implementation uses an in-memory {@link Map} to perform CRUD operations on {@link User} entities.
 * It is activated when the application property <code>strategy.dao.type</code> is set to <code>in-memory</code>.
 * The class is initialized with a {@link StorageInitializer} to provide the necessary user storage map.</p>
 */
@Slf4j
@Repository
@ConditionalOnProperty(name = "strategy.dao.type", havingValue = "in-memory")
public class InMemoryUserDao extends AbstractInMemoryDao<User> implements UserDao {
    private final Map<String, User> userMap;

    /**
     * Constructs an {@link InMemoryUserDao} with the provided {@link StorageInitializer}.
     *
     * <p>This constructor initializes the DAO with the user storage map from the {@link StorageInitializer} and
     * delegates to the superclass constructor to set up the in-memory storage.</p>
     *
     * @param storageInitializer the {@link StorageInitializer} providing the user storage map.
     */
    public InMemoryUserDao(StorageInitializer storageInitializer) {
        super(storageInitializer.getUserStorage());
        this.userMap = storageInitializer.getUserStorage();
    }

    /**
     * Retrieves all {@link User} entities.
     *
     * <p>This method returns a list of all users stored in memory.</p>
     *
     * @return a list of all {@link User} entities.
     */
    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    /**
     * Finds a {@link User} by username.
     *
     * <p>This method retrieves a user from memory based on the provided username.
     * If no user is found, it returns an empty {@link Optional}.</p>
     *
     * @param username the username of the user to retrieve.
     * @return an {@link Optional} containing the user if found, or empty if not found.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(getUserBy(username));
    }

    /**
     * Checks if a {@link User} with the specified username exists.
     *
     * <p>This method returns {@code true} if a user with the given username
     * is present in the storage, otherwise {@code false}.</p>
     *
     * @param username the username to check for existence.
     * @return {@code true} if a user with the username exists, {@code false} otherwise.
     */
    @Override
    public boolean existsByUsername(String username) {
        return userMap.containsKey(username);
    }

    /**
     * Retrieves a {@link User} by username.
     *
     * <p>This method fetches a user from the in-memory storage based on the provided username.</p>
     *
     * @param username the username of the user to retrieve.
     * @return the {@link User} associated with the username, or {@code null} if no user exists.
     */
    @Override
    public User getUserBy(String username) {
        return userMap.get(username);
    }

    /**
     * Saves a {@link User} entity to the storage.
     *
     * <p>This method adds the user to the in-memory storage and logs the updated user map.</p>
     *
     * @param entity the {@link User} entity to save.
     * @return the saved {@link User} entity.
     */
    @Override
    public User save(User entity) {
        userMap.put(entity.getUsername(), entity);
        log.info(userMap.toString());
        return entity;
    }

    /**
     * Updates an existing {@link User} entity.
     *
     * <p>This method updates the user in the in-memory storage.
     * If the user does not exist, a {@link NoSuchElementException} is thrown.</p>
     *
     * @param entity the {@link User} entity to update.
     * @return the updated {@link User} entity.
     * @throws NoSuchElementException if the user does not exist in the storage.
     */
    @Override
    public User update(User entity) {
        if (entity == null || !userMap.containsKey(entity.getUsername())) {
            throw new NoSuchElementException("User does not exist");
        }
        userMap.put(entity.getUsername(), entity);
        return entity;
    }

    /**
     * Deletes a {@link User} entity from the storage.
     *
     * <p>This method removes the user from the in-memory storage. If the user does not exist,
     * a {@link NoSuchElementException} is thrown.</p>
     *
     * @param entity the {@link User} entity to delete.
     * @throws NoSuchElementException if the user does not exist in the storage.
     */
    @Override
    public void delete(User entity) {
        if (entity == null || !userMap.containsKey(entity.getUsername())) {
            throw new NoSuchElementException("User does not exist");
        }
        userMap.remove(entity.getUsername());
    }
}
