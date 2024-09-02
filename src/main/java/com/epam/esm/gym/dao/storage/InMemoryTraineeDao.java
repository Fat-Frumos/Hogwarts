package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryTraineeDao extends AbstractInMemoryDao<Trainee> implements TraineeDao {

    private final Map<String, Trainer> trainerStorage;

    public InMemoryTraineeDao(StorageInitializer storageInitializer) {
        super(storageInitializer.getTraineeStorage());
        this.trainerStorage = storageInitializer.getTrainerStorage();
    }

    @Override
    public List<Trainee> findAll() {
        return storage.values().stream().toList();
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        return storage.values().stream()
                .filter(trainee -> trainee.getUser().getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Trainee save(Trainee trainee) {
        storage.put(trainee.getUser().getUsername(), trainee);
        return trainee;
    }

    @Override
    public Trainee update(Trainee trainee) {
        if (storage.containsKey(trainee.getUser().getUsername())) {
            storage.put(trainee.getUser().getUsername(), trainee);
            return trainee;
        }
        return null;
    }

    @Override
    public void delete(Trainee trainee) {
        storage.remove(trainee.getUser().getUsername());
    }

    @Override
    public List<Trainer> findNotAssignedTrainers(String username) {
        Trainee trainee = storage.get(username);
        if (trainee == null) {
            return List.of();
        }
        return trainerStorage.values().stream()
                .filter(trainer -> !trainee.getTrainers().contains(trainer))
                .toList();
    }
}
