package com.epam.esm.gym.user.broker;

import com.epam.esm.gym.jms.dto.ActionType;
import com.epam.esm.gym.jms.dto.WorkloadRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.provider.ValidWorkloadRequestArgumentsProvider;
import com.epam.esm.gym.user.provider.trainee.TraineeTrainersResponseEntityProfileArgumentsProvider;
import com.epam.esm.gym.user.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static com.epam.esm.gym.jms.ActiveMQConfig.ERROR_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.TRAINERS_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.TRAINER_QUEUE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerProducerTest {
    @Mock
    private WorkloadJmsService workloadService;
    @Mock
    private TrainerService trainerService;
    @InjectMocks
    private TrainerProducer trainerProducer;

    @ParameterizedTest
    @ArgumentsSource(TraineeTrainersResponseEntityProfileArgumentsProvider.class)
    void testPublishTrainer_validUsername(String username, List<Trainer> trainers,
                                          List<TrainerProfile> updatedTrainers) {
        TrainerProfile profile = updatedTrainers.get(0);
        when(trainerService.getTrainerProfileByName(username)).thenReturn(profile);
        trainerProducer.publishTrainer(username);
        verify(trainerService).getTrainerProfileByName(username);
        verify(workloadService).convertAndSend(TRAINER_QUEUE, profile);
    }

    @Test
    void testPublishTrainer_invalidUsername() {
        assertThrows(IllegalArgumentException.class, () -> trainerProducer.publishTrainer(" "));
        verify(workloadService).convertAndSend(ERROR_QUEUE, "Invalid username received");
    }

    @Test
    void testPublishTrainer_trainerNotFound() {
        String username = "Severus.Snape";
        when(trainerService.getTrainerProfileByName(username)).thenThrow(new UserNotFoundException(""));
        trainerProducer.publishTrainer(username);
        verify(workloadService).convertAndSend(ERROR_QUEUE, "User Not Found by name " + username);
    }

    @Test
    void testPublishTrainerListWithTrainers() {
        List<TrainerProfile> profiles = List.of(
                TrainerProfile.builder().username("Minerva.McGonagall").build(),
                TrainerProfile.builder().username("Remus.Lupin").build());
        when(trainerService.findAll()).thenReturn(profiles);
        trainerProducer.publishTrainerList();
        verify(workloadService).convertAndSend(TRAINERS_QUEUE, profiles);
    }

    @Test
    void testPublishTrainerListNoTrainers() {
        when(trainerService.findAll()).thenReturn(Collections.emptyList());
        trainerProducer.publishTrainerList();
        verify(workloadService).convertAndSend(ERROR_QUEUE, "Users Not Found");
    }

    @ParameterizedTest
    @ArgumentsSource(ValidWorkloadRequestArgumentsProvider.class)
    void testPublishTrainingResponseWithRequest(WorkloadRequest request) {
        String expectedQueue = request.actionType().getQueue();
        trainerProducer.publishWorkloadResponse(request);
        verify(workloadService).convertAndSend(expectedQueue, request);
    }

    @Test
    void testPublishTrainingResponseNullRequest() {
        trainerProducer.publishTrainingResponse(TRAINER_QUEUE, null);
        verify(workloadService).convertAndSend(ERROR_QUEUE, "Workload Request cannot be null");
    }

    @ParameterizedTest
    @ArgumentsSource(ValidWorkloadRequestArgumentsProvider.class)
    void testPublishWorkloadResponseWithRequest(WorkloadRequest request) {
        ActionType actionType = mock(ActionType.class);
        when(actionType.getQueue()).thenReturn(TRAINER_QUEUE);

        WorkloadRequest mockRequest = new WorkloadRequest(
                request.trainerUsername(),
                request.trainerFirstName(),
                request.trainerLastName(),
                request.status(),
                request.trainingDate(),
                request.trainingDuration(),
                actionType
        );

        trainerProducer.publishWorkloadResponse(mockRequest);
        verify(workloadService).convertAndSend(TRAINER_QUEUE, mockRequest);
    }
}
