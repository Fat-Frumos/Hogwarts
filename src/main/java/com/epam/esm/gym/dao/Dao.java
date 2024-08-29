package com.epam.esm.gym.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    List<T> findAll();

    Optional<T> findByUsername(String username);

    T save(T t);

    T update(T t);

    void delete(T t);
}
