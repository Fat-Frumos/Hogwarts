package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.dao.jpa.TrainingTypeRepository;
import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.auth.MessageResponse;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeDto;
import com.epam.esm.gym.mapper.TrainingMapper;
import com.epam.esm.gym.service.profile.TrainingProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingProfileServiceTest {

    @Mock
    private TrainingDao dao;

    @Mock
    private TrainingTypeRepository typeRepository;

    @Mock
    private TrainingMapper mapper;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingProfileService service;

    @Test
    void testGetTrainingTypes() {
        List<TrainingTypeDto> result = service.getTrainingTypes();
        assertNotNull(result);
    }

    @Test
    void testCreateTrainingSuccess() {
        TrainingRequest request = new TrainingRequest(
                "trainer", "trainee", "trainingName", "2024-08-22", 60);
        Trainer trainer = Trainer.builder()
                .user(new User())
                .trainingTypes(List.of(TrainingType.builder().specialization(Specialization.DEFENSE).build()))
                .build();
        Trainee trainee = new Trainee();
        Training training = new Training();
        TrainingResponse response = new TrainingResponse(
                "trainer", "trainingName", "TRANSFIGURATION", 60, null);

        when(traineeService.getTrainee(request.getTraineeUsername())).thenReturn(Optional.of(trainee));
        when(trainerService.getTrainer(request.getTrainerUsername())).thenReturn(Optional.of(trainer));
        when(dao.findByName(request.getTrainingName())).thenReturn(Optional.empty());
        when(mapper.toEntity(request, trainee, trainer)).thenReturn(training);
        when(typeRepository.findBySpecialization(any(Specialization.class)))
                .thenReturn(TrainingType.builder().specialization(Specialization.DEFENSE).build());
        when(dao.save(training)).thenReturn(training);
        when(mapper.toDto(training)).thenReturn(response);

        ResponseEntity<BaseResponse> actualResponse = service.createTraining(request);

        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        verify(traineeService).getTrainee(request.getTraineeUsername());
        verify(trainerService).getTrainer(request.getTrainerUsername());
        verify(dao).findByName(request.getTrainingName());
        verify(typeRepository).findBySpecialization(any(Specialization.class));
        verify(dao).save(training);
        verify(mapper).toDto(training);
    }

    @Test
    void testGetTrainerTrainingsByName() {
        String trainerName = "Severus.Snape";
        TrainingProfile profile = new TrainingProfile();
        profile.setTrainerName(trainerName);

        Trainer trainer = Trainer.builder()
                .user(User.builder().username(trainerName).build())
                .build();

        when(trainerService.getTrainer(trainerName)).thenReturn(Optional.of(trainer));

        ResponseEntity<List<TrainingResponse>> result = service.getTrainerTrainingsByName(profile);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(trainerService).getTrainer(trainerName);
    }

    @Test
    void testCreateTraining_Success() {
        TrainingRequest request = new TrainingRequest(
                "trainer", "trainee", "trainingName", "2024-08-22", 60);
        Trainer trainer = Trainer.builder()
                .user(new User())
                .trainingTypes(List.of(TrainingType.builder().specialization(Specialization.DEFENSE).build()))
                .build();
        Trainee trainee = new Trainee();
        Training training = new Training();
        TrainingResponse response = new TrainingResponse("trainer", "trainingName",
                "TRANSFIGURATION", 60, LocalDate.of(2024, 8, 22));

        when(traineeService.getTrainee(request.getTraineeUsername())).thenReturn(Optional.of(trainee));
        when(trainerService.getTrainer(request.getTrainerUsername())).thenReturn(Optional.of(trainer));
        when(dao.findByName(request.getTrainingName())).thenReturn(Optional.empty());
        when(mapper.toEntity(request, trainee, trainer)).thenReturn(training);
        when(typeRepository.findBySpecialization(any(Specialization.class))).thenReturn(new TrainingType());
        when(dao.save(training)).thenReturn(training);
        when(mapper.toDto(training)).thenReturn(response);

        ResponseEntity<BaseResponse> actualResponse = service.createTraining(request);

        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(response, actualResponse.getBody());
    }

    @Test
    void testCreateTraining_TraineeNotFound() {
        TrainingRequest request = new TrainingRequest("trainer",
                "nonexistentTrainee", "trainingName", "2024-08-22", 60);

        when(traineeService.getTrainee(request.getTraineeUsername())).thenReturn(Optional.empty());

        ResponseEntity<BaseResponse> actualResponse = service.createTraining(request);

        assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
        assertEquals("Trainee not found for username: trainer",
                ((MessageResponse) Objects.requireNonNull(actualResponse.getBody())).getMessage());
    }

    @Test
    void testCreateTraining_TrainerNotFound() {
        TrainingRequest request = new TrainingRequest(
                "nonexistentTrainer",
                "trainee", "trainingName", "2024-08-22", 60);
        Trainee trainee = new Trainee();

        when(traineeService.getTrainee(request.getTraineeUsername())).thenReturn(Optional.of(trainee));
        when(trainerService.getTrainer(request.getTrainerUsername())).thenReturn(Optional.empty());

        ResponseEntity<BaseResponse> actualResponse = service.createTraining(request);

        assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
        assertEquals("Trainer not found for username: trainee",
                ((MessageResponse) Objects.requireNonNull(actualResponse.getBody())).getMessage());
    }

    @Test
    void testCreateTraining_TrainingAlreadyExists() {
        TrainingRequest request = new TrainingRequest("trainer",
                "trainee", "existingTraining", "2024-08-22", 60);
        Trainer trainer = Trainer.builder()
                .user(new User())
                .trainingTypes(List.of(TrainingType.builder().specialization(Specialization.DEFENSE).build()))
                .build();
        Trainee trainee = new Trainee();

        when(traineeService.getTrainee(request.getTraineeUsername())).thenReturn(Optional.of(trainee));
        when(trainerService.getTrainer(request.getTrainerUsername())).thenReturn(Optional.of(trainer));
        when(dao.findByName(request.getTrainingName())).thenReturn(Optional.of(new Training()));

        ResponseEntity<BaseResponse> actualResponse = service.createTraining(request);

        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
        assertEquals("Training already exists: existingTraining",
                ((MessageResponse) Objects.requireNonNull(actualResponse.getBody())).getMessage());
    }

    @Test
    void testCreateTraining_TrainerHasNoTrainingTypes() {
        TrainingRequest request = new TrainingRequest("trainer",
                "trainee", "trainingName", "2024-08-22", 60);
        Trainer trainer = Trainer.builder()
                .user(new User())
                .trainingTypes(Collections.emptyList())
                .build();
        Trainee trainee = new Trainee();

        when(traineeService.getTrainee(request.getTraineeUsername())).thenReturn(Optional.of(trainee));
        when(trainerService.getTrainer(request.getTrainerUsername())).thenReturn(Optional.of(trainer));
        when(dao.findByName(request.getTrainingName())).thenReturn(Optional.empty());

        ResponseEntity<BaseResponse> actualResponse = service.createTraining(request);

        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
        assertEquals("Trainer has no training types available.",
                ((MessageResponse) Objects.requireNonNull(actualResponse.getBody())).getMessage());
    }

    @Test
    void testCreateTrainingTrainingAlreadyExists() {
        TrainingRequest request = new TrainingRequest("trainer",
                "trainee", "trainingName", "2024-08-22", 60);
        when(traineeService.getTrainee(request.getTraineeUsername())).thenReturn(Optional.of(new Trainee()));
        when(trainerService.getTrainer(request.getTrainerUsername())).thenReturn(Optional.of(new Trainer()));
        when(dao.findByName(request.getTrainingName())).thenReturn(Optional.of(new Training()));

        ResponseEntity<BaseResponse> response = service.createTraining(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Training already exists: trainingName",
                ((MessageResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    void testCreateTrainingTrainerHasNoTrainingTypes() {
        TrainingRequest request = new TrainingRequest("trainer",
                "trainee", "trainingName", "2024-08-22", 60);
        Trainer trainer = Trainer.builder().user(new User()).trainingTypes(List.of()).build();
        when(traineeService.getTrainee(request.getTraineeUsername())).thenReturn(Optional.of(new Trainee()));
        when(trainerService.getTrainer(request.getTrainerUsername())).thenReturn(Optional.of(trainer));
        when(dao.findByName(request.getTrainingName())).thenReturn(Optional.empty());

        ResponseEntity<BaseResponse> response = service.createTraining(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Trainer has no training types available.",
                ((MessageResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }


    @Test
    void testCreateTrainingTraineeNotFound() {
        String trainerUsername = "Minerva.McGonagall";
        TrainingRequest request = new TrainingRequest(
                "Harry.Potter", trainerUsername,
                "Advanced Transfiguration", "2024-22-08", 60);

        when(traineeService.getTrainee("Harry.Potter")).thenReturn(Optional.empty());

        ResponseEntity<BaseResponse> response = service.createTraining(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trainee not found for username: Harry.Potter",
                ((MessageResponse) Objects.requireNonNull(response.getBody())).getMessage());

        verify(traineeService).getTrainee("Harry.Potter");
        verifyNoInteractions(trainerService, mapper, dao);
    }

    @Test
    void testCreateTrainingTrainerNotFound() {
        String trainerUsername = "Minerva.McGonagall";
        TrainingRequest request = new TrainingRequest(
                "Harry.Potter", trainerUsername,
                "Advanced Transfiguration", "2024-22-08", 60);

        when(traineeService.getTrainee("Harry.Potter")).thenReturn(Optional.of(new Trainee()));
        when(trainerService.getTrainer(trainerUsername)).thenReturn(Optional.empty());

        ResponseEntity<BaseResponse> response = service.createTraining(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trainer not found for username: Minerva.McGonagall",
                ((MessageResponse) Objects.requireNonNull(response.getBody())).getMessage());

        verify(traineeService).getTrainee("Harry.Potter");
        verify(trainerService).getTrainer(trainerUsername);
        verifyNoInteractions(mapper, dao);
    }
}
