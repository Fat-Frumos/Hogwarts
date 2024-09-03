package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingSession;
import com.epam.esm.gym.domain.TrainingType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * In-memory Data Access Object (DAO) for managing {@link Training} entities.
 *
 * <p>This DAO implementation uses in-memory storage to perform CRUD operations on {@link Training} entities.
 * It is activated when the application property <code>strategy.dao.type</code> is set to <code>in-memory</code>.
 * The class relies on a {@link StorageInitializer} to provide the necessary storage maps for users.</p>
 */
@Repository
@ConditionalOnProperty(name = "strategy.dao.type", havingValue = "in-memory")
public class InMemoryTrainingDao extends AbstractInMemoryDao<Training> implements TrainingDao {

    private final Map<String, Trainer> trainerStorage;
    private final Map<String, Training> trainingStorage;

    /**
     * Constructs an instance of {@link InMemoryTrainingDao} with the provided {@link StorageInitializer}.
     *
     * <p>This constructor initializes the DAO with in-memory storage maps for trainings and trainers provided by
     * the {@link StorageInitializer}. It sets up the storage for trainings and also initializes the storage.</p>
     *
     * @param storageInitializer the {@link StorageInitializer} used to initialize storage maps for users.
     */
    public InMemoryTrainingDao(StorageInitializer storageInitializer) {
        super(storageInitializer.getTrainingStorage());
        this.trainerStorage = storageInitializer.getTrainerStorage();
        this.trainingStorage = storageInitializer.getTrainingStorage();
    }

    /**
     * Retrieves all {@link Training} entities from in-memory storage.
     *
     * <p>This method accesses the in-memory storage to fetch a list of all {@link Training} entities.
     * It uses Java Streams to process the values from the storage map and collect them into a list.
     * This operation does not apply any filtering or sorting,
     * thus returning the trainings in the order they were stored.</p>
     *
     * @return a list containing all {@link Training} entities stored in memory.
     */
    @Override
    public List<Training> findAll() {
        return storage.values().stream().toList();
    }

    /**
     * Finds a {@link Training} entity by the trainer's username.
     *
     * <p>This method searches the storage for a {@link Training} whose trainer's username matches the provided
     * username. It retrieves the training directly from the storage map using the username as the key,
     * returning an {@link Optional} containing the training if found.
     * If no match is found, the {@link Optional} will be empty.</p>
     *
     * @param username the username of the trainer associated with the {@link Training} to find.
     * @return an {@link Optional} containing the {@link Training} if found, or an empty {@link Optional} if not found.
     */
    @Override
    public Optional<Training> findByUsername(String username) {
        return Optional.ofNullable(storage.get(username));
    }

    /**
     * Retrieves all distinct {@link TrainingType} values from the in-memory storage.
     *
     * <p>This method extracts the {@link TrainingType} from all {@link Training} entities stored in the in-memory
     * storage. It uses Java Streams to map the types, remove duplicates, and collect them into a list.</p>
     *
     * @return a list of distinct {@link TrainingType} values from all stored trainings.
     */
    @Override
    public List<TrainingType> findAllTrainingTypes() {
        return trainingStorage.values().stream()
                .map(Training::getType)
                .distinct().toList();
    }

    /**
     * Finds all {@link Training} entities associated with a specific trainer.
     *
     * <p>This method searches the in-memory storage for all {@link Training} entities where the associated trainer's
     * username matches the provided name. If the trainer is not found, a {@link NoSuchElementException} is thrown.</p>
     *
     * @param trainerUsername the username of the trainer whose trainings are to be retrieved.
     * @return a list of {@link Training} entities associated with the specified trainer.
     * @throws NoSuchElementException if no trainer with the specified username is found.
     */
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

    /**
     * Finds all {@link TrainingSession} entities within a specified time range.
     *
     * <p>This method retrieves all {@link TrainingSession} entities from the in-memory storage and filters them to
     * include only those whose start time falls within the specified range.
     * It uses Java Streams to process the training sessions and collect those that meet the criteria.</p>
     *
     * @param startOfWeekDateTime the start time of the range to filter sessions.
     * @param endOfWeekDateTime   the end time of the range to filter sessions.
     * @return a list of {@link TrainingSession} entities whose start time falls within the specified range.
     */
    @Override
    public List<TrainingSession> findByStartTimeBetween(
            LocalDateTime startOfWeekDateTime, LocalDateTime endOfWeekDateTime) {
        return storage.values().stream()
                .flatMap(training -> training.getTrainingSessions().stream())
                .filter(session -> !session.getStartTime().isBefore(startOfWeekDateTime)
                        && !session.getStartTime().isAfter(endOfWeekDateTime))
                .toList();
    }

    /**
     * Saves a {@link Training} entity to the in-memory storage.
     *
     * <p>This method adds the provided {@link Training} entity to the in-memory storage.
     * The training is stored in the map using its name as the key.
     * This operation overwrites any existing training with the same name.</p>
     *
     * @param entity the {@link Training} entity to save.
     * @return the saved {@link Training} entity.
     */
    @Override
    public Training save(Training entity) {
        storage.put(entity.getTrainingName(), entity);
        return entity;
    }

    /**
     * Updates an existing {@link Training} entity in the in-memory storage.
     *
     * <p>This method updates the information of an existing {@link Training} in the in-memory storage.
     * It first checks whether the training with the specified name exists in the storage.
     * If found, it updates the training's information;
     * otherwise, it throws a {@link NoSuchElementException}.</p>
     *
     * @param entity the {@link Training} entity with updated information.
     * @return the updated {@link Training} entity if the update was successful.
     * @throws NoSuchElementException if no training with the specified name is found.
     */
    @Override
    public Training update(Training entity) {
        if (storage.containsKey(entity.getTrainingName())) {
            storage.put(entity.getTrainingName(), entity);
            return entity;
        } else {
            throw new NoSuchElementException("No training found with name: " + entity.getTrainingName());
        }
    }

    /**
     * Deletes a {@link Training} entity from the in-memory storage.
     *
     * <p>This method removes the specified {@link Training} from the in-memory storage.
     * If the training exists in the storage, it is removed; otherwise, a {@link NoSuchElementException} is thrown.</p>
     *
     * @param entity the {@link Training} entity to delete.
     * @throws NoSuchElementException if no training with the specified name is found.
     */
    @Override
    public void delete(Training entity) {
        if (storage.containsKey(entity.getTrainingName())) {
            storage.remove(entity.getTrainingName());
        } else {
            throw new NoSuchElementException("No training found with name: " + entity.getTrainingName());
        }
    }
}