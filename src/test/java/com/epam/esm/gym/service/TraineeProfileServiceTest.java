package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.auth.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainee.PostTraineeRequest;
import com.epam.esm.gym.dto.trainee.PutTraineeRequest;
import com.epam.esm.gym.dto.trainee.TraineeProfileResponseResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerResponse;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.mapper.TraineeMapper;
import com.epam.esm.gym.service.profile.TraineeProfileService;
import com.epam.esm.gym.web.provider.trainee.TraineeArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeProfileArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeRegistrationArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainingArgumentsProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeProfileServiceTest {
    @Mock
    private TraineeDao dao;
    @Mock
    private TraineeMapper mapper;
    @Mock
    private UserService userService;
    @Mock
    private TrainerService trainerService;
    @InjectMocks
    private TraineeProfileService service;

    @ParameterizedTest
    @ArgumentsSource(TraineeRegistrationArgumentsProvider.class)
    void register(PostTraineeRequest request,
                  ResponseEntity<ProfileResponse> expectedResponse,
                  Trainee trainee) {

        String rawPassword = "rawPassword";
        String encodedPassword = "encodedPassword";
        User mockUser = trainee.getUser();
        ProfileResponse expectedProfileResponse = expectedResponse.getBody();

        when(userService.generateRandomPassword()).thenReturn(rawPassword);
        when(userService.encodePassword(rawPassword)).thenReturn(encodedPassword);
        when(userService.createTraineeUser(request, encodedPassword)).thenReturn(mockUser);
        when(userService.saveUser(mockUser)).thenReturn(mockUser);
        when(mapper.toTrainee(mockUser, request)).thenReturn(trainee);
        when(dao.save(trainee)).thenReturn(trainee);
        when(mapper.toProfile(mockUser.getUsername(), rawPassword)).thenReturn(expectedProfileResponse);

        ResponseEntity<ProfileResponse> response = service.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedProfileResponse, response.getBody());
        verify(userService).generateRandomPassword();
        verify(userService).encodePassword(rawPassword);
        verify(userService).createTraineeUser(request, encodedPassword);
        verify(userService).saveUser(mockUser);
        verify(mapper).toTrainee(mockUser, request);
        verify(dao).save(trainee);
        verify(mapper).toProfile(mockUser.getUsername(), rawPassword);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeArgumentsProvider.class)
    void deleteTraineeWhenTraineeExists(String username, Trainee trainee) {
        when(dao.findByName(username)).thenReturn(Optional.of(trainee));
        ResponseEntity<MessageResponse> response = service.deleteTrainee(username);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(dao).delete(trainee);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void deleteTraineeWhenTraineeDoesNotExist(String username) {
        when(dao.findByName(username)).thenReturn(Optional.empty());
        ResponseEntity<MessageResponse> response = service.deleteTrainee(username);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(dao, never()).delete(any());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeArgumentsProvider.class)
    void getTraineeProfileByNameWhenTraineeExists(
            String username, Trainee trainee,
            ResponseEntity<TraineeProfileResponseResponse> profile) {
        when(dao.findByName(username)).thenReturn(Optional.of(trainee));
        when(mapper.toTraineeProfile(trainee)).thenReturn(profile.getBody());
        ResponseEntity<BaseResponse> response = service.getTraineeProfileByName(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profile, response);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeArgumentsProvider.class)
    void getTraineeProfileByNameWhenTraineeDoesNotExist(String username) {
        when(dao.findByName(username)).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = service.getTraineeProfileByName(username);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateTrainee() {
        String username = "Harry.Potter.123";
        PutTraineeRequest request = PutTraineeRequest.builder()
                .firstName("Harry")
                .lastName("Potter")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .build();
        Trainee trainee = new Trainee();
        Trainee updatedTrainee = new Trainee();
        TraineeProfileResponseResponse updatedProfile = new TraineeProfileResponseResponse();
        when(dao.findByName(username)).thenReturn(Optional.of(trainee));
        when(dao.findByName(request.getUsername())).thenReturn(Optional.empty());
        when(mapper.update(request, trainee)).thenReturn(updatedTrainee);
        when(dao.update(updatedTrainee)).thenReturn(updatedTrainee);
        when(mapper.toTraineeProfile(updatedTrainee)).thenReturn(updatedProfile);
        ResponseEntity<BaseResponse> response = service.updateTrainee(username, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProfile, response.getBody());
    }


    @Test
    void validateUserWhenValid() {
        ProfileRequest request = new ProfileRequest(
                "Harry.Potter", "oldPassword", "wrongPassword");
        UserProfile userProfile = UserProfile.builder().password("oldPassword").active(true).build();
        when(userService.getUserByUsername(request.getUsername())).thenReturn(userProfile);
        assertTrue(service.validateUser(request));
    }

    @Test
    void validateUserWhenInvalid() {
        ProfileRequest request = new ProfileRequest(
                "Harry.Potter", "wrongPassword", "wrongPassword");
        UserProfile userProfile = UserProfile.builder().password("oldPassword").active(false).build();
        when(userService.getUserByUsername(request.getUsername())).thenReturn(userProfile);
        assertFalse(service.validateUser(request));
    }

    @Test
    void changePassword() {
        ProfileRequest request = new ProfileRequest(
                "Harry.Potter", "oldPassword", "newPassword");
        doAnswer(invocation -> null)
                .doThrow(new RuntimeException("Password change failed"))
                .when(userService)
                .changePassword(request);
        service.changePassword(request);
        verify(userService).changePassword(request);

        try {
            service.changePassword(request);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Password change failed", e.getMessage());
        }
    }


    @Test
    void updateTraineeTrainersByName() {
        String username = "Harry.Potter";
        List<String> trainerUsernames = List.of("Trainer1", "Trainer2");
        List<TrainerProfile> profiles = List.of(new TrainerProfile(), new TrainerProfile());
        Trainee trainee = new Trainee();
        when(trainerService.getTrainer(anyString())).thenReturn(Optional.of(new Trainer()));
        when(mapper.toTrainerProfile(any())).thenReturn(new TrainerProfile());
        when(dao.findByName(username)).thenReturn(Optional.of(trainee));
        when(mapper.toTrainers(anyList())).thenReturn(new HashSet<>());

        ResponseEntity<List<TrainerResponse>> response =
                service.updateTraineeTrainersByName(username, trainerUsernames);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profiles, response.getBody());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainingArgumentsProvider.class)
    void getTraineeTrainingsByName(
            Map<String, String> params, Trainee trainee,
            Training training, TrainingResponse trainingResponse) {
        when(dao.findByName(trainee.getUser().getUsername())).thenReturn(Optional.of(trainee));
        when(mapper.toResponse(training)).thenReturn(trainingResponse);
        ResponseEntity<List<TrainingResponse>> response =
                service.getTraineeTrainingsByName(trainee.getUser().getUsername(), params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(trainingResponse), response.getBody());
    }


    @Test
    void activateDeactivateProfile() {
        String username = "Harry.Potter";
        Trainee trainee = Trainee.builder().user(new User()).build();
        when(dao.findByName(username)).thenReturn(Optional.of(trainee));
        ResponseEntity<BaseResponse> response = service.activateDeactivateProfile(username, true);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(trainee.getUser().getActive());
    }

    @Test
    void getNotAssignedTrainers() {
        String username = "Harry.Potter";
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());
        List<TrainerProfile> trainerProfiles = List.of(new TrainerProfile(), new TrainerProfile());
        when(dao.findNotAssignedTrainers(username)).thenReturn(trainers);
        when(mapper.toTrainerProfile(any())).thenReturn(new TrainerProfile());
        ResponseEntity<List<TrainerResponse>> response = service.getNotAssignedTrainers(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainerProfiles, response.getBody());
    }

    @Test
    void register() {
        PostTraineeRequest request = new PostTraineeRequest(
                "Hermione", "Granger", LocalDate.of(1990, 1, 1), "Hogwarts");
        String rawPassword = "rawPassword";
        String encodedPassword = "encodedPassword";
        User user = User.builder()
                .username("hermione.granger")
                .password(encodedPassword)
                .build();
        Trainee trainee = Trainee.builder()
                .user(user)
                .build();
        ProfileResponse response = ProfileResponse.builder()
                .username("hermione.granger")
                .password(rawPassword)
                .build();

        when(userService.generateRandomPassword()).thenReturn(rawPassword);
        when(userService.encodePassword(rawPassword)).thenReturn(encodedPassword);
        when(userService.createTraineeUser(request, encodedPassword)).thenReturn(user);
        when(userService.saveUser(user)).thenReturn(user);
        when(mapper.toTrainee(user, request)).thenReturn(trainee);
        when(dao.save(trainee)).thenReturn(trainee);
        when(mapper.toProfile(user.getUsername(), rawPassword)).thenReturn(response);
        ResponseEntity<ProfileResponse> result = service.register(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(userService).generateRandomPassword();
        verify(userService).encodePassword(rawPassword);
        verify(userService).createTraineeUser(request, encodedPassword);
        verify(userService).saveUser(user);
        verify(mapper).toTrainee(user, request);
        verify(dao).save(trainee);
        verify(mapper).toProfile(user.getUsername(), rawPassword);
    }
}
