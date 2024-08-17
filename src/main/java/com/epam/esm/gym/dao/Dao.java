package com.epam.esm.gym.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T, ID> {
    List<T> findAll();

    Optional<T> findById(ID id);

    Optional<T> findByUsername(String username);

    T save(T t);

    void update(T entity);

    void delete(T t);
}
