package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Initializes and populates in-memory storage maps for different entity types.
 *
 * <p>This class loads data from a JSON file into in-memory storage maps for trainers, trainees, trainings, and users.
 * It uses the {@link ObjectMapper} to parse the JSON content into {@link StorageData} objects and populates the
 * corresponding maps with the parsed data. The class is annotated with {@link Component} to be managed by the Spring
 * container and uses {@link PostConstruct} to perform initialization after the bean's properties have been set.</p>
 */
@Slf4j
@Getter
@Component
@AllArgsConstructor
public class StorageInitializer {

    private final Map<String, Trainer> trainerStorage;
    private final Map<String, Trainee> traineeStorage;
    private final Map<String, Training> trainingStorage;
    private final Map<String, User> userStorage;

    /**
     * Loads data from a JSON file and initializes in-memory storage maps.
     *
     * <p>This method reads a JSON file located at "json/data.json" and parses it using {@link ObjectMapper}.
     * It then populates the in-memory storage maps for trainers, trainees, trainings, and users with the data
     * extracted from the JSON file. If the file is not found or an error occurs during reading or parsing,
     * an error message is logged. This method is executed automatically after the bean's properties have been set.</p>
     */
    @PostConstruct
    public void init() {
        URL resourceUrl = getClass().getClassLoader().getResource("json/data.json");
        if (resourceUrl == null) {
            log.error("Resource not found: {}", "json/data.json");
            return;
        }
        try {
            String content = new String(Files.readAllBytes(Paths.get(resourceUrl.toURI())));
            StorageData storageData = new ObjectMapper().readValue(content, StorageData.class);
            storageData.getUsers().forEach(user -> userStorage.put(user.getUsername(), user));
            storageData.getTrainers().forEach(trainer -> trainerStorage.put(trainer.getUsername(), trainer));
            storageData.getTrainees().forEach(trainee -> traineeStorage.put(trainee.getUsername(), trainee));
            storageData.getTrainings().forEach(training -> trainingStorage.put(training.getTrainingName(), training));
        } catch (IOException | URISyntaxException e) {
            log.error("Failed to load data from file: {}", e.getMessage());
        }
    }
}
