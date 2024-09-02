package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class InMemoryTrainerDao extends AbstractInMemoryDao<Trainer> implements TrainerDao {

    private final Map<String, Trainee> traineeStorage;

    public InMemoryTrainerDao(StorageInitializer storageInitializer) {
        super(storageInitializer.getTrainerStorage());
        this.traineeStorage = storageInitializer.getTraineeStorage();
    }

    @Override
    public List<Trainer> findAll() {
        return storage.values().stream().toList();
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return Optional.ofNullable(storage.get(username));
    }

    @Override
    public Trainer save(Trainer trainer) {
        storage.put(trainer.getUser().getUsername(), trainer);
        return trainer;
    }

    @Override
    public Trainer update(Trainer trainer) {
        if (storage.containsKey(trainer.getUser().getUsername())) {
            storage.put(trainer.getUser().getUsername(), trainer);
            return trainer;
        }
        return null;
    }

    @Override
    public void delete(Trainer trainer) {
        storage.remove(trainer.getUser().getUsername());
    }

    @Override
    public void activateTrainer(String username, Boolean active) {
        Trainer trainer = storage.get(username);
        if (trainer == null) {
            throw new NoSuchElementException("No trainer found with username: " + username);
        }
        trainer.getUser().setActive(active);
        storage.put(username, trainer);
    }

    @Override
    public List<Trainer> findNotAssigned(String username) {
        return new ArrayList<>();
    }

    @Override
    public void assignTraineeToTrainer(String trainerUsername, String traineeUsername) {
        Trainer trainer = storage.get(trainerUsername);
        if (trainer == null) {
            throw new NoSuchElementException("No trainer found with username: " + trainerUsername);
        }
        Trainee trainee = traineeStorage.get(traineeUsername);
        if (trainee == null) {
            throw new NoSuchElementException("No trainee found with username: " + traineeUsername);
        }
        trainer.getTrainees().add(trainee);
        trainee.getTrainers().add(trainer);
        storage.put(trainerUsername, trainer);
        traineeStorage.put(traineeUsername, trainee);
    }
}
