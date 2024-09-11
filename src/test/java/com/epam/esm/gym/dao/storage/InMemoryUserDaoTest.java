package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link InMemoryUserDao} class.
 *
 * <p>This class is responsible for testing the functionality of the {@link InMemoryUserDao},
 * which provides in-memory data access operations for {@link User} entities.
 * The tests ensure that CRUD operations such as saving, updating, and deleting
 * users are correctly handled by the DAO implementation.</p>
 *
 * <p>Tests include verification of user retrieval, storage operations, and handling of
 * null and missing values. The class uses Mockito to mock dependencies and
 * verify interactions with the {@link InMemoryUserDao}.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class InMemoryUserDaoTest {

    @InjectMocks
    private InMemoryUserDao dao;

    @Mock
    private StorageInitializer storageInitializer;

    private Map<String, User> userMap;

    private final User user = User.builder()
            .id(2)
            .firstName("Hermione")
            .lastName("Granger")
            .username("Hermione.Granger")
            .password("newPassword")
            .active(true)
            .build();

    private final User updatedUser = User.builder()
            .id(3)
            .firstName("Harry")
            .lastName("Potter")
            .username("Harry.Potter")
            .password("password789")
            .active(true)
            .build();

    @BeforeEach
    void setUp() {
        userMap = new java.util.HashMap<>();
        when(storageInitializer.getUserStorage()).thenReturn(userMap);
        dao = new InMemoryUserDao(storageInitializer);
    }

    @Test
    void findAll() {
        userMap.put(user.getUsername(), user);
        userMap.put(updatedUser.getUsername(), updatedUser);

        assertEquals(2, dao.findAll().size());
    }

    @Test
    void findByUsername() {
        userMap.put(user.getUsername(), user);
        Optional<User> result = dao.findByName("Hermione.Granger");
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByUsername_UserNotFound() {
        Optional<User> result = dao.findByName("nonexistentUser");
        assertFalse(result.isPresent());
    }

    @Test
    void existsByUsername() {
        userMap.put(user.getUsername(), user);
        assertTrue(dao.existsByUsername("Hermione.Granger"));
        assertFalse(dao.existsByUsername("nonexistentUser"));
    }

    @Test
    void getUserBy() {
        userMap.put(user.getUsername(), user);
        User result = dao.getUserBy("Hermione.Granger");
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void getUserBy_UserNotFound() {
        assertNull(dao.getUserBy("nonexistentUser"));
    }

    @Test
    void save() {
        User savedUser = dao.save(user);
        assertEquals(user, savedUser);
        assertTrue(userMap.containsKey(user.getUsername()));
    }

    @Test
    void update() {
        userMap.put(user.getUsername(), user);
        User result = dao.update(user);
        assertEquals(user, result);
        assertEquals("newPassword", userMap.get("Hermione.Granger").getPassword());
    }

    @Test
    void update_UserNull() {
        assertThrows(NoSuchElementException.class, () -> dao.update(null));
    }

    @Test
    void update_UserNotFound() {
        assertThrows(NoSuchElementException.class, () -> dao.update(user));
    }

    @Test
    void delete() {
        userMap.put(user.getUsername(), user);
        dao.delete(user);
        assertFalse(userMap.containsKey(user.getUsername()));
    }

    @Test
    void delete_UserNull() {
        assertThrows(NoSuchElementException.class, () -> dao.delete(null));
    }

    @Test
    void delete_UserNotFound() {
        assertThrows(NoSuchElementException.class, () -> dao.delete(user));
    }
}
