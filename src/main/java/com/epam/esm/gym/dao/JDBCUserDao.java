package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.User;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCUserDao implements UserDao {

    private final SessionFactory factory;

    public JDBCUserDao(SessionFactory sessionFactory) {
        this.factory = sessionFactory;
    }

    @Override
    public User save(User user) {
        factory.getCurrentSession().persist(user);
        return user;

    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(
                factory.getCurrentSession()
                        .get(User.class, id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return factory.getCurrentSession()
                .createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public void delete(User entity) {
        factory.getCurrentSession().remove(entity);
    }

    @Override
    public List<User> findAll() {
        return factory
                .getCurrentSession()
                .createQuery("FROM User", User.class)
                .getResultList();
    }

    @Override
    public void update(User entity) {

    }
}
