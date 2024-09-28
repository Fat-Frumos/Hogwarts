package com.epam.esm.gym.user.dao;

import com.epam.esm.gym.user.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.gym.user.entity.RoleType.ROLE_TRAINEE;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Unit tests for {@link JpaUserDao} class.
 *
 * <p>This test class uses Mockito to mock dependencies and verify interactions with the {@link SessionFactory}
 * and {@link Session} used by the {@link JpaUserDao} class. The tests cover methods for finding, saving, updating,
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
@DataJpaTest
@Sql("/test-data-user.sql")
class JpaUserDaoTest {

    @Autowired
    private JpaUserDao userDao;

    @Test
    void testFindByUsername() {
        Optional<User> user = userDao.findByUsername("harry.potter");
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo("harry.potter");
    }

    @Test
    void testFindUsersByUsername() {
        List<User> users = userDao.findUsersByUsername("ron.weasley");
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo("ron.weasley");
    }

    @Test
    void testFindAllTraineesByUsername() {
        List<User> users = userDao.findAllTraineesByUsername(
                ROLE_TRAINEE, List.of("hermione.granger", "neville.longbottom"));
        assertThat(users).hasSize(2);
        assertThat(users.stream().map(User::getUsername))
                .containsExactlyInAnyOrder("hermione.granger", "neville.longbottom");
    }

    @Test
    void testGetUserBy_userNotFound() {
        Optional<User> user = userDao.findByUsername("non.existent.user");
        assertThat(user).isNotPresent();
    }

    @Test
    void testGetUserBy_userExists() {
        Optional<User> user = userDao.findByUsername("hermione.granger");
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo("hermione.granger");
    }

    @Test
    void testExistsByUsername() {
        boolean exists = userDao.existsByUsername("hermione.granger");
        assertThat(exists).isTrue();
        exists = userDao.existsByUsername("non.existent.user");
        assertThat(exists).isFalse();
    }

    @Test
    void testUpdateUser() {
        User user = userDao.findByUsername("hermione.granger").orElseThrow();
        user.setFirstName("Hermione Jane");

        User updatedUser = userDao.save(user);
        assertThat(updatedUser.getFirstName()).isEqualTo("Hermione Jane");
    }

    @Test
    void testDeleteUser() {
        User user = userDao.findByUsername("neville.longbottom").orElseThrow();
        userDao.delete(user);
        Optional<User> deletedUser = userDao.findByUsername("neville.longbottom");
        assertThat(deletedUser).isNotPresent();
    }

    @Test
    void testFindAllUsers() {
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(6);
    }
}
