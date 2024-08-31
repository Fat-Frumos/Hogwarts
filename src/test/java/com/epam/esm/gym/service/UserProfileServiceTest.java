package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.mapper.UserMapper;
import com.epam.esm.gym.web.provider.AuthenticationArgumentsProvider;
import com.epam.esm.gym.web.provider.UserArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.PasswordChangeArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeProfileArgumentsProvider;
import com.epam.esm.gym.web.provider.trainer.TrainerProfileArgumentsProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

    @InjectMocks
    private UserProfileService userProfileService;

    @Mock
    private UserDao userDao;

    @Mock
    private UserMapper userMapper;

    @ParameterizedTest
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void saveTrainee(String expectedUsername, ResponseEntity<TraineeProfile> expectedResponse, TraineeRequest request) {
        User mockUser = new User();
        mockUser.setUsername(expectedUsername);

        when(userMapper.toUser(eq(request.getFirstName()), eq(request.getLastName()), eq(expectedUsername), anyString())).thenReturn(mockUser);
        when(userDao.save(mockUser)).thenReturn(mockUser);
        when(userMapper.toTraineeProfile(mockUser)).thenReturn(expectedResponse.getBody());

        TraineeProfile result = userProfileService.saveTrainee(request);

        assertNotNull(result);
        assertEquals(expectedResponse.getBody(), result);
        verify(userDao).save(mockUser);
        verify(userMapper).toTraineeProfile(mockUser);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerProfileArgumentsProvider.class)
    void saveTrainer(String expectedUsername, ResponseEntity<TrainerProfile> expectedResponse, TrainerRequest request) {
        User mockUser = new User();
        when(userMapper.toUser(eq(request.getFirstName()), eq(request.getLastName()), eq(expectedUsername), anyString())).thenReturn(mockUser);
        when(userDao.save(mockUser)).thenReturn(mockUser);
        when(userMapper.toTrainerProfile(mockUser)).thenReturn(expectedResponse.getBody());
        TrainerProfile result = userProfileService.saveTrainer(request);
        assertNotNull(result);
        assertEquals(expectedResponse.getBody(), result);
        verify(userDao).save(mockUser);
        verify(userMapper).toTrainerProfile(mockUser);
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

    @ParameterizedTest
    @ArgumentsSource(PasswordChangeArgumentsProvider.class)
    void changePassword(ProfileRequest request, MessageResponse expectedResponse) {
        User harry = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("Harry.Potter")
                .password("password123")
                .active(true)
                .permission(RoleType.TRAINER)
                .build();
        when(userDao.findByUsername(request.getUsername())).thenReturn(Optional.of(harry));
        ResponseEntity<MessageResponse> response = userProfileService.changePassword(request);
        assertEquals(expectedResponse.getStatus(), response.getStatusCode());
        assertEquals(expectedResponse.getMessage(), Objects.requireNonNull(response.getBody()).getMessage());
    }
}
