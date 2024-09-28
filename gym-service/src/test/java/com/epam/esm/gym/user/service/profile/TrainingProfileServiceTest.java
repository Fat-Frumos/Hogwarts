package com.epam.esm.gym.user.service.profile;

import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.dao.JpaTrainingDao;
import com.epam.esm.gym.user.dao.JpaTrainingTypeDao;
import com.epam.esm.gym.user.dto.training.TrainingProfile;
import com.epam.esm.gym.user.dto.training.TrainingRequest;
import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.Training;
import com.epam.esm.gym.user.entity.TrainingType;
import com.epam.esm.gym.user.entity.User;
import com.epam.esm.gym.user.exception.UserAlreadyExistsException;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.mapper.TrainingMapper;
import com.epam.esm.gym.user.service.TraineeService;
import com.epam.esm.gym.user.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingProfileServiceTest {

    @Mock
    private JpaTrainingDao dao;

    @Mock
    private TrainingMapper mapper;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private JpaTrainingTypeDao typeDao;

    @InjectMocks
    private TrainingProfileService service;

    @Test
    void testCreateTrainingTraineeNotFounds() {
        TrainingRequest request = TrainingRequest.builder()
                .traineeUsername("NonExistentTrainee")
                .trainerUsername("TrainerX")
                .trainingName("New Training")
                .build();
        when(traineeService.getTrainee("NonExistentTrainee")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createTraining(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Trainee not found for username: NonExistentTrainee");
    }


    @Test
    void testCreateTrainingTrainingAlreadyExist() {
        TrainingRequest request = TrainingRequest.builder()
                .traineeUsername("Harry.Potter")
                .trainerUsername("TrainerX")
                .trainingName("Existing Training")
                .build();
        when(traineeService.getTrainee("Harry.Potter")).thenReturn(Optional.of(Trainee.builder().build()));
        when(trainerService.getTrainer("TrainerX")).thenReturn(Optional.of(Trainer.builder().build()));
        when(dao.findByTrainingName("Existing Training")).thenReturn(Optional.of(Training.builder().build()));

        assertThatThrownBy(() -> service.createTraining(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("Training already exists: Existing Training");
    }

    @Test
    void testCreateTrainingUnSuccess() {
        String trainerUsername = "Minerva.McGonagall";
        String traineeUsername = "Harry.Potter";
        String trainingName = "Advanced Transfiguration";
        TrainingRequest request = new TrainingRequest(trainerUsername, traineeUsername,
                trainingName, 60, LocalDate.parse("2024-02-08"));
        Exception exception = assertThrows(UserNotFoundException.class, () -> service.createTraining(request));
        assertEquals("Trainee not found for username: Minerva.McGonagall", exception.getMessage());
    }

    @Test
    void testCreateTrainingTrainingAlreadyExists() {
        TrainingRequest request = new TrainingRequest("trainer", "trainee",
                "existingTraining", 60, LocalDate.parse("2024-02-08"));
        Trainer trainer = Trainer.builder()
                .user(new User())
                .trainingType(TrainingType.builder().specialization(Specialization.DEFENSE).build())
                .build();
        Trainee trainee = new Trainee();

        when(traineeService.getTrainee(request.getTraineeUsername())).thenReturn(Optional.of(trainee));
        when(trainerService.getTrainer(request.getTrainerUsername())).thenReturn(Optional.of(trainer));
        when(dao.findByTrainingName(request.getTrainingName())).thenReturn(Optional.of(new Training()));

        assertThatThrownBy(() -> service.createTraining(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("Training already exists: existingTraining");
    }

    @Test
    void testCreateTrainingTrainerNotFound() {
        String trainerUsername = "Minerva.McGonagall";
        TrainingRequest request = new TrainingRequest(
                "Harry.Potter", trainerUsername, "Advanced Transfiguration", 60,
                LocalDate.parse("2024-02-08"));

        when(traineeService.getTrainee("Harry.Potter")).thenReturn(Optional.of(new Trainee()));
        when(trainerService.getTrainer(trainerUsername)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createTraining(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Trainer not found for username: Minerva.McGonagall");

        verify(traineeService).getTrainee("Harry.Potter");
        verifyNoInteractions(mapper, dao);
    }

    @Test
    void testGetAllTrainings() {
        List<Training> trainings = List.of(new Training(), new Training());
        List<TrainingResponse> expectedResponses = List.of(new TrainingResponse(), new TrainingResponse());

        when(dao.findAll()).thenReturn(trainings);

        List<TrainingResponse> result = service.getAllTrainings();

        assertNotNull(result);
        assertEquals(expectedResponses.size(), result.size());
        verify(dao).findAll();
    }

    @Test
    void testGetTrainingTypesHappyPath() {
        List<TrainingType> trainingTypes = List.of(TrainingType.builder()
                .specialization(Specialization.DEFENSE).build());
        when(typeDao.findAll()).thenReturn(trainingTypes);

        List<TrainingTypeDto> result = service.getTrainingTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetTrainingTypesEmptyList() {
        when(typeDao.findAll()).thenReturn(Collections.emptyList());
        List<TrainingTypeDto> result = service.getTrainingTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTrainerTrainingsByNameTrainerNotFound() {
        TrainingProfile profile = TrainingProfile.builder().trainerName("NonExistentTrainer").build();

        when(trainerService.getTrainer("NonExistentTrainer")).thenReturn(Optional.empty());

        List<TrainingResponse> result = service.getTrainerTrainingsByName(profile);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trainerService).getTrainer("NonExistentTrainer");
        verifyNoInteractions(dao);
    }

    @Test
    void testGetAllTrainingsHappyPath() {
        List<Training> trainings = List.of(new Training());
        when(dao.findAll()).thenReturn(trainings);

        List<TrainingResponse> result = service.getAllTrainings();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(dao).findAll();
    }

    @Test
    void testGetAllTrainingsEmptyList() {
        when(dao.findAll()).thenReturn(Collections.emptyList());

        List<TrainingResponse> result = service.getAllTrainings();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveTrainingTypeHappyPath() {
        TrainingType type = TrainingType.builder().specialization(Specialization.DEFENSE).build();
        when(typeDao.save(type)).thenReturn(type);

        TrainingType actual = service.save(type);

        assertNotNull(actual);
        assertEquals(Specialization.DEFENSE, actual.getSpecialization());
        verify(typeDao).save(type);
    }

    @Test
    void testGetTrainerTrainingsByNameHappyPath() {
        String trainerName = "Severus.Snape";
        TrainingProfile profile = TrainingProfile.builder().trainerName(trainerName).build();

        Trainee trainee = Trainee.builder()
                .user(User.builder().username("Harry.Potter").build())
                .build();

        Trainer trainer = Trainer.builder()
                .user(User.builder().username(trainerName).build())
                .trainingType(TrainingType.builder().specialization(Specialization.DEFENSE).build())
                .trainees(Set.of(trainee))
                .build();

        when(trainerService.getTrainer(trainerName)).thenReturn(Optional.of(trainer));
        when(dao.findTrainingsByTrainerName(anyString(), any(), any()))
                .thenReturn(List.of(new Training()));

        List<TrainingResponse> result = service.getTrainerTrainingsByName(profile);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(trainerService).getTrainer(trainerName);
        verify(dao).findTrainingsByTrainerName(trainerName, profile.getPeriodFrom(), profile.getPeriodTo());
    }
}
