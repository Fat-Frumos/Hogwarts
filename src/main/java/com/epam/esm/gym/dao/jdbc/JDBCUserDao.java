package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.User;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
    public boolean existsByUsername(String username) {
        try (Session session = factory.openSession()) {
            String hql = "SELECT count(*) FROM User WHERE username = :username";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("username", username);
            return query.getSingleResult() > 0;
        }
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
