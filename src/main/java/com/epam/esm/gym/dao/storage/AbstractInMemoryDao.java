package com.epam.esm.gym.dao.storage;

import java.util.Map;

/**
 * Abstract base class for in-memory data access objects (DAOs) managing entities of type {@link T}.
 *
 * <p>This abstract class provides a common base for DAOs that store entities in an in-memory map.
 * It defines a protected map field to store entities and abstract methods for common DAO operations
 * such as save, update, and delete. Concrete subclasses must provide implementations for these
 * operations, leveraging the in-memory map for data storage. This class is useful for testing or scenarios
 * where persistence is not required.</p>
 *
 * @param <T> the type of the entity managed by the DAO.
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public abstract class AbstractInMemoryDao<T> {

    /**
     * The in-memory storage map for entities.
     *
     * <p>This map holds the entities managed by the DAO, keyed by a unique identifier, typically a
     * string. Subclasses should use this map to implement the data access operations. The map is
     * initialized via the constructor and is protected to allow access by subclasses.</p>
     */
    protected final Map<String, T> storage;

    /**
     * Constructs an {@link AbstractInMemoryDao} with the specified storage map.
     *
     * <p>This constructor initializes the DAO with the given map for in-memory storage of entities.
     * It sets up the storage map that concrete subclasses will use for performing data operations.
     * The map should be initialized prior to using this constructor, and its contents will be managed
     * by the DAO's methods.</p>
     *
     * @param map the map to be used for in-memory storage of entities.
     */
    protected AbstractInMemoryDao(Map<String, T> map) {
        this.storage = map;
    }

    /**
     * Saves a new entity into the in-memory storage.
     *
     * <p>This method is responsible for adding a new entity to the storage map. The entity should not
     * already exist in the map, as this method is intended for insertion of new records. The concrete
     * implementation should handle the logic for generating a unique key for the new entity and storing
     * it in the map. The method returns the saved entity.</p>
     *
     * @param entity the entity to be saved.
     * @return the saved entity.
     */
    public abstract T save(T entity);

    /**
     * Updates an existing entity in the in-memory storage.
     *
     * <p>This method updates an existing entity in the storage map. The entity should already exist in
     * the map, and the method should replace the old entity with the updated version. The concrete
     * implementation should ensure that the entity's unique identifier is used to locate and update the
     * correct record. The method returns the updated entity.</p>
     *
     * @param entity the entity to be updated.
     * @return the updated entity.
     */
    public abstract T update(T entity);

    /**
     * Deletes an entity from the in-memory storage.
     *
     * <p>This method removes an existing entity from the storage map. The entity must be present in the
     * map for the deletion to be successful. The concrete implementation should handle the logic for
     * locating and removing the entity based on its unique identifier. This method does not return any
     * value and performs the deletion operation.</p>
     *
     * @param entity the entity to be deleted.
     */
    public abstract void delete(T entity);
}
