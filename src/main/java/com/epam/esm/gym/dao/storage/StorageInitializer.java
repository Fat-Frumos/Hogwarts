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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
@Getter
@Component
@AllArgsConstructor
public class StorageInitializer {

    private final Map<String, Trainer> trainerStorage;
    private final Map<String, Trainee> traineeStorage;
    private final Map<String, Training> trainingStorage;
    private final Map<String, User> userStorage;

    @PostConstruct
    public void init() {
        URL resourceUrl = getClass().getResource("classpath:json/data.json");
        if (resourceUrl == null) {
            log.error("Resource not found: {}", "classpath:json/data.json");
            return;
        }
        try {
            String content = new String(Files.readAllBytes(Paths.get(resourceUrl.toURI())));
            StorageData storageData = new ObjectMapper().readValue(content, StorageData.class);
            storageData.getTrainers().forEach(trainer -> trainerStorage.put(trainer.getUser().getUsername(), trainer));
            storageData.getTrainees().forEach(trainee -> traineeStorage.put(trainee.getUser().getUsername(), trainee));
            storageData.getTrainings().forEach(training -> trainingStorage.put(training.getTrainingName(), training));

        } catch (IOException | URISyntaxException e) {
            log.error("Failed to load data from file: {}", e.getMessage());
        }
    }
}
