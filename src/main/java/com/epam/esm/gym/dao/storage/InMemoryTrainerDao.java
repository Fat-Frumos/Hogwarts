package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * In-memory Data Access Object (DAO) for managing {@link Trainer} entities.
 *
 * <p>This DAO implementation uses in-memory storage to perform CRUD operations on {@link Trainer} entities.
 * It is activated when the application property <code>strategy.dao.type</code> is set to <code>in-memory</code>.
 * The class relies on a {@link StorageInitializer} to provide the storage maps for trainers and trainees.</p>
 */
@Repository
@ConditionalOnProperty(name = "strategy.dao.type", havingValue = "in-memory")
public class InMemoryTrainerDao extends AbstractInMemoryDao<Trainer> implements TrainerDao {

    private final Map<String, Trainee> traineeStorage;

    /**
     * Constructs an instance of {@link InMemoryTrainerDao} with the provided {@link StorageInitializer}.
     *
     * <p>This constructor initializes the DAO with in-memory storage maps for trainers and trainees provided by
     * the {@link StorageInitializer}. It sets up the storage for trainers and initializes the trainee storage.</p>
     *
     * @param storageInitializer the {@link StorageInitializer} used to initialize storage maps for users.
     */
    public InMemoryTrainerDao(StorageInitializer storageInitializer) {
        super(storageInitializer.getTrainerStorage());
        this.traineeStorage = storageInitializer.getTraineeStorage();
    }

    /**
     * Retrieves all {@link Trainer} entities from in-memory storage.
     *
     * <p>This method accesses the in-memory storage to fetch a list of all {@link Trainer} entities.
     * It uses Java Streams to process the values from the storage map and collect them into a list.
     * This operation does not apply any filtering or sorting,
     * thus returning the trainers in the order they were stored.</p>
     *
     * @return a list containing all {@link Trainer} entities stored in memory.
     */
    @Override
    public List<Trainer> findAll() {
        return storage.values().stream().toList();
    }

    /**
     * Finds a {@link Trainer} entity by their username.
     *
     * <p>This method searches the in-memory storage for a {@link Trainer} whose username matches the provided
     * username. It retrieves the trainer directly from the storage map using the username as the key,
     * returning an {@link Optional} containing the trainer if found.
     * If no match is found, the {@link Optional} will be empty.</p>
     *
     * @param username the username of the {@link Trainer} to find.
     * @return an {@link Optional} containing the {@link Trainer} if found, or an empty {@link Optional} if not found.
     */
    @Override
    public Optional<Trainer> findByName(String username) {
        return Optional.ofNullable(storage.get(username));
    }

    /**
     * Saves a {@link Trainer} entity to the in-memory storage.
     *
     * <p>This method adds the provided {@link Trainer} entity to the in-memory storage. The trainer is stored in the
     * map using their username as the key. This operation overwrites any existing trainer with the same username.</p>
     *
     * @param trainer the {@link Trainer} entity to save.
     * @return the saved {@link Trainer} entity.
     */
    @Override
    public Trainer save(Trainer trainer) {
        storage.put(trainer.getUser().getUsername(), trainer);
        return trainer;
    }

    /**
     * Updates an existing {@link Trainer} entity in the in-memory storage.
     *
     * <p>This method updates the information of an existing {@link Trainer} in the in-memory storage. It first checks
     * whether the trainer with the specified username exists in the storage.
     * If found, it updates the trainer's information; otherwise, it returns <code>null</code>.
     * This operation is idempotent and will overwrite the existing record.</p>
     *
     * @param trainer the {@link Trainer} entity with updated information.
     * @return the updated {@link Trainer} entity if the update was successful,
     * or <code>null</code> if the trainer was not found.
     */
    @Override
    public Trainer update(Trainer trainer) {
        if (storage.containsKey(trainer.getUser().getUsername())) {
            storage.put(trainer.getUser().getUsername(), trainer);
            return trainer;
        }
        return null;
    }

    /**
     * Deletes a {@link Trainer} entity from the in-memory storage.
     *
     * <p>This method removes the specified {@link Trainer} from the in-memory storage using the trainer's username
     * as the key. If the trainer exists in the storage, they are removed; otherwise, no action is taken. This method
     * is used to clean up or remove outdated or unwanted trainer data.</p>
     *
     * @param trainer the {@link Trainer} entity to delete.
     */
    @Override
    public void delete(Trainer trainer) {
        storage.remove(trainer.getUser().getUsername());
    }

    /**
     * Activates or deactivates a {@link Trainer} entity based on the provided username.
     *
     * <p>This method updates the active status of the {@link Trainer} identified by the given username.
     * If the trainer is found in the storage, their active status is set according to the provided value.
     * If the trainer is not found, a {@link NoSuchElementException} is thrown.</p>
     *
     * @param username the username of the {@link Trainer} whose active status is to be updated.
     * @param active   the new active status to set.
     * @throws NoSuchElementException if no trainer with the specified username is found.
     */
    @Override
    public void activateTrainer(String username, Boolean active) {
        Trainer trainer = storage.get(username);
        if (trainer == null) {
            throw new NoSuchElementException("No trainer found with username: " + username);
        }
        trainer.getUser().setActive(active);
        storage.put(username, trainer);
    }

    /**
     * Finds trainers who are not assigned to any trainee.
     *
     * <p>This method currently returns an empty list as the assignment logic is not implemented.
     * In a complete implementation, it would return trainers who are not associated with any trainees.
     * This method provides a placeholder for future functionality.</p>
     *
     * @param username the username of the trainee to exclude from assignment.
     * @return an empty list of {@link Trainer} entities.
     */
    @Override
    public List<Trainer> findNotAssigned(String username) {
        return new ArrayList<>();
    }

    /**
     * Assigns a {@link Trainee} to a {@link Trainer}.
     *
     * <p>This method associates a {@link Trainee} with a {@link Trainer} based on their usernames.
     * It first retrieves both entities from their respective storages. If both the trainer and trainee are found,
     * the trainee is added to the trainer's list of trainees and vice versa.
     * The updated trainer and trainee are then stored back in their respective maps.
     * If either the trainer or trainee is not found, a {@link NoSuchElementException} is thrown.</p>
     *
     * @param trainerUsername the username of the {@link Trainer} to whom the trainee will be assigned.
     * @param traineeUsername the username of the {@link Trainee} to be assigned.
     * @throws NoSuchElementException if no trainer or trainee with the specified usernames is found.
     */
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
