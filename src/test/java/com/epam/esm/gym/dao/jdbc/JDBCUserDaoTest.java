package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.web.provider.UserDaoArgumentsProvider;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JDBCUserDaoTest {

    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;

    @InjectMocks
    private JDBCUserDao userDao;
    @Mock
    NativeQuery<User> nativeQuery;

    @BeforeEach
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testFindAll(User user) {
        List<User> users = List.of(user);
        String hql = "FROM " + User.class.getName();
        when(session.createQuery(hql, User.class)).thenReturn(nativeQuery);
        when(nativeQuery.getResultList()).thenReturn(users);
        List<User> result = userDao.findAll();
        assertEquals(users, result);
    }

    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testSave(User user) {
        doNothing().when(session).persist(user);
        User result = userDao.save(user);
        verify(session).persist(user);
        assertEquals(user, result);
    }

    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testUpdate(User user) {
        MutationQuery mutationQuery = mock(MutationQuery.class);
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter(anyString(), any())).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(1);
        User result = userDao.update(user);
        verify(session).createMutationQuery("UPDATE User SET active = :active, firstName = :firstName, lastName = :lastName, password = :password, username = :username WHERE id = :id");
        verify(mutationQuery).setParameter("active", user.getActive());
        verify(mutationQuery).setParameter("firstName", user.getFirstName());
        verify(mutationQuery).setParameter("lastName", user.getLastName());
        verify(mutationQuery).setParameter("password", user.getPassword());
        verify(mutationQuery).setParameter("username", user.getUsername());
        verify(mutationQuery).executeUpdate();
        assertEquals(user, result);
    }


    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testDelete(User user) {
        doNothing().when(session).remove(user);
        userDao.delete(user);
        verify(session).remove(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testFindByUsername(User user, String username) {
        String sql = "SELECT * FROM \"users\" WHERE username = :username";
        Optional<User> expectedUser = Optional.of(user);
        doReturn(nativeQuery).when(session).createNativeQuery(sql, User.class);
        doReturn(nativeQuery).when(nativeQuery).setParameter("username", username);
        doReturn(expectedUser).when(nativeQuery).uniqueResultOptional();
        Optional<User> actualUser = userDao.findByUsername(username);
        assertEquals(expectedUser, actualUser);
    }

    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testExistsByUsername(User user) {
        String username = user.getUsername();
        String sql = "SELECT COUNT(*) FROM \"users\" WHERE username = :username";
        doReturn(nativeQuery).when(session).createNativeQuery(sql, Long.class);
        doReturn(nativeQuery).when(nativeQuery).setParameter("username", username);
        doReturn(1L).when(nativeQuery).getSingleResult();
        assertTrue(userDao.existsByUsername(username));
    }

    @ParameterizedTest
    @ArgumentsSource(UserDaoArgumentsProvider.class)
    void testUpdateUser(User user) {
        String hql = "UPDATE User SET active = :active, firstName = :firstName, lastName = :lastName, password = :password, username = :username WHERE id = :id";
        MutationQuery mutationQuery = mock(MutationQuery.class);
        when(session.createMutationQuery(hql)).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("active", user.getActive())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("firstName", user.getFirstName())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("lastName", user.getLastName())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("password", user.getPassword())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("username", user.getUsername())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter("id", user.getId())).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(1);
        userDao.update(user);
        verify(mutationQuery).executeUpdate();
    }
}
