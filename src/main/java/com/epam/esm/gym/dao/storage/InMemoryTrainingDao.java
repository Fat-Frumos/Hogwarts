package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingSession;
import com.epam.esm.gym.domain.TrainingType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class InMemoryTrainingDao extends AbstractInMemoryDao<Training> implements TrainingDao {

    private final Map<String, Trainer> trainerStorage;
    private final Map<String, Training> trainingStorage;

    public InMemoryTrainingDao(StorageInitializer storageInitializer) {
        super(storageInitializer.getTrainingStorage());
        this.trainerStorage = storageInitializer.getTrainerStorage();
        this.trainingStorage = storageInitializer.getTrainingStorage();
    }

    @Override
    public List<Training> findAll() {
        return storage.values().stream().toList();
    }

    @Override
    public Optional<Training> findByUsername(String username) {
        return Optional.ofNullable(storage.get(username));
    }

    @Override
    public List<TrainingType> findAllTrainingTypes() {
        return trainingStorage.values().stream()
                .map(Training::getType)
                .distinct().toList();
    }

    @Override
    public List<Training> findTrainingsByTrainerUsername(String trainerUsername) {
        Trainer trainer = trainerStorage.get(trainerUsername);
        if (trainer == null) {
            throw new NoSuchElementException("No trainer found with username: " + trainerUsername);
        }
        return storage.values().stream()
                .filter(training -> training.getTrainer().equals(trainer))
                .toList();
    }

    @Override
    public List<TrainingSession> findByStartTimeBetween(LocalDateTime startOfWeekDateTime, LocalDateTime endOfWeekDateTime) {
        return storage.values().stream()
                .flatMap(training -> training.getTrainingSessions().stream())
                .filter(session -> !session.getStartTime().isBefore(startOfWeekDateTime)
                        && !session.getStartTime().isAfter(endOfWeekDateTime))
                .toList();
    }

    @Override
    public Training save(Training entity) {
        storage.put(entity.getTrainingName(), entity);
        return entity;
    }

    @Override
    public Training update(Training entity) {
        if (storage.containsKey(entity.getTrainingName())) {
            storage.put(entity.getTrainingName(), entity);
            return entity;
        } else {
            throw new NoSuchElementException("No training found with name: " + entity.getTrainingName());
        }
    }

    @Override
    public void delete(Training entity) {
        if (storage.containsKey(entity.getTrainingName())) {
            storage.remove(entity.getTrainingName());
        } else {
            throw new NoSuchElementException("No training found with name: " + entity.getTrainingName());
        }
    }
}