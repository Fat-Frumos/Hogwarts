package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory Data Access Object (DAO) for managing {@link Trainee} entities.
 *
 * <p>This DAO implementation uses in-memory storage to perform CRUD operations on {@link Trainee} entities.
 * It is activated when the application property <code>strategy.dao.type</code> is set to <code>in-memory</code>.
 * The class relies on a {@link StorageInitializer} to provide the necessary storage maps for trainees and trainers.</p>
 *
 * <p>In-memory storage facilitates rapid access and modification of data during application testing or
 * development phases where persistence to a database is not required. The methods in this class handle operations
 * such as saving, updating, deleting, and retrieving trainees.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @see AbstractInMemoryDao
 * @see Trainee
 * @see Trainer
 * @since 1.0
 */
@Slf4j
@Repository
@ConditionalOnProperty(name = "strategy.dao.type", havingValue = "in-memory")
public class InMemoryTraineeDao extends AbstractInMemoryDao<Trainee> implements TraineeDao {

    private final Map<String, Trainer> trainerStorage;

    /**
     * Constructs an {@link InMemoryTraineeDao} with the specified {@link StorageInitializer}.
     *
     * <p>This constructor initializes the DAO with in-memory storage for {@link Trainee} entities and
     * sets up the storage for {@link Trainer} entities provided by the {@link StorageInitializer}.
     * This setup allows for in-memory operations and manipulation of both trainees and trainers.</p>
     *
     * @param storageInitializer the {@link StorageInitializer} used for initializing storage.
     */
    public InMemoryTraineeDao(StorageInitializer storageInitializer) {
        super(storageInitializer.getTraineeStorage());
        this.trainerStorage = storageInitializer.getTrainerStorage();
    }

    /**
     * Retrieves all {@link Trainee} entities from in-memory storage.
     *
     * <p>This method accesses the in-memory storage to fetch a list of all {@link Trainee} entities.
     * It uses Java Streams to process the values from the storage map and collect them into a list.
     * This operation does not apply any filtering, thus returning the trainees in the order they were stored.</p>
     *
     * @return a list containing all {@link Trainee} entities stored in memory.
     */
    @Override
    public List<Trainee> findAll() {
        return storage.values().stream().toList();
    }

    /**
     * Finds a {@link Trainee} entity by their username.
     *
     * <p>This method searches the in-memory storage for a {@link Trainee} whose username matches the provided
     * username. It filters the stored trainees based on their associated username and returns an {@link Optional}
     * containing the matching {@link Trainee} if found. If no match is found, the {@link Optional} will be empty.</p>
     *
     * @param username the username of the {@link Trainee} to find.
     * @return an {@link Optional} containing the {@link Trainee} if found, or an empty {@link Optional} if not found.
     */
    @Override
    public Optional<Trainee> findByUsername(String username) {
        return storage.values().stream()
                .filter(trainee -> trainee.getUser().getUsername().equals(username))
                .findFirst();
    }

    /**
     * Saves a {@link Trainee} entity to the in-memory storage.
     *
     * <p>This method adds the provided {@link Trainee} entity to the in-memory storage.
     * The trainee is stored in the map using their username as the key.
     * After the operation, the current state of the storage is logged for tracking purposes.
     * This operation overwrites any existing trainee with the same username.</p>
     *
     * @param trainee the {@link Trainee} entity to save.
     * @return the saved {@link Trainee} entity.
     */
    @Override
    public Trainee save(Trainee trainee) {
        storage.put(trainee.getUser().getUsername(), trainee);
        log.info(storage.toString());
        return trainee;
    }

    /**
     * Updates an existing {@link Trainee} entity in the in-memory storage.
     *
     * <p>This method updates the information of an existing {@link Trainee} in the in-memory storage. It first checks
     * whether the trainee with the specified username exists in the storage.
     * If found, it updates the trainee's information;
     * otherwise, it returns <code>null</code>.
     * This operation is idempotent and will overwrite the existing record.</p>
     *
     * @param trainee the {@link Trainee} entity with updated information.
     * @return the updated {@link Trainee} entity if the update was successful,
     * or <code>null</code> if the trainee was not found.
     */
    @Override
    public Trainee update(Trainee trainee) {
        if (storage.containsKey(trainee.getUser().getUsername())) {
            storage.put(trainee.getUser().getUsername(), trainee);
            return trainee;
        }
        return null;
    }

    /**
     * Deletes a {@link Trainee} entity from the in-memory storage.
     *
     * <p>This method removes the specified {@link Trainee} from the in-memory storage using the trainee's username
     * as the key. If the trainee exists in the storage, they are removed; otherwise, no action is taken.
     * This method is used to clean up or remove outdated or unwanted trainee data.</p>
     *
     * @param trainee the {@link Trainee} entity to delete.
     */
    @Override
    public void delete(Trainee trainee) {
        storage.remove(trainee.getUser().getUsername());
    }

    /**
     * Finds trainers not assigned to a specific trainee.
     *
     * <p>This method retrieves a list of {@link Trainer} entities from the in-memory storage that are not currently
     * assigned to the trainee specified by the username. It filters out trainers that are already associated with the
     * given trainee, returning only those trainers who are not linked to the trainee.</p>
     *
     * @param username the username of the trainee whose assignments are to be considered.
     * @return a list of {@link Trainer} entities not currently assigned to the specified trainee.
     */
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
