package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.exception.UserNotFoundException;
import com.epam.esm.gym.web.provider.UserDaoArgumentsProvider;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link JDBCUserDao} class.
 *
 * <p>This test class uses Mockito to mock dependencies and verify interactions with the {@link SessionFactory}
 * and {@link Session} used by the {@link JDBCUserDao} class. The tests cover methods for finding, saving, updating,
 * and deleting {@link User} entities, as well as verifying user existence and querying by username.</p>
 *
 * <p>The class is annotated with {@link ExtendWith} to use Mockito's JUnit extension, and it defines mock objects
 * for the {@link SessionFactory}, {@link Session}, and other Hibernate components.</p>
 *
 * <p>Each test method is parameterized using {@link ParameterizedTest} and {@link ArgumentsSource} to run tests with
 * different {@link User} objects, providing comprehensive coverage for the DAO methods.</p>
 *
 * @since 1.0
 * @version 1.0.0
 * @author Pavlo Poliak
 */
@ExtendWith(MockitoExtension.class)
class JDBCUserDaoTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private JDBCUserDao userDao;

    @Mock
    Query<User> query;

    @BeforeEach
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    /**
     * Tests the {@link JDBCUserDao#findAll()} method to ensure it retrieves all users correctly.
     *
     * <p>This test uses a parameterized {@link User} object to verify that the method correctly interacts with the
     * Hibernate {@link Session} to fetch the list of users.
     * It mocks the query execution to return a predefined list of users.</p>
     *
     * @param user the {@link User} object to be used in the test.
     */
    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testFindAll(User user) {
        List<User> users = List.of(user);
        String hql = "SELECT u FROM User u LEFT JOIN FETCH u.tokens";
        when(session.createQuery(hql, User.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(users);
        List<User> result = userDao.findAll();
        assertEquals(users, result);
    }

    /**
     * Tests the {@link JDBCUserDao#save(User)} method to ensure it correctly saves a user.
     *
     * <p>This test uses a parameterized {@link User} object to verify that the method
     * interacts with the Hibernate {@link Session} to persist the user.
     * It mocks the persist operation and verifies that the correct user is saved.</p>
     *
     * @param user the {@link User} object to be saved.
     */
    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testSave(User user) {
        doNothing().when(session).persist(user);
        User result = userDao.save(user);
        verify(session).persist(user);
        assertEquals(user, result);
    }

    /**
     * Tests the {@link JDBCUserDao#update(User)} method to ensure it correctly updates a user.
     *
     * <p>This test verifies that the update method creates a correct mutation query, sets the appropriate parameters,
     * and executes the update. It mocks the mutation query and verifies interactions with it.</p>
     *
     * @param user the {@link User} object to be updated.
     */
    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testUpdate(User user) {
        MutationQuery mutationQuery = mock(MutationQuery.class);
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter(anyString(), any())).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(1);
        User result = userDao.update(user);
        String hql = "UPDATE User SET active = :active, firstName = :firstName, " +
                "lastName = :lastName, password = :password, username = :username WHERE id = :id";
        verify(session).createMutationQuery(hql);
        verify(mutationQuery).setParameter("active", user.getActive());
        verify(mutationQuery).setParameter("firstName", user.getFirstName());
        verify(mutationQuery).setParameter("lastName", user.getLastName());
        verify(mutationQuery).setParameter("password", user.getPassword());
        verify(mutationQuery).setParameter("username", user.getUsername());
        verify(mutationQuery).setParameter("id", user.getId());
        verify(mutationQuery).executeUpdate();
        assertEquals(user, result);
    }

    /**
     * Tests the {@link JDBCUserDao#delete(User)} method to ensure it correctly deletes a user.
     *
     * <p>This test uses a parameterized {@link User} object to verify that the method correctly interacts with the
     * Hibernate {@link Session} to remove the user.
     * It mocks the remove operation and verifies that the correct user is deleted.</p>
     *
     * @param user the {@link User} object to be deleted.
     */
    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testDelete(User user) {
        doNothing().when(session).remove(user);
        userDao.delete(user);
        verify(session).remove(user);
    }

    /**
     * Tests the {@link JDBCUserDao#findByName(String)} method to ensure it correctly retrieves a user by username.
     *
     * <p>This test uses parameterized {@link User} and username values to verify that the method
     * correctly interacts with the Hibernate {@link Session} to find a user by username.
     * It mocks the query execution and verifies that the expected user is returned.</p>
     *
     * @param user the {@link User} object to be retrieved.
     * @param username the username to find the user by.
     */
    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testFindByUsername(User user, String username) {
        String sql = "SELECT u FROM User u WHERE username = :username";
        Optional<User> expectedUser = Optional.of(user);
        doReturn(query).when(session).createQuery(sql, User.class);
        doReturn(query).when(query).setParameter("username", username);
        doReturn(expectedUser).when(query).uniqueResultOptional();
        Optional<User> actualUser = userDao.findByName(username);
        assertEquals(expectedUser, actualUser);
    }

    /**
     * Tests the {@link JDBCUserDao#existsByUsername(String)} method
     * to ensure it correctly checks for the existence of a username.
     *
     * <p>This test uses a parameterized {@link User} object to verify that
     * the method correctly interacts with the Hibernate
     * {@link Session} to determine if a user with the given username exists.
     * It mocks the query execution and verifies the result.</p>
     *
     * @param user the {@link User} object whose username is to be checked.
     */
    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testExistsByUsername(User user) {
        String username = user.getUsername();
        String sql = "SELECT COUNT(*) FROM User WHERE username = :username";
        doReturn(query).when(session).createQuery(sql, Long.class);
        doReturn(query).when(query).setParameter("username", username);
        doReturn(1L).when(query).getSingleResult();
        assertTrue(userDao.existsByUsername(username));
    }

    /**
     * Tests the {@link JDBCUserDao#update(User)} method to ensure it correctly updates a user.
     *
     * <p>This test verifies that the method creates a correct mutation query, sets the appropriate parameters,
     * and executes the update. It mocks the mutation query and verifies interactions with it.</p>
     *
     * @param user the {@link User} object to be updated.
     */
    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testUpdateUser(User user) {
        String hql = "UPDATE User SET active = :active, firstName = :firstName, " +
                "lastName = :lastName, password = :password, username = :username WHERE id = :id";
        MutationQuery mocked = mock(MutationQuery.class);
        when(session.createMutationQuery(hql)).thenReturn(mocked);
        when(mocked.setParameter("active", user.getActive())).thenReturn(mocked);
        when(mocked.setParameter("firstName", user.getFirstName())).thenReturn(mocked);
        when(mocked.setParameter("lastName", user.getLastName())).thenReturn(mocked);
        when(mocked.setParameter("password", user.getPassword())).thenReturn(mocked);
        when(mocked.setParameter("username", user.getUsername())).thenReturn(mocked);
        when(mocked.setParameter("id", user.getId())).thenReturn(mocked);
        when(mocked.executeUpdate()).thenReturn(1);
        userDao.update(user);
        verify(mocked).executeUpdate();
    }

    @Test
    void testGetUserBy_userExists() {
        String username = "testUser";
        User expectedUser = new User();
        when(session.createQuery("SELECT u FROM User u WHERE username = :username", User.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(expectedUser));
        User actualUser = userDao.getUserBy(username);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void testGetUserBy_userNotFound() {
        String username = "testUser";
        when(session.createQuery("SELECT u FROM User u WHERE username = :username", User.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.empty());
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> userDao.getUserBy(username));
        assertEquals("User not found: testUser", thrown.getMessage());
    }

    @Test
    void testFindUsernamesByBaseName() {
        String baseUser = "baseUser";
        User user = new User();
        List<User> expectedUsers = Collections.singletonList(user);
        when(session.createQuery(
                "SELECT u FROM User u WHERE u.username LIKE :baseUsernamePattern", User.class))
                .thenReturn(query);
        when(query.setParameter("baseUsernamePattern", baseUser + "%")).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedUsers);
        List<User> actualUsers = userDao.findUsernamesByBaseName(baseUser);
        assertEquals(expectedUsers, actualUsers);
    }
}
