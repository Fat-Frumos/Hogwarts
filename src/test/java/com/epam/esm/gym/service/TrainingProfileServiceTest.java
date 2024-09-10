package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.mapper.TrainingMapper;
import com.epam.esm.gym.service.profile.TrainingProfileService;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainingArgumentsProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingProfileServiceTest {

    @Mock
    private TrainingDao dao;

    @Mock
    private TrainingMapper mapper;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingProfileService service;

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainingArgumentsProvider.class)
    void testGetTrainerTrainingsByName(
            Map<String, String> params, Trainee trainee,
            Training training, TrainingResponse response) {
        String name = "Severus.Snape";
        when(dao.findTrainingsByTrainerUsername(name)).thenReturn(List.of(training));
        when(mapper.toResponses(List.of(training))).thenReturn(List.of(response));
        ResponseEntity<List<TrainingResponse>> result = service.getTrainerTrainingsByName(name, new TrainingProfile());
        assertEquals(ResponseEntity.ok(List.of(response)), result);
        verify(dao).findTrainingsByTrainerUsername(name);
        verify(mapper).toResponses(List.of(training));
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainingArgumentsProvider.class)
    void testCreateTraining(Map<String, String> params, Trainee trainee, Training training) {
        Trainer trainer = Trainer.builder().user(trainee.getUser()).build();
        TrainingRequest request = new TrainingRequest(
                "Harry.Potter", "Minerva.McGonagall",
                "Advanced Transfiguration", "2024-22-08", 60);
        when(traineeService.getTrainee(request.getTraineeUsername())).thenReturn(Optional.of(trainee));
        when(trainerService.getTrainer(request.getTrainerUsername())).thenReturn(Optional.of(trainer));
        when(mapper.toEntity(request, trainee, trainer)).thenReturn(training);

        service.createTraining(request);

        verify(traineeService).getTrainee(request.getTraineeUsername());
        verify(trainerService).getTrainer(request.getTrainerUsername());
        verify(mapper).toEntity(request, trainee, trainer);
        verify(dao).save(training);
    }
}
