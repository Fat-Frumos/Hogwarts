package com.epam.esm.gym.workload.service;

import com.epam.esm.gym.jms.dto.TrainerProfile;
import com.epam.esm.gym.jms.dto.TrainerStatus;
import com.epam.esm.gym.jms.dto.TrainerSummary;
import com.epam.esm.gym.jms.dto.TrainerWorkloadResponse;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.jms.dto.WorkloadRequest;
import com.epam.esm.gym.workload.dao.InMemoryDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.gym.jms.ActiveMQConfig.ADD_TRAINING_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.DELETE_TRAINING_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.ERROR_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.TRAINERS_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.TRAINER_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.TRAINER_SUMMARY_QUEUE;

/**
 * WarehouseReceiveService is responsible for receiving TrainerProfile messages
 * from the trainer queue and processing them.
 */
@Slf4j
@Service
@AllArgsConstructor
public class TrainerReceiverService {

    private final InMemoryDao dao;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Receives a trainer summary from the JMS queue and saves it to the database.
     *
     * @param jsonTrainerSummary the JSON string representation of the trainer summary
     */
    @JmsListener(destination = TRAINER_SUMMARY_QUEUE, containerFactory = "jmsListenerContainerFactory")
    public void receiveTrainerSummary(String jsonTrainerSummary) {
        try {
            TrainerSummary trainerSummary = objectMapper.readValue(jsonTrainerSummary, TrainerSummary.class);
            dao.saveSummary(trainerSummary);
            log.info("Trainer summary saved: {}", trainerSummary);
        } catch (Exception e) {
            log.error("Unexpected error in listener method: {}", e.getMessage());
            jmsTemplate.convertAndSend(ERROR_QUEUE, e.getMessage());
        }
    }

    /**
     * Listens for ADD training requests from the specified JMS queue.
     *
     * @param json the JSON string containing the WorkloadRequest details
     */
    @JmsListener(destination = ADD_TRAINING_QUEUE, containerFactory = "jmsListenerContainerFactory")
    @CircuitBreaker(openTimeout = 5000, resetTimeout = 10000)
    public void listenAddTraining(String json) {
        log.info("Received ADD training request");
        try {
            WorkloadRequest request = objectMapper.readValue(json, WorkloadRequest.class);
            dao.saveWorkload(request);
            log.info("Successfully added training session for trainer: {}", request.trainerUsername());
        } catch (Exception e) {
            jmsTemplate.convertAndSend(ERROR_QUEUE, "Failed to process ADD training request");
            log.error("Failed to process ADD training request {}", e.getMessage());
        }
    }

    /**
     * Listens for DELETE training requests from the specified JMS queue.
     *
     * @param json the WorkloadRequest containing training session details
     */
    @JmsListener(destination = DELETE_TRAINING_QUEUE, containerFactory = "jmsListenerContainerFactory")
    @CircuitBreaker(openTimeout = 5000, resetTimeout = 10000)
    public void listenDeleteTraining(String json) {
        log.info("Received DELETE training request for trainer: {}", json);
        try {
            WorkloadRequest request = new ObjectMapper().readValue(json, WorkloadRequest.class);
            dao.removeWorkload(request);
            log.info("Successfully deleted training session for trainer: {}", request.trainerUsername());
        } catch (Exception e) {
            jmsTemplate.convertAndSend(ERROR_QUEUE, "Failed to process ADD training request");
            log.error("Failed to process DELETE training request for trainer: {}", e.getMessage());
        }
    }

    /**
     * Receives TrainerProfile messages from the queue asynchronously.
     *
     * @param trainerProfileJson the received TrainerProfile object
     */
    @JmsListener(destination = TRAINER_QUEUE, containerFactory = "jmsListenerContainerFactory")
    public void receive(String trainerProfileJson) {
        if (trainerProfileJson == null) {
            log.error("Username cannot be null");
            jmsTemplate.convertAndSend(ERROR_QUEUE, "Username cannot be null");
            return;
        }
        try {
            TrainerProfile trainerProfile = objectMapper.readValue(trainerProfileJson, TrainerProfile.class);
            dao.save(trainerProfile);
            log.info("WarehouseService received {}", trainerProfile);
        } catch (JsonProcessingException e){
            jmsTemplate.convertAndSend(ERROR_QUEUE, e.getMessage());
        }
    }

    /**
     * Receives a list of TrainerProfile objects from the TRAINER_LIST_QUEUE,
     * processes each trainer by generating a weekly report.
     *
     * @param trainerProfiles the list of TrainerProfile objects received from the queue
     */
    @JmsListener(destination = TRAINERS_QUEUE, containerFactory = "jmsListenerContainerFactory")
    public void receiveTrainerProfileList(List<TrainerProfile> trainerProfiles) {
        if (trainerProfiles == null || trainerProfiles.isEmpty()) {
            jmsTemplate.convertAndSend(ERROR_QUEUE, "Invalid List of TrainerProfile received");
        } else {
            dao.save(trainerProfiles);
        }
    }

    /**
     * Retrieves all trainers and converts them into a list of TrainerWorkloadResponse objects.
     *
     * @return a list of TrainerWorkloadResponse objects representing all trainers
     */
    public List<TrainerWorkloadResponse> findAllTrainers() {
        return dao.findAll().stream()
                .map(this::toTrainerWorkloadResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts a TrainerProfile object to a TrainerWorkloadResponse object.
     *
     * @param profile the TrainerProfile object to be converted
     * @return a TrainerWorkloadResponse object containing the trainer's workload summary
     */
    private TrainerWorkloadResponse toTrainerWorkloadResponse(TrainerProfile profile) {
        Map<Integer, Map<Month, Long>> yearMonthDurations = profile.trainings().stream()
                .collect(Collectors.groupingBy(
                        training -> training.getTrainingDate().getYear(),
                        Collectors.groupingBy(
                                training -> training.getTrainingDate().getMonth(),
                                Collectors.summingLong(TrainingResponse::getTrainingDuration)
                        )
                ));

        List<TrainerWorkloadResponse.YearSummaryResponse> yearSummaries = yearMonthDurations.entrySet()
                .stream()
                .map(entry -> new TrainerWorkloadResponse.YearSummaryResponse(
                        entry.getKey(),
                        entry.getValue().entrySet().stream()
                                .map(monthEntry -> new TrainerWorkloadResponse.YearSummaryResponse
                                        .MonthSummaryResponse(monthEntry.getKey(), monthEntry.getValue()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        TrainerStatus trainerStatus = profile.active() ? TrainerStatus.ACTIVE : TrainerStatus.INACTIVE;

        return TrainerWorkloadResponse.builder()
                .username(profile.username())
                .firstName(profile.firstName())
                .lastName(profile.lastName())
                .trainerStatus(trainerStatus)
                .summary(yearSummaries)
                .build();
    }
}
