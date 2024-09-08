package com.epam.esm.gym.dao.jdbc;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Abstract base class for Data Access Objects (DAOs) providing common CRUD operations.
 *
 * <p>This abstract class provides a generic implementation for basic CRUD operations
 * using Hibernate. It simplifies DAO implementation by handling common database operations
 * such as saving, updating, deleting, and retrieving entities. Subclasses must specify the
 * entity type they manage by providing the entity class type.</p>
 *
 * @param <T> the type of the entity that the DAO manages.
 * @author Pavlo Poliak
 * @version 1.0.1
 * @since 1.0
 */
@AllArgsConstructor
public abstract class AbstractDao<T> {

    /**
     * The Hibernate {@link SessionFactory} used to obtain Hibernate sessions.
     * This SessionFactory is essential for interacting with the database.
     */
    private final SessionFactory sessionFactory;
    protected static final String USERNAME = "username";

    /**
     * Retrieves all entities of type {@code T}.
     *
     * <p>This method queries the database for all instances of the specified entity type.
     * It uses a dynamic Hibernate query to retrieve the entire result set.</p>
     *
     * @return a {@link List} containing all entities of type {@code T}.
     */
    abstract List<T> findAll();

    /**
     * Retrieves the current Hibernate session.
     *
     * <p>This method provides access to the current Hibernate {@link Session} used for
     * interacting with the database. The session is obtained from the {@link SessionFactory}
     * and is bound to the current transaction.</p>
     *
     * @return the current Hibernate {@link Session}.
     */
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Saves a new entity to the database.
     *
     * <p>This method persists the given entity to the database. If the entity is new, it
     * will be inserted into the database. The method does not return the ID of the persisted
     * entity; instead, it returns the entity itself. The behavior of this method may depend
     * on the specific implementation of the DAO.</p>
     *
     * @param entity the entity to save. Must not be null.
     * @return the saved entity.
     * @throws IllegalArgumentException if the entity is null.
     */
    @Transactional
    public T save(T entity) {
        getSession().persist(entity);
        return entity;
    }

    /**
     * Updates an existing entity in the database.
     *
     * <p>This method updates the details of the given entity in the database. If the entity
     * does not exist in the database, it may be inserted, depending on the implementation
     * details. The method returns the updated entity, which may be a proxy object.</p>
     *
     * @param entity the entity to update. Must not be null.
     * @return the updated entity.
     * @throws IllegalArgumentException if the entity is null.
     */
    @Transactional
    public T update(T entity) {
        return getSession().merge(entity);
    }

    /**
     * Deletes an entity from the database.
     *
     * <p>This method removes the given entity from the database. If the entity does not exist,
     * it may result in an exception or no operation, depending on the specific implementation
     * details. The method does not return any value.</p>
     *
     * @param entity the entity to delete. Must not be null.
     * @throws IllegalArgumentException if the entity is null.
     */
    @Transactional
    public void delete(T entity) {
        getSession().remove(entity);
    }
}
