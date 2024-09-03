package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.mapper.UserMapper;
import com.epam.esm.gym.service.profile.UserProfileService;
import com.epam.esm.gym.web.provider.AuthenticationArgumentsProvider;
import com.epam.esm.gym.web.provider.UserArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeProfileArgumentsProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UserProfileService}.
 *
 * <p>This class contains test cases for the {@link UserProfileService} service layer. The tests ensure
 * that the service behaves correctly under various scenarios by using Mockito to mock dependencies and
 * verify interactions. Each test method sets up mocks and asserts expected outcomes to validate the
 * correctness of the service's methods.</p>
 *
 * <p>The class uses JUnit 5 and Mockito to create a controlled testing environment. Mocks are injected
 * into the {@link UserProfileService} instance to isolate the service's behavior from external dependencies
 * and to focus on testing the service logic.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {
    @InjectMocks
    private UserProfileService userProfileService;
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    @ParameterizedTest
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void saveTrainee(String expectedUsername,
                     ResponseEntity<TraineeProfile> expectedResponse,
                     TraineeRequest request) {
        String encodedPassword = "encodedPassword";

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(expectedUsername);
        user.setPassword(encodedPassword);
        user.setPermission(RoleType.ROLE_TRAINEE);

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setFirstName(request.getFirstName());
        savedUser.setLastName(request.getLastName());
        savedUser.setUsername(expectedUsername);
        savedUser.setPassword(encodedPassword);
        savedUser.setPermission(RoleType.ROLE_TRAINEE);

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userMapper.toUser(request.getFirstName(), request.getLastName(),
                expectedUsername, encodedPassword, RoleType.ROLE_TRAINEE)).thenReturn(user);
        when(userDao.save(user)).thenReturn(savedUser);

        User result = userProfileService.saveTraineeUser(request);

        assertEquals(savedUser, result);
        verify(passwordEncoder).encode(anyString());
        verify(userMapper).toUser(request.getFirstName(), request.getLastName(),
                expectedUsername, encodedPassword, RoleType.ROLE_TRAINEE);
        verify(userDao).save(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void updateUser(String username, User user) {
        user.setUsername(username);
        when(userDao.save(user)).thenReturn(user);
        userProfileService.updateUser(user);
        verify(userDao).save(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void deleteUser(String username, User user) {
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        doNothing().when(userDao).delete(user);
        userProfileService.deleteUser(username);
        verify(userDao).delete(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void getUserByUsername(String username, User user, UserProfile profile) {
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(profile);
        UserProfile result = userProfileService.getUserByUsername(username);
        assertNotNull(result);
        assertEquals(profile, result);
        verify(userMapper).toDto(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void activateUser(String username, User user) {
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        doAnswer(invocation -> {
            User arg = invocation.getArgument(0);
            arg.setActive(true);
            return null;
        }).when(userDao).save(user);
        userProfileService.activateUser(username);
        assertTrue(user.getActive(), "User should be active after activation");
        verify(userDao).findByUsername(username);
        verify(userDao).save(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void deactivateUser(String username, User user) {
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        doAnswer(invocation -> {
            User argument = invocation.getArgument(0);
            argument.setActive(false);
            return null;
        }).when(userDao).save(user);
        userProfileService.deactivateUser(username);
        assertFalse(user.getActive(), "User should be inactive after deactivation");
        verify(userDao).findByUsername(username);
        verify(userDao).save(user);
    }

    @ParameterizedTest
    @ArgumentsSource(AuthenticationArgumentsProvider.class)
    void authenticate(String username, String password, User user, HttpStatus expectedStatus) {
        user.setPassword(password);
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        ResponseEntity<MessageResponse> response = userProfileService.authenticate(username, password);
        assertEquals(expectedStatus, response.getStatusCode());
    }
}
