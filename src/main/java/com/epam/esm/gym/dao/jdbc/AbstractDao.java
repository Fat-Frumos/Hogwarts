package com.epam.esm.gym.dao.jdbc;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
public abstract class AbstractDao<T> {

    private final Class<T> clazz;
    protected final SessionFactory sessionFactory;
    protected final static String USERNAME = "username";

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return getSession()
                .createQuery("FROM " + clazz.getName(), clazz)
                .getResultList();
    }

    @Transactional
    public T save(T entity) {
        getSession().persist(entity);
        return entity;
    }

    @Transactional
    public T update(T entity) {
        return getSession().merge(entity);
    }

    @Transactional
    public void delete(T entity) {
        getSession().remove(entity);
    }
}
