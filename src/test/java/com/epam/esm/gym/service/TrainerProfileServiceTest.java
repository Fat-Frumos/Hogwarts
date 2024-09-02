package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.esm.gym.mapper.TrainerMapper;
import com.epam.esm.gym.web.provider.trainer.TrainerRequestArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainerNameArgumentsProvider;
import jakarta.persistence.EntityNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerProfileServiceTest {
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private UserProfileService userService;

    @Mock
    private TrainerMapper mapper;

    @Mock
    private TrainerDao dao;

    @InjectMocks
    private TrainerProfileService service;

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testRegisterTrainer(Trainer trainer, TrainerProfile profile, TrainerRequest request, ProfileResponse response) {
        when(userService.saveTrainer(request)).thenReturn(profile);
        when(mapper.toEntity(profile)).thenReturn(trainer);
        when(dao.save(trainer)).thenReturn(trainer);
        when(mapper.toProfileDto(trainer.getUser())).thenReturn(response);
        ResponseEntity<ProfileResponse> result = service.registerTrainer(request);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
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
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainer));
        doNothing().when(dao).delete(trainer);
        service.deleteTrainer(username);
        verify(dao).delete(trainer);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testGetTrainerProfileByName(Trainer trainer, TrainerProfile profile) {
        String username = trainer.getUser().getUsername();
        when(dao.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(mapper.toDto(trainer)).thenReturn(profile);
        ResponseEntity<TrainerProfile> result = service.getTrainerProfileByName(username);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(profile, result.getBody());
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testUpdateTrainer(Trainer trainer, TrainerProfile profile) {
        TrainerUpdateRequest request = TrainerUpdateRequest.builder().build();
        when(mapper.toEntity(request)).thenReturn(trainer);
        when(dao.update(trainer)).thenReturn(trainer);
        when(mapper.toDto(trainer)).thenReturn(profile);
        ResponseEntity<TrainerProfile> result = service.updateTrainer(trainer.getUser().getUsername(), request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(profile, result.getBody());
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testGetNotAssigned(Trainer trainer, TrainerProfile profile) {
        List<Trainer> trainers = List.of(trainer);
        List<TrainerProfile> profiles = List.of(profile);
        String username = trainer.getUser().getUsername();
        when(dao.findNotAssigned(username)).thenReturn(trainers);
        when(mapper.toDtos(trainers)).thenReturn(profiles);
        ResponseEntity<List<TrainerProfile>> result = service.getNotAssigned(username);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(profiles, result.getBody());
    }

    @Test
    void testActivateDeactivateProfile() {
        String username = "trainer";
        Boolean active = true;
        doNothing().when(dao).activateTrainer(username, active);
        ResponseEntity<Void> result = service.activateDeactivateProfile(username, active);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(dao).activateTrainer(username, active);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testGetTrainerThrowsEntityNotFoundException(Trainer trainer) {
        String username = trainer.getUser().getUsername();
        when(dao.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getTrainer(username));
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerRequestArgumentsProvider.class)
    void testFindAll(Trainer trainer, TrainerProfile profile) {
        List<Trainer> trainers = List.of(trainer);
        List<TrainerProfile> profiles = List.of(profile);
        when(dao.findAll()).thenReturn(trainers);
        when(mapper.toDtos(trainers)).thenReturn(profiles);
        ResponseEntity<List<TrainerProfile>> result = service.findAll();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(profiles, result.getBody());
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainerNameArgumentsProvider.class)
    public void testAssignTraineeToTrainer(String trainerUsername, String traineeUsername) {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(trainerUsername);
        doNothing().when(dao).assignTraineeToTrainer(trainerUsername, traineeUsername);
        service.assignTraineeToTrainer(traineeUsername);
        verify(dao).assignTraineeToTrainer(trainerUsername, traineeUsername);
    }

}
