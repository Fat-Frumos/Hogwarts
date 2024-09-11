package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.dao.jpa.TrainingTypeRepository;
import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.PutTrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.mapper.TrainerMapper;
import com.epam.esm.gym.service.profile.TrainerProfileService;
import com.epam.esm.gym.service.profile.UserProfileService;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainerNameArgumentsProvider;
import com.epam.esm.gym.web.provider.trainer.TrainerRequestArgumentsProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerResponseServiceTest {
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private UserProfileService userService;
    @Mock
    private TrainingTypeRepository trainingTypeDao;
    @Mock
    private TrainerMapper mapper;
    @Mock
    private TrainerDao trainerDao;
    @InjectMocks
    private TrainerProfileService service;

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testRegisterTrainer(
            Trainer trainer, TrainerProfile profile,
            TrainerRequest request, ProfileResponse expectedResponse) {

        String rawPassword = "rawPassword";
        String encodedPassword = "encodedPassword";
        Specialization specialization = Specialization.DEFAULT;
        TrainingType trainingType = TrainingType.builder().specialization(specialization).build();
        User user = trainer.getUser();
        when(userService.generateRandomPassword()).thenReturn(rawPassword);
        when(userService.encodePassword(rawPassword)).thenReturn(encodedPassword);
        when(userService.createTrainerUser(request, encodedPassword)).thenReturn(user);
        when(userService.saveUser(user)).thenReturn(user);
        when(trainingTypeDao.findAllBySpecialization(specialization)).thenReturn(List.of(trainingType));
        when(mapper.toTrainer(user, List.of(trainingType))).thenReturn(trainer);
        when(trainerDao.save(trainer)).thenReturn(trainer);
        when(mapper.toProfileDto(user.getUsername(), rawPassword)).thenReturn(expectedResponse);

        ResponseEntity<ProfileResponse> response = service.registerTrainer(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(userService).generateRandomPassword();
        verify(userService).encodePassword(rawPassword);
        verify(userService).createTrainerUser(request, encodedPassword);
        verify(userService).saveUser(user);
        verify(trainingTypeDao).findAllBySpecialization(specialization);
        verify(mapper).toTrainer(user, List.of(trainingType));
        verify(trainerDao).save(trainer);
        verify(mapper).toProfileDto(user.getUsername(), rawPassword);
    }


    @Test
    void testChangeTrainerPassword() {
        ProfileRequest request = ProfileRequest.builder().build();
        service.changeTrainerPassword(request);
        verify(userService).changePassword(request);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testDeleteTrainer(Trainer trainer) {
        String username = trainer.getUser().getUsername();
        when(trainerDao.findByName(username)).thenReturn(Optional.of(trainer));
        doNothing().when(trainerDao).delete(trainer);
        service.deleteTrainer(username);
        verify(trainerDao).delete(trainer);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testGetTrainerProfileByName(Trainer trainer, TrainerProfile profile) {
        String username = trainer.getUser().getUsername();
        when(trainerDao.findByName(username)).thenReturn(Optional.of(trainer));
        when(mapper.toDto(trainer)).thenReturn(profile);
        ResponseEntity<BaseResponse> result = service.getTrainerProfileByName(username);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(profile, result.getBody());
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testUpdateTrainer(Trainer trainer, TrainerProfile profile) {
        PutTrainerRequest request = PutTrainerRequest.builder()
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .active(trainer.getUser().getActive())
                .build();
        ResponseEntity<BaseResponse> actual = service.updateTrainer(trainer.getUser().getUsername(), request);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testGetNotAssigned(Trainer trainer, TrainerProfile profile) {
        List<Trainer> trainers = List.of(trainer);
        List<TrainerProfile> profiles = List.of(profile);
        String username = trainer.getUser().getUsername();
        when(trainerDao.findNotAssigned(username)).thenReturn(trainers);
        when(mapper.toTrainerProfiles(trainers)).thenReturn(profiles);
        ResponseEntity<List<TrainerProfile>> result = service.getNotAssigned(username);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(profiles, result.getBody());
    }

    @Test
    void testActivateDeactivateProfile() {
        String username = "trainer";
        Boolean active = true;
        doNothing().when(trainerDao).activateTrainer(username, active);
        ResponseEntity<Void> result = service.activateDeactivateProfile(username, active);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(trainerDao).activateTrainer(username, active);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testGetTrainerThrowsEntityNotFoundException(Trainer trainer) {
        String username = trainer.getUser().getUsername();
        when(trainerDao.findByName(username)).thenReturn(Optional.empty());
        assertTrue(service.getTrainer(username).isEmpty());
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testFindAll(Trainer trainer, TrainerProfile profile) {
        List<Trainer> trainers = List.of(trainer);
        List<TrainerProfile> profiles = List.of(profile);
        when(trainerDao.findAll()).thenReturn(trainers);
        when(mapper.toTrainerProfiles(trainers)).thenReturn(profiles);
        ResponseEntity<List<TrainerProfile>> result = service.findAll();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(profiles, result.getBody());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    void testAssignTraineeToTrainer(String trainerUsername, String traineeUsername) {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(trainerUsername);
        doNothing().when(trainerDao).assignTraineeToTrainer(trainerUsername, traineeUsername);
        service.assignTraineeToTrainer(traineeUsername);
        verify(trainerDao).assignTraineeToTrainer(trainerUsername, traineeUsername);
    }
}
