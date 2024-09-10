package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.exception.UserNotFoundException;
import com.epam.esm.gym.mapper.UserMapper;
import com.epam.esm.gym.service.profile.UserProfileService;
import com.epam.esm.gym.web.provider.AuthenticationArgumentsProvider;
import com.epam.esm.gym.web.provider.UserArgumentsProvider;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

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
        if (user == null) {
            when(userDao.findByUsername(username)).thenReturn(Optional.empty());
        } else {
            when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(password, user.getPassword())).thenReturn(expectedStatus == HttpStatus.OK);
        }
        ResponseEntity<BaseResponse> response = userProfileService.authenticate(username, password);
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testGetUserShouldReturnUserWhenUserExists(String username, User user) {
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        User result = userProfileService.getUser(username);
        assertEquals(user, result);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testGetUserShouldThrowUserNotFoundExceptionWhenUserDoesNotExist(String username, User user) {
        when(userDao.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userProfileService.getUser(username));
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testChangePasswordShouldReturnAcceptedWhenPasswordIsUpdated(String username, User user) {
        ProfileRequest request = new ProfileRequest(
                username, "oldPassword123", "newPassword123");
        when(userDao.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn("encodedNewPassword");
        ResponseEntity<BaseResponse> response = userProfileService.changePassword(request);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(userDao).save(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testAuthenticateShouldReturnOkWhenCredentialsAreValid(String username, User user) {
        String password = "password456";
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        ResponseEntity<BaseResponse> response = userProfileService.authenticate(username, password);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userDao).save(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void createTrainerUser_ShouldReturnUser(String username, User user) {
        TrainerRequest dto = new TrainerRequest("Harry", "POISON", "Potter", null, null);
        String password = "password123";
        String name = "Harry.Potter.1";
        when(userMapper.toUser(dto.getFirstName(), dto.getLastName(), name, password, RoleType.ROLE_TRAINER))
                .thenReturn(user);
        User result = userProfileService.createTrainerUser(dto, password);
        assertEquals(user, result);

    }

}
