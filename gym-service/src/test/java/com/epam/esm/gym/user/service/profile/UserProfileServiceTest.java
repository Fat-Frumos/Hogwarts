package com.epam.esm.gym.user.service.profile;

import com.epam.esm.gym.user.dao.JpaUserDao;
import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileRequest;
import com.epam.esm.gym.user.dto.profile.UserResponse;
import com.epam.esm.gym.user.dto.trainee.PostTraineeRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerRequest;
import com.epam.esm.gym.user.entity.RoleType;
import com.epam.esm.gym.user.entity.User;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.mapper.UserMapper;
import com.epam.esm.gym.user.provider.AuthenticationArgumentsProvider;
import com.epam.esm.gym.user.provider.UserArgumentsProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
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

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper mapper;
    @Mock
    private JpaUserDao userDao;
    @InjectMocks
    private UserProfileService userProfileService;

    User user = User.builder()
            .id(1)
            .firstName("Harry")
            .lastName("Potter")
            .username("Harry.Potter")
            .password("password123")
            .active(true)
            .permission(RoleType.ROLE_TRAINER)
            .build();

    @Test
    void deleteUser() {
        when(userDao.findByUsername("Harry.Potter")).thenReturn(Optional.of(user));
        userProfileService.deleteUser("Harry.Potter");
        verify(userDao).delete(user);
    }

    @Test
    void getUser() {
        when(userDao.findByUsername("Harry.Potter")).thenReturn(Optional.of(user));
        User result = userProfileService.getUser("Harry.Potter");
        assertEquals(user, result);
    }

    @Test
    void generateRandomPassword() {
        String randomPassword = userProfileService.generateRandomPassword();
        assertEquals(10, randomPassword.length());
        randomPassword.chars().forEach(c -> assertTrue(
                UserProfileService.ALPHANUMERIC_STRING.indexOf(c) >= 0));
    }

    @Test
    void saveUser() {
        when(userDao.save(user)).thenReturn(user);
        User result = userProfileService.saveUser(user);
        assertEquals(user, result);
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
    @ArgumentsSource(AuthenticationArgumentsProvider.class)
    void authenticate(String username, String password, User user, HttpStatus expectedStatus) {
        if (user == null) {
            when(userDao.findByUsername(username)).thenReturn(Optional.empty());
        } else {
            when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(password, user.getPassword()))
                    .thenReturn(expectedStatus == HttpStatus.OK);
        }
        MessageResponse response = userProfileService.authenticate(username, password);
        assertNotNull(response);
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
    void testGetUserShouldThrowUserNotFoundExceptionWhenUserDoesNotExist(String username) {
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
        MessageResponse response = userProfileService.changePassword(request);
        assertNotNull(response);
        verify(userDao).save(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testAuthenticateShouldReturnOkWhenCredentialsAreValid(String username, User user) {
        String password = "password456";
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        MessageResponse response = userProfileService.authenticate(username, password);
        assertNotNull(response);
        verify(userDao).save(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testDeleteUser(String username, User user) {
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        ResponseEntity<Void> response = userProfileService.deleteUser(username);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userDao).delete(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testAuthenticateSuccessful(String username, User user) {
        String password = "correctPassword";
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        MessageResponse response = userProfileService.authenticate(username, password);
        assertEquals("Authentication successful", response.message());
        verify(userDao).save(user);
    }

    @Test
    void createTraineeUser() {
        PostTraineeRequest request = PostTraineeRequest.builder()
                .firstName("Harry").lastName("Potter").build();
        String password = "rawPassword";
        when(mapper.toUser(anyString(), anyString(), anyString(), eq(password),
                eq(RoleType.ROLE_TRAINEE))).thenReturn(user);
        User result = userProfileService.createTraineeUser(request, password);
        assertNotNull(result);
        verify(mapper).toUser(eq("Harry"), eq("Potter"), anyString(),
                eq(password), eq(RoleType.ROLE_TRAINEE));
    }

    @Test
    void createTrainerUser() {
        TrainerRequest request = TrainerRequest.builder().firstName("Harry").lastName("Potter").build();
        String password = "rawPassword";
        when(mapper.toUser(anyString(), anyString(), anyString(), eq(password),
                eq(RoleType.ROLE_TRAINER))).thenReturn(user);
        User result = userProfileService.createTrainerUser(request, password);
        assertNotNull(result);
        verify(mapper).toUser(eq("Harry"), eq("Potter"), anyString(),
                eq(password), eq(RoleType.ROLE_TRAINER));
    }

    @Test
    void updateUser() {
        userProfileService.updateUser(user);
        verify(userDao).save(user);
    }

    @Test
    void encodePassword() {
        String password = "password123";
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        String result = userProfileService.encodePassword(password);
        assertEquals("encodedPassword", result);
        verify(passwordEncoder).encode(password);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void authenticate(String username, User user) {
        String password = "password123";
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        MessageResponse response = userProfileService.authenticate(username, password);
        assertEquals("Authentication successful", response.message());
        verify(userDao).save(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testChangePasswordSuccess(String username, User user) {
        ProfileRequest request = new ProfileRequest(
                username, "oldPassword", "newPassword");
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches("newPassword", user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        MessageResponse response = userProfileService.changePassword(request);
        assertNotNull(response);
        assertEquals("Password updated successfully", response.message());
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testFindAll(String username, User user) {
        List<User> users = List.of(user);
        when(userDao.findAll()).thenReturn(users);
        when(mapper.toDto(user)).thenReturn(UserResponse.builder().username(username).build());
        List<UserResponse> responses = userProfileService.findAll();
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(username, responses.get(0).getUsername());
        verify(userDao).findAll();
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testChangePasswordUserNotFound(String username) {
        ProfileRequest request = new ProfileRequest(
                username, "oldPassword", "newPassword");
        when(userDao.findByUsername(username)).thenReturn(Optional.empty());
        MessageResponse response = userProfileService.changePassword(request);
        assertNotNull(response);
        assertEquals("User not found", response.message());
        verify(userDao).findByUsername(username);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testChangePasswordOldPasswordMismatch(String username, User user) {
        ProfileRequest request = new ProfileRequest(username, "wrongOldPassword", "newPassword");
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOldPassword", user.getPassword())).thenReturn(false);
        MessageResponse response = userProfileService.changePassword(request);
        assertNotNull(response);
        assertEquals("Old password is incorrect", response.message());
        verify(userDao).findByUsername(username);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testChangePasswordNewPasswordSameAsOld(String username, User user) {
        ProfileRequest request = new ProfileRequest(
                username, "oldPassword", "oldPassword");
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);
        MessageResponse response = userProfileService.changePassword(request);
        assertNotNull(response);
        assertEquals("New password is the same as the old password", response.message());
        verify(userDao).findByUsername(username);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testAuthenticateUserNotFound(String username) {
        when(userDao.findByUsername(username)).thenReturn(Optional.empty());
        MessageResponse response = userProfileService.authenticate(username, "password123");
        assertNotNull(response);
        assertEquals("User not found", response.message());
        verify(userDao).findByUsername(username);
    }

    @Test
    void testAuthenticateSuccess() {
        when(userDao.findByUsername("Harry.Potter")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);
        MessageResponse response = userProfileService.authenticate("Harry.Potter", "password123");
        assertNotNull(response);
        assertEquals("Authentication successful", response.message());
        verify(userDao).findByUsername("Harry.Potter");
        verify(userDao).save(user);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testFindAllTraineeByName(String username, User user) {
        List<String> usernames = List.of(username);
        List<User> users = List.of(user);

        when(userDao.findAllTraineesByUsername(RoleType.ROLE_TRAINEE, usernames)).thenReturn(users);
        UserResponse expectedResponse = mapper.toDto(user);
        when(mapper.toDto(user)).thenReturn(expectedResponse);
        List<UserResponse> responses = userProfileService.findAllTraineeByName(usernames);
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(expectedResponse, responses.get(0));

        verify(userDao).findAllTraineesByUsername(RoleType.ROLE_TRAINEE, usernames);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    void testGetUserByUsername(String username, User user) {
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        UserResponse expectedResponse = UserResponse.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        when(mapper.toDto(user)).thenReturn(expectedResponse);
        UserResponse response = userProfileService.getUserByUsername(username);
        assertNotNull(response, "Response should not be null");
        assertEquals(expectedResponse.getUsername(), response.getUsername());
        verify(userDao).findByUsername(username);
    }

    @Test
    void testDeleteUser() {
        when(userDao.findByUsername("Harry.Potter")).thenReturn(Optional.of(user));
        ResponseEntity<Void> response = userProfileService.deleteUser("Harry.Potter");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userDao).delete(user);
    }

    @Test
    void testDeleteUserUserNotFound() {
        when(userDao.findByUsername("Non.Existent")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userProfileService.deleteUser("Non.Existent"));
    }
}
