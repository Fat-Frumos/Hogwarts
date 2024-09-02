package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Getter
@Component
public class StorageInitializer {

    private final String filePath;
    private final Map<String, Trainer> trainerStorage;
    private final Map<String, Trainee> traineeStorage;
    private final Map<String, Training> trainingStorage;
    private final Map<String, User> userStorage;

    public StorageInitializer(
            @Value("${storage.file.path}") String filePath,
            Map<String, User> userStorage,
            Map<String, Trainer> trainerStorage,
            Map<String, Trainee> traineeStorage,
            Map<String, Training> trainingStorage) {
        this.filePath = filePath;
        this.userStorage = userStorage;
        this.trainerStorage = trainerStorage;
        this.traineeStorage = traineeStorage;
        this.trainingStorage = trainingStorage;
    }

    @PostConstruct
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(filePath);
            StorageData storageData = mapper.readValue(file, StorageData.class);
            storageData.getTrainers().forEach(trainer -> trainerStorage.put(trainer.getUser().getUsername(), trainer));
            storageData.getTrainees().forEach(trainee -> traineeStorage.put(trainee.getUser().getUsername(), trainee));
            storageData.getTrainings().forEach(training -> trainingStorage.put(training.getTrainingName(), training));
        } catch (IOException e) {
            log.error("Failed to load data from file: {}", e.getMessage());
        }
    }
}
