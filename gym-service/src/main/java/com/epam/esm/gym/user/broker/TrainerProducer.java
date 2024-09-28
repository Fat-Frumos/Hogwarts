package com.epam.esm.gym.user.broker;

import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.jms.dto.WorkloadRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.service.TrainerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.epam.esm.gym.jms.ActiveMQConfig.ERROR_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.TRAINERS_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.TRAINER_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.USERNAME_QUEUE;

/**
 * Service for publishing trainer information to a message broker.
 * This class handles operations related to sending trainer profiles to specified JMS queues.
 */
@Slf4j
@Service
@AllArgsConstructor
public class TrainerProducer {

    private final WorkloadJmsService workloadService;
    private final TrainerService trainerService;

    /**
     * {@inheritDoc}
     * Publishes the specified trainer's information to a broker for processing.
     *
     * @param username the username of the trainer to be published
     * @throws com.epam.esm.gym.user.exception.UserNotFoundException if no trainer is found with the specified username
     */
    @JmsListener(destination = USERNAME_QUEUE, containerFactory = "jmsListenerContainerFactory")
    public void publishTrainer(String username) {
        validate(username);
        try {
            TrainerProfile trainer = trainerService.getTrainerProfileByName(username);
            workloadService.convertAndSend(TRAINER_QUEUE, trainer);
            log.info("Publish successfully as Json {}", trainer);
        } catch (UserNotFoundException e) {
            String message = "User Not Found by name " + username;
            workloadService.convertAndSend(ERROR_QUEUE, message);
            log.error(message);
        }
    }

    /**
     * Publishes a list of trainers to a specified queue every Monday at midnight.
     * If no trainers are found, an error message is sent to the dead letter queue.
     * This method is scheduled to run automatically at the specified cron expression.
     */
    @Scheduled(cron = "0 0 0 * * MON")
    public void publishTrainerList() {
        List<TrainerProfile> profiles = trainerService.findAll();
        if (profiles.isEmpty()) {
            workloadService.convertAndSend(ERROR_QUEUE, "Users Not Found");
            log.error("Users Not Found");
        } else {
            workloadService.convertAndSend(TRAINERS_QUEUE, profiles);
            log.info("Publish successfully as Json {}", profiles);
        }
    }

    /**
     * Validates the given username to ensure it is not null or blank.
     * If the validation fails, an error message is sent to the error queue, and an exception is thrown.
     *
     * @param username the username to be validated
     * @throws IllegalArgumentException if the username is null or blank
     */
    private void validate(String username) {
        if (username == null || username.isBlank()) {
            workloadService.convertAndSend(ERROR_QUEUE, "Invalid username received");
            log.error("Invalid username sent to dead letter queue");
            throw new IllegalArgumentException("Invalid username received");
        }
    }

    /**
     * Publishes a TrainingResponse request to the specified JMS queue.
     *
     * @param request the TrainingResponse to be published. This must contain the necessary
     *                details regarding the TrainingResponse If this parameter is null,
     *                an error message is sent to the error queue.
     * @throws IllegalArgumentException if the request is null, though the method handles
     *                                  this by sending an error message to the error queue
     *                                  instead of throwing an exception.
     */
    public void publishTrainingResponse(String destination, TrainingResponse request) {
        if (request != null) {
            workloadService.convertAndSend(destination, request);
        } else {
            workloadService.convertAndSend(ERROR_QUEUE, "Workload Request cannot be null");
        }
    }

    /**
     * Publishes a workload request to the specified JMS queue.
     *
     * @param request the workload request to be published. This must contain the necessary
     *                details regarding the training workload. If this parameter is null,
     *                an error message is sent to the error queue.
     */
    public void publishWorkloadResponse(WorkloadRequest request) {
        workloadService.convertAndSend(request.actionType().getQueue(), request);
    }
}
