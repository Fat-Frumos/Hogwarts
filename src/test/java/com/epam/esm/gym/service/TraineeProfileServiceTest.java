package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.mapper.TraineeMapper;
import com.epam.esm.gym.web.provider.TraineeArgumentsProvider;
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
public class TraineeProfileServiceTest {

    @InjectMocks
    private TraineeProfileService traineeProfileService;

    @Mock
    private TraineeDao dao;

    @Mock
    private TraineeMapper mapper;

    @Mock
    private UserService userService;

    @Mock
    private TrainerService trainerService;

    @ParameterizedTest
    @ArgumentsSource(TraineeRegistrationArgumentsProvider.class)
    void register(TraineeRequest request, ResponseEntity<ProfileResponse> response) {

        TraineeProfile expectedProfile = TraineeProfile.builder()
                .firstName("Harry")
                .lastName("Potter")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .active(true)
                .build();
        User harry = new User(1, "Harry", "Potter", "Harry.Potter", "password123", true);

        Trainee trainee = Trainee.builder()
                .id(1L)
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .user(harry)
                .trainings(new HashSet<>())
                .build();


        when(userService.saveTrainee(request)).thenReturn(expectedProfile);
        when(mapper.toEntity(expectedProfile)).thenReturn(trainee);
        when(dao.save(trainee)).thenReturn(trainee);
        when(mapper.toProfile(harry)).thenReturn(response.getBody());

        ResponseEntity<ProfileResponse> register = traineeProfileService.register(request);

        assertEquals(HttpStatus.CREATED, register.getStatusCode());
        assertEquals(response.getBody(), register.getBody());
        verify(dao).save(trainee);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeArgumentsProvider.class)
    void deleteTraineeWhenTraineeExists(String username, Trainee trainee) {
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));
        ResponseEntity<Void> response = traineeProfileService.deleteTrainee(username);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(dao).delete(trainee);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void deleteTraineeWhenTraineeDoesNotExist(String username) {
        when(dao.findByUsername(username)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = traineeProfileService.deleteTrainee(username);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(dao, never()).delete(any());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeArgumentsProvider.class)
    void getTraineeProfileByNameWhenTraineeExists(String username, Trainee trainee, ResponseEntity<TraineeProfile> profile) {
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(mapper.toTraineeProfile(trainee)).thenReturn(profile.getBody());
        ResponseEntity<TraineeProfile> response = traineeProfileService.getTraineeProfileByName(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profile, response);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeArgumentsProvider.class)
    void getTraineeProfileByNameWhenTraineeDoesNotExist(String username) {
        when(dao.findByUsername(username)).thenReturn(Optional.empty());
        ResponseEntity<TraineeProfile> response = traineeProfileService.getTraineeProfileByName(username);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateTrainee() {
        String username = "Harry.Potter";
        TraineeRequest request = TraineeRequest.builder()
                .firstName("Harry")
                .lastName("Potter")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .build();

        Trainee trainee = new Trainee();
        Trainee updatedTrainee = new Trainee();
        TraineeProfile updatedProfile = new TraineeProfile();

        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(mapper.update(request, trainee)).thenReturn(updatedTrainee);
        when(dao.update(updatedTrainee)).thenReturn(updatedTrainee);
        when(mapper.toTraineeProfile(updatedTrainee)).thenReturn(updatedProfile);

        ResponseEntity<TraineeProfile> response = traineeProfileService.updateTrainee(username, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProfile, response.getBody());
    }

    @Test
    void validateUserWhenValid() {
        ProfileRequest request = new ProfileRequest("Harry.Potter", "oldPassword", "wrongPassword");
        UserProfile userProfile = new UserProfile();
        userProfile.setPassword("oldPassword");
        userProfile.setActive(true);
        when(userService.getUserByUsername(request.getUsername())).thenReturn(userProfile);

        assertTrue(traineeProfileService.validateUser(request));
    }

    @Test
    void validateUserWhenInvalid() {
        ProfileRequest request = new ProfileRequest("Harry.Potter", "wrongPassword", "wrongPassword");
        UserProfile userProfile = new UserProfile();
        userProfile.setPassword("oldPassword");
        userProfile.setActive(false);

        when(userService.getUserByUsername(request.getUsername())).thenReturn(userProfile);

        assertFalse(traineeProfileService.validateUser(request));
    }

    @Test
    void changePassword() {
        ProfileRequest request = new ProfileRequest("Harry.Potter", "oldPassword", "newPassword");
        doAnswer(invocation -> null).doThrow(new RuntimeException("Password change failed")).when(userService).changePassword(request);
        traineeProfileService.changePassword(request);
        verify(userService).changePassword(request);

        try {
            traineeProfileService.changePassword(request);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Password change failed", e.getMessage());
        }
    }


    @Test
    void updateTraineeTrainersByName() {
        String username = "Harry.Potter";
        List<String> trainerUsernames = List.of("Trainer1", "Trainer2");
        List<TrainerProfile> trainerProfiles = List.of(new TrainerProfile(), new TrainerProfile());
        Trainee trainee = new Trainee();
        when(trainerService.getTrainer(anyString())).thenReturn(new Trainer());
        when(mapper.toTrainerProfile(any())).thenReturn(new TrainerProfile());
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(mapper.toTrainers(anyList())).thenReturn(new HashSet<>());

        ResponseEntity<List<TrainerProfile>> response = traineeProfileService.updateTraineeTrainersByName(username, trainerUsernames);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainerProfiles, response.getBody());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainingArgumentsProvider.class)
    void getTraineeTrainingsByName(Map<String, String> params, Trainee trainee, Training training, TrainingResponse trainingResponse) {

        when(dao.findByUsername(trainee.getUser().getUsername())).thenReturn(Optional.of(trainee));
        when(mapper.toResponse(training)).thenReturn(trainingResponse);
        ResponseEntity<List<TrainingResponse>> response = traineeProfileService.getTraineeTrainingsByName(trainee.getUser().getUsername(), params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(trainingResponse), response.getBody());
    }


    @Test
    void activateDeactivateProfile() {
        String username = "Harry.Potter";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());

        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));

        ResponseEntity<Void> response = traineeProfileService.activateDeactivateProfile(username, true);

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

        ResponseEntity<List<TrainerProfile>> response = traineeProfileService.getNotAssignedTrainers(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainerProfiles, response.getBody());
    }
}
