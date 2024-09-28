package com.epam.esm.gym.user.service.profile;

import com.epam.esm.gym.user.dao.JpaTrainerDao;
import com.epam.esm.gym.user.dao.JpaTrainingTypeDao;
import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileRequest;
import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.trainer.UpdateTrainerRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import com.epam.esm.gym.user.dto.trainer.TrainerRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerResponseDto;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.TrainingType;
import com.epam.esm.gym.user.entity.User;
import com.epam.esm.gym.user.exception.UserAlreadyExistsException;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.mapper.TrainerMapper;
import com.epam.esm.gym.user.provider.trainee.TraineeTrainersResponseEntityProfileArgumentsProvider;
import com.epam.esm.gym.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link TrainerProfileService}.
 * mocking JDBC interactions, ensuring that tests do not rely on a real database.
 * on {@link TrainerProfileService}, allowing for the testing of web layer components
 * related to trainer profile operations.
 */
@ExtendWith(MockitoExtension.class)
class TrainerProfileServiceTest {

    @InjectMocks
    private TrainerProfileService service;

    @Mock
    private TrainerMapper mapper;

    @Mock
    private UserService userService;

    @Mock
    private JpaTrainerDao trainerDao;

    @Mock
    private JpaTrainingTypeDao trainingTypeDao;

    @Test
    void testRegisterTrainerSuccess() {
        TrainerRequest request = TrainerRequest.builder()
                .firstName("trainerName").specialization("specialization").build();
        String rawPassword = "rawPassword";

        User user = User.builder().username("username").build();
        Trainer trainer = Trainer.builder().user(user).build();
        when(userService.generateRandomPassword()).thenReturn(rawPassword);
        when(userService.createTrainerUser(request, userService.encodePassword(rawPassword))).thenReturn(user);
        when(userService.saveUser(user)).thenReturn(user);
        when(trainingTypeDao.findBySpecialization(any())).thenReturn(Optional.empty());
        when(trainingTypeDao.save(any())).thenReturn(new TrainingType());
        when(mapper.toTrainer(any(), any())).thenReturn(trainer);
        when(trainerDao.save(trainer)).thenReturn(trainer);
        when(mapper.toProfileDto(any(), any())).thenReturn(new ProfileResponse("trainerName", rawPassword));

        ProfileResponse response = service.registerTrainer(request);

        assertEquals("trainerName", response.username());
        verify(trainerDao).save(trainer);
    }

    @Test
    void testChangeTrainerPasswordSuccess() {
        ProfileRequest request = new ProfileRequest("trainerName", "newPassword", "");
        service.changeTrainerPassword(request);
        verify(userService).changePassword(request);
    }

    @Test
    void testDeleteTrainerSuccess() {
        String username = "trainerName";
        Trainer trainer = new Trainer();
        when(trainerDao.findByUsername(username)).thenReturn(Optional.of(trainer));

        MessageResponse response = service.deleteTrainer(username);

        assertEquals("Trainer deleted successfully: trainerName", response.message());
        verify(trainerDao).delete(trainer);
    }

    @Test
    void testDeleteTrainerNotFound() {
        String username = "nonExistentTrainer";
        when(trainerDao.findByUsername(username)).thenReturn(Optional.empty());

        MessageResponse response = service.deleteTrainer(username);

        assertEquals("Trainer by name: nonExistentTrainer not found", response.message());
        verify(trainerDao, never()).delete(any());
    }

