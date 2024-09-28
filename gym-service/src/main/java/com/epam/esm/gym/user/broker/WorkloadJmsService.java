package com.epam.esm.gym.user.broker;

import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.epam.esm.gym.jms.ActiveMQConfig.ERROR_QUEUE;

/**
 * Service for handling JMS (Java Message Service) operations related to trainer workloads.
 * It supports sending messages to destinations, including sending and receiving responses,
 * and converting objects to JSON using Jackson's ObjectMapper.
 */
@Slf4j
@Service
@AllArgsConstructor
public class WorkloadJmsService {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Sends a simple string message to the specified JMS destination.
     *
     * @param destination the JMS queue or topic to send the message to
     * @param message     the message to send
     */
    @Transactional
    @CircuitBreaker(openTimeout = 5000, resetTimeout = 10000)
    public void convertAndSend(String destination, String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    /**
     * Converts a DTO object to JSON and sends it to the specified JMS destination.
     *
     * @param destination the JMS queue or topic to send the message to
     * @param dto         the object to convert to JSON and send
     */
    @Transactional
    public <T> void convertAndSend(String destination, T dto) {
        String json = getJson(dto);
        if (json != null) {
            convertAndSend(destination, json);
        }
    }

    /**
     * Converts a list of {@link TrainerProfile} objects to JSON and sends it to the specified JMS destination.
     *
     * @param destination the JMS queue or topic to send the message to
     * @param profiles    the list of trainer profiles to convert to JSON and send
     */
    @Transactional
    public void convertAndSend(String destination, List<TrainerProfile> profiles) {
        jmsTemplate.convertAndSend(destination, getJson(profiles));
    }

    /**
     * Converts an object to its JSON representation using the {@link ObjectMapper}.
     * If the conversion fails, an error message is logged and sent to an error queue.
     *
     * @param dto the object to convert to JSON
     * @return the JSON representation of the object, or {@code null} if the conversion fails
     * @throws IllegalArgumentException if the conversion fails
     */
    @Transactional
    public <T> String getJson(T dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            convertAndSend(ERROR_QUEUE, e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
