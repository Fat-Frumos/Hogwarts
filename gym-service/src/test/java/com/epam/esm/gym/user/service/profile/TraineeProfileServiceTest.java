package com.epam.esm.gym.user.service.profile;

import com.epam.esm.gym.user.dao.JpaTraineeDao;
import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileRequest;
import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.profile.UserResponse;
import com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse;
import com.epam.esm.gym.user.dto.trainee.PostTraineeRequest;
import com.epam.esm.gym.user.dto.trainee.PutTraineeRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerResponse;
import com.epam.esm.gym.user.dto.trainer.TrainerResponseDto;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.Training;
import com.epam.esm.gym.user.entity.User;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.mapper.TraineeMapper;
import com.epam.esm.gym.user.provider.trainee.TraineeArgumentsProvider;
import com.epam.esm.gym.user.provider.trainee.TraineeProfileArgumentsProvider;
import com.epam.esm.gym.user.provider.trainee.TraineeRegistrationArgumentsProvider;
import com.epam.esm.gym.user.provider.trainee.TraineeTrainingArgumentsProvider;
import com.epam.esm.gym.user.service.TrainerService;
import com.epam.esm.gym.user.service.UserService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeProfileServiceTest {

    @InjectMocks
    private TraineeProfileService service;
    @Mock
    private TraineeMapper mapper;
    @Mock
    private JpaTraineeDao dao;
    @Mock
    private UserService userService;
    @Mock
    private TrainerService trainerService;


    @Test
    void testFindAllWithUsernames() {
        List<String> usernames = List.of("user1", "user2");
        Trainee trainee1 = new Trainee();
        Trainee trainee2 = new Trainee();
        when(dao.findAllByUsernames(usernames)).thenReturn(List.of(trainee1, trainee2));
        when(mapper.toTraineeProfile(trainee1)).thenReturn(FullTraineeProfileResponse.builder().build());
        when(mapper.toTraineeProfile(trainee2)).thenReturn(FullTraineeProfileResponse.builder().build());
        List<FullTraineeProfileResponse> result = service.findAll(usernames);
        assertEquals(2, result.size());
        verify(dao).findAllByUsernames(usernames);
    }

    @Test
    void testFindAllWithoutUsernames() {
        Trainee trainee = new Trainee();
        when(dao.findAll()).thenReturn(List.of(trainee));
        when(mapper.toTraineeProfile(trainee)).thenReturn(FullTraineeProfileResponse.builder().build());
        List<FullTraineeProfileResponse> result = service.findAll(null);
        assertEquals(1, result.size());
        verify(dao).findAll();
    }

    @Test
    void testGetActiveTrainersForTraineeSuccess() {
        String username = "traineeUser";
        Trainee trainee = new Trainee();
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(trainerService.getActiveTrainersForTrainee(trainee)).thenReturn(List.of(new TrainerResponseDto()));
        List<TrainerResponseDto> result = service.getActiveTrainersForTrainee(username);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(dao).findByUsername(username);
    }

    @Test
    void testGetActiveTrainersForTraineeNotFound() {
        String username = "unknownUser";
        when(dao.findByUsername(username)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> service.getActiveTrainersForTrainee(username));
        assertEquals("unknownUser", exception.getMessage());
    }

    @Test
    void testRegisterSuccess() {
        PostTraineeRequest request = PostTraineeRequest.builder().build();
        User user = User.builder().username("username").build();
        Trainee trainee = Trainee.builder().user(user).build();
        String rawPassword = "rawPassword";
        when(userService.generateRandomPassword()).thenReturn(rawPassword);
        when(userService.encodePassword(rawPassword)).thenReturn("encodedPassword");
        when(userService.createTraineeUser(request, "encodedPassword")).thenReturn(user);
        when(userService.saveUser(user)).thenReturn(user);
        when(mapper.toTrainee(user, request)).thenReturn(trainee);
        when(dao.save(trainee)).thenReturn(trainee);
        when(mapper.toProfile(trainee.getUser().getUsername(), rawPassword))
                .thenReturn(ProfileResponse.builder().username("username").build());
        ProfileResponse response = service.register(request);

        assertNotNull(response);
        verify(dao).save(trainee);
    }

    @Test
    void testDeleteTraineeSuccess() {
        String username = "traineeUser";
        Trainee trainee = new Trainee();
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));
        ResponseEntity<MessageResponse> response = service.deleteTrainee(username);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).message().contains("successfully deleted"));
        verify(dao).delete(trainee);
    }

    @Test
    void testDeleteTraineeNotFound() {
        String username = "unknownUser";
        when(dao.findByUsername(username)).thenReturn(Optional.empty());
        ResponseEntity<MessageResponse> response = service.deleteTrainee(username);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).message().contains("not found"));
    }

    @Test
    void testUpdateTraineeSuccess() {
        String username = "traineeUser";
        PutTraineeRequest request = PutTraineeRequest.builder().build();
        Trainee existingTrainee = new Trainee();
        when(dao.findByUsername(username)).thenReturn(Optional.of(existingTrainee));
        when(mapper.update(request, existingTrainee)).thenReturn(existingTrainee);
        when(dao.save(existingTrainee)).thenReturn(existingTrainee);
        when(mapper.toTraineeProfile(existingTrainee)).thenReturn(FullTraineeProfileResponse.builder().build());

        FullTraineeProfileResponse response = service.updateTrainee(username, request);

        assertNotNull(response);
        verify(dao).save(existingTrainee);
    }

    @Test
    void testUpdateTraineeNotFound() {
        String username = "unknownUser";
        PutTraineeRequest request = PutTraineeRequest.builder().build();
        when(dao.findByUsername(username)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> service.updateTrainee(username, request));
        assertEquals("User by name: unknownUser not found", exception.getMessage());
    }

    @Test
    void testValidateUserSuccess() {
        ProfileRequest request = new ProfileRequest(
                "username", "password", "");
        UserResponse userResponse = new UserResponse(
                "username", "password", "", " ", true);
        when(userService.getUserByUsername(request.getUsername())).thenReturn(userResponse);
        boolean isValid = service.validateUser(request);

        assertTrue(isValid);
        verify(userService).getUserByUsername(request.getUsername());
    }

    @Test
    void testGetTraineeTrainingsByNameSuccess() {
        String username = "traineeUser";
        Map<String, String> params = new HashMap<>();
        Trainee trainee = new Trainee();
        Training training = new Training();
        trainee.setTrainings(Set.of(training));
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(mapper.toResponse(training)).thenReturn(new TrainingResponse());
        List<TrainingResponse> responses = service.getTraineeTrainingsByName(username, params);
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        verify(dao).findByUsername(username);
    }

    @Test
    void testGetTraineeTrainingsByNameNotFound() {
        String username = "unknownUser";
        when(dao.findByUsername(username)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> service.getTraineeTrainingsByName(username, new HashMap<>()));
        assertEquals(username, exception.getMessage());
    }

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

        ProfileResponse response = service.register(request);

        assertEquals(expectedProfileResponse, response);
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
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));
        ResponseEntity<MessageResponse> response = service.deleteTrainee(username);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(dao).delete(trainee);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void deleteTraineeWhenTraineeDoesNotExist(String username) {
        when(dao.findByUsername(username)).thenReturn(Optional.empty());
        ResponseEntity<MessageResponse> response = service.deleteTrainee(username);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(dao, never()).delete(any());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeArgumentsProvider.class)
    void getTraineeProfileByNameWhenTraineeExists(
            String username, Trainee trainee, FullTraineeProfileResponse profile) {
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(mapper.toTraineeProfile(trainee)).thenReturn(profile);
        FullTraineeProfileResponse response = service.getTraineeProfileByName(username);
        assertEquals(profile, response);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeArgumentsProvider.class)
    void getTraineeProfileByNameWhenTraineeDoesNotExist(String username) {
        when(dao.findByUsername(username)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> service.getTraineeProfileByName(username));
        assertEquals(String.format("User by name: %s not found", username), exception.getMessage());
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
        FullTraineeProfileResponse updatedProfile = new FullTraineeProfileResponse(
                "", "", "", "", true, LocalDate.now(), new ArrayList<>());
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(dao.findByUsername(request.username())).thenReturn(Optional.empty());
        when(mapper.update(request, trainee)).thenReturn(updatedTrainee);
        when(dao.save(updatedTrainee)).thenReturn(updatedTrainee);
        when(mapper.toTraineeProfile(updatedTrainee)).thenReturn(updatedProfile);
        FullTraineeProfileResponse response = service.updateTrainee(username, request);
        assertNotNull(response);
    }

    @Test
    void validateUserWhenValid() {
        ProfileRequest request = new ProfileRequest(
                "Harry.Potter", "oldPassword", "wrongPassword");
        UserResponse userProfile = UserResponse.builder().password("oldPassword").active(true).build();
        when(userService.getUserByUsername(request.getUsername())).thenReturn(userProfile);
        assertTrue(service.validateUser(request));
    }

    @Test
    void validateUserWhenInvalid() {
        ProfileRequest request = new ProfileRequest(
                "Harry.Potter", "wrongPassword", "wrongPassword");
        UserResponse userProfile = UserResponse.builder().password("oldPassword").active(false).build();
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
        List<TrainerResponse> profiles = List.of(new TrainerResponse(), new TrainerResponse());
        Trainee trainee = new Trainee();
        when(trainerService.getTrainer(anyString())).thenReturn(Optional.of(new Trainer()));
        when(mapper.toTrainerProfile(any())).thenReturn(new TrainerResponse());
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(mapper.toTrainers(anyList())).thenReturn(new HashSet<>());
        List<TrainerResponse> response = service.updateTraineeTrainersByName(username, trainerUsernames);
        assertEquals(profiles, response);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainingArgumentsProvider.class)
    void getTraineeTrainingsByName(
            Map<String, String> params, Trainee trainee,
            Training training, TrainingResponse trainingResponse) {
        when(dao.findByUsername(trainee.getUser().getUsername())).thenReturn(Optional.of(trainee));
        when(mapper.toResponse(training)).thenReturn(trainingResponse);
        List<TrainingResponse> response = service.getTraineeTrainingsByName(trainee.getUser().getUsername(), params);
        assertEquals(List.of(trainingResponse), response);
    }

    @Test
    void activateDeactivateProfile() {
        String username = "Harry.Potter";
        Trainee trainee = Trainee.builder().user(new User()).build();
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainee));
        MessageResponse response = service.updateStatusProfile(username, true);
        assertNotNull(response);
    }

    @Test
    void register() {
        PostTraineeRequest request = new PostTraineeRequest(
                "Hermione", "Granger",
                LocalDate.of(1990, 1, 1), "Hogwarts");
        String rawPassword = "rawPassword";
        String encodedPassword = "encodedPassword";
        User user = User.builder()
                .username("hermione.granger")
                .password(encodedPassword)
                .build();
        Trainee trainee = Trainee.builder()
                .user(user)
                .build();
        ProfileResponse response = new ProfileResponse("hermione.granger", rawPassword);

        when(userService.generateRandomPassword()).thenReturn(rawPassword);
        when(userService.encodePassword(rawPassword)).thenReturn(encodedPassword);
        when(userService.createTraineeUser(request, encodedPassword)).thenReturn(user);
        when(userService.saveUser(user)).thenReturn(user);
        when(mapper.toTrainee(user, request)).thenReturn(trainee);
        when(dao.save(trainee)).thenReturn(trainee);
        when(mapper.toProfile(user.getUsername(), rawPassword)).thenReturn(response);
        ProfileResponse result = service.register(request);

        assertEquals(response, result);

        verify(userService).generateRandomPassword();
        verify(userService).encodePassword(rawPassword);
        verify(userService).createTraineeUser(request, encodedPassword);
        verify(userService).saveUser(user);
        verify(mapper).toTrainee(user, request);
        verify(dao).save(trainee);
        verify(mapper).toProfile(user.getUsername(), rawPassword);
    }
}
