package com.epam.esm.gym.dao.storage;

import java.util.Map;

public abstract class AbstractInMemoryDao<T> {

    protected final Map<String, T> storage;

    protected AbstractInMemoryDao(Map<String, T> storage) {
        this.storage = storage;
    }
    public abstract T save(T entity);
    public abstract T update(T entity);
    public abstract void delete(T entity);
}
