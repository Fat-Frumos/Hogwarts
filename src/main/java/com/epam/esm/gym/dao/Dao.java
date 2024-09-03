package com.epam.esm.gym.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic Data Access Object (DAO) interface for managing entities of type {@code T}.
 *
 * <p>This interface provides methods for basic CRUD operations, including finding all entities,
 * finding entities by username, saving, updating, and deleting entities. Implementations
 * of this interface are responsible for interacting with the underlying data store.</p>
 *
 * @param <T> the type of the entity that the DAO manages.
 * @author Pavlo Poliak
 * @since 1.0
 */
public interface Dao<T> {

    /**
     * Retrieves all entities of type {@code T}.
     * <p>This method returns a list of all entities stored in the data source.</p>
     *
     * @return a {@link List} containing all entities of type {@code T}.
     */
    List<T> findAll();

    /**
     * Retrieves an entity by its username.
     *
     * <p>This method searches for an entity with the specified username. If no entity
     * with the given username is found, an empty {@link Optional} is returned.</p>
     *
     * @param username the username of the entity to find.
     * @return an {@link Optional} containing the found entity, or an empty {@link Optional}
     * if no entity is found with the given username.
     */
    Optional<T> findByUsername(String username);

    /**
     * Saves a new entity to the data source.
     *
     * <p>This method persists the given entity. If the entity already exists, the behavior
     * is dependent on the implementation (e.g., it might update the existing entity).</p>
     *
     * @param entity the entity to save.
     * @return the saved entity.
     */
    T save(T entity);

    /**
     * Updates an existing entity in the data source.
     *
     * <p>This method updates the details of the given entity. The entity must already exist
     * in the data source; otherwise, the behavior is dependent on the implementation.</p>
     *
     * @param entity the entity to update.
     * @return the updated entity.
     */
    T update(T entity);

    /**
     * Deletes an entity from the data source.
     *
     * <p>This method removes the specified entity from the data source. If the entity does
     * not exist, the behavior is dependent on the implementation.</p>
     *
     * @param entity the entity to delete.
     */
    void delete(T entity);
}