    @Test
    void testGetTrainerProfileByNameSuccess() {
        String username = "trainerName";
        Trainer trainer = new Trainer();
        when(trainerDao.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(mapper.toDto(trainer)).thenReturn(new TrainerProfile());

        TrainerProfile profile = service.getTrainerProfileByName(username);

        assertNotNull(profile);
        verify(mapper).toDto(trainer);
    }

    @Test
    void testGetTrainerProfileByNameNotFound() {
        String username = "nonExistentTrainer";
        when(trainerDao.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getTrainerProfileByName(username));
    }

    @Test
    void testUpdateTrainerSuccess() {
        String username = "trainerName";
        UpdateTrainerRequest request = new UpdateTrainerRequest();
        Trainer existingTrainer = new Trainer();
        Trainer updatedTrainer = new Trainer();

        when(trainerDao.findByUsername(username)).thenReturn(Optional.of(existingTrainer));
        when(trainerDao.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(mapper.toEntity(request, existingTrainer)).thenReturn(updatedTrainer);
        when(trainerDao.save(updatedTrainer)).thenReturn(updatedTrainer);
        when(mapper.toDto(updatedTrainer)).thenReturn(new TrainerProfile());
        TrainerProfile profile = service.updateTrainer(username, request);
        assertNotNull(profile);
        verify(trainerDao).save(updatedTrainer);
    }

    @Test
    void testUpdateTrainerNotFound() {
        String username = "nonExistentTrainer";
        UpdateTrainerRequest request = new UpdateTrainerRequest();
        when(trainerDao.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> service.updateTrainer(username, request));
    }

    @Test
    void testGetNotAssignedTrainers() {
        String traineeUsername = "traineeName";
        Trainer trainer = new Trainer();
        when(trainerDao.findNotAssignedTrainers(traineeUsername)).thenReturn(List.of(trainer));
        when(mapper.toResponseDto(trainer)).thenReturn(new TrainerResponseDto());
        List<TrainerResponseDto> trainers = service.getNotAssignedTrainers(traineeUsername);
        assertFalse(trainers.isEmpty());
        verify(mapper).toResponseDto(trainer);
    }

    @Test
    void testRegisterTrainerFailure() {
        TrainerRequest request = TrainerRequest.builder().specialization("specialization").build();
        String rawPassword = "rawPassword";
        when(userService.generateRandomPassword()).thenReturn(rawPassword);
        when(userService.createTrainerUser(request, userService.encodePassword(rawPassword)))
                .thenThrow(new RuntimeException("User creation failed"));
        assertThrows(RuntimeException.class, () -> service.registerTrainer(request));
    }

    @Test
    void testChangeTrainerPasswordFailure() {
        ProfileRequest request = new ProfileRequest("trainerName", "newPassword", "");
        doThrow(new RuntimeException("Password change failed")).when(userService).changePassword(request);
        assertThrows(RuntimeException.class, () -> service.changeTrainerPassword(request));
    }

    @Test
    void testUpdateTrainerUsernameAlreadyInUse() {
        String username = "trainerName";
        UpdateTrainerRequest request = UpdateTrainerRequest.builder().username("existingUsername").build();
        Trainer existingTrainer = new Trainer();
        when(trainerDao.findByUsername(username)).thenReturn(Optional.of(existingTrainer));
        when(trainerDao.findByUsername(request.getUsername())).thenReturn(Optional.of(new Trainer()));
        assertThrows(UserAlreadyExistsException.class, () -> service.updateTrainer(username, request));
    }

    @Test
    void testUpdateStatusTrainerProfile() {
        String username = "trainerName";
        Boolean status = true;
        MessageResponse response = service.updateStatusTrainerProfile(username, status);
        assertEquals(String.format("Trainer profile for '%s' has been updated to %s",
                username, status), response.message());
        verify(trainerDao).activateTrainer(username, status);
    }

    @Test
    void testAssignTraineeToTrainerSuccess() {
        String traineeUsername = "Trainee.User";
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("trainerUsername");
        SecurityContextHolder.setContext(securityContext);
        service.assignTraineeToTrainer(traineeUsername);
        verify(trainerDao).assignTraineeToTrainer("trainerUsername", traineeUsername);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainersResponseEntityProfileArgumentsProvider.class)
    void testGetNotAssigned(String username, List<Trainer> trainers, List<TrainerProfile> expectedProfiles) {
        when(trainerDao.findNotAssignedTrainers(username)).thenReturn(trainers);
        when(mapper.toTrainerProfiles(trainers)).thenReturn(expectedProfiles);
        List<TrainerProfile> result = service.getNotAssigned(username);
        assertNotNull(result);
        assertEquals(expectedProfiles.size(), result.size());
        assertEquals(expectedProfiles, result);
        verify(trainerDao).findNotAssignedTrainers(username);
        verify(mapper).toTrainerProfiles(trainers);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void testFindAllSuccess(List<Trainer> trainers, List<TrainerProfile> expectedProfiles) {
        when(trainerDao.findAllWithUsers()).thenReturn(trainers);
        when(mapper.toTrainerProfiles(trainers)).thenReturn(expectedProfiles);
        List<TrainerProfile> result = service.findAll();
        assertNotNull(result);
        assertEquals(expectedProfiles.size(), result.size());
        verify(trainerDao).findAllWithUsers();
        verify(mapper).toTrainerProfiles(trainers);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void testGetNotAssignedTrainersSuccess(List<Trainer> trainers, List<TrainerProfile> expectedProfiles) {
        String traineeUsername = "Harry.Potter";
        when(trainerDao.findNotAssignedTrainers(traineeUsername)).thenReturn(trainers);
        when(mapper.toTrainerProfiles(trainers)).thenReturn(expectedProfiles);
        List<TrainerProfile> result = service.getNotAssigned(traineeUsername);
        assertEquals(expectedProfiles, result);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void testGetActiveTrainersForTrainee(List<Trainer> trainers) {
        Trainee trainee = Trainee.builder().user(User.builder().username("trainer1").build()).build();
        List<TrainerResponseDto> expectedResponses = List.of(
                TrainerResponseDto.builder().username("trainer1").build(),
                TrainerResponseDto.builder().username("trainer2").build()
        );

        when(trainerDao.findNotAssignedTrainers(trainee.getUsername())).thenReturn(trainers);
        when(mapper.toResponseDto(trainers.get(0))).thenReturn(expectedResponses.get(0));
        when(mapper.toResponseDto(trainers.get(1))).thenReturn(expectedResponses.get(1));

        List<TrainerResponseDto> result = service.getActiveTrainersForTrainee(trainee);

        assertNotNull(result);
        assertEquals(expectedResponses.size(), result.size());
        verify(trainerDao).findNotAssignedTrainers(trainee.getUsername());
        verify(mapper, times(2)).toResponseDto(any());
    }


    @ParameterizedTest
    @ArgumentsSource(TrainerArgumentsProvider.class)
    void testGetActiveTrainersForTraineeSuccess(List<Trainer> trainers) {
        Trainee trainee = Trainee.builder().user(User.builder().username("Harry.Potter").build()).build();
        when(trainerDao.findNotAssignedTrainers(trainee.getUsername())).thenReturn(trainers);

        List<TrainerResponseDto> expectedResponses = List.of(
                TrainerResponseDto.builder().username("trainer1").build(),
                TrainerResponseDto.builder().username("trainer2").build()
        );
        when(mapper.toResponseDto(trainers.get(0))).thenReturn(expectedResponses.get(0));
        when(mapper.toResponseDto(trainers.get(1))).thenReturn(expectedResponses.get(1));
        List<TrainerResponseDto> result = service.getActiveTrainersForTrainee(trainee);
        assertEquals(expectedResponses, result);
    }
}
