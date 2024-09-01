package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
public class JDBCUserDao extends AbstractDao<User> implements UserDao {

    public JDBCUserDao(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM \"users\" WHERE username = :username";
        Query<Long> query = getSession().createNativeQuery(sql, Long.class);
        query.setParameter("username", username);
        return query.getSingleResult() > 0;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Session session = getSession();
        String sql = "SELECT * FROM \"users\" WHERE username = :username";
        Query<User> query = session.createNativeQuery(sql, User.class);
        query.setParameter("username", username);
        return query.uniqueResultOptional();
    }

    @Override
    @Transactional
    public User update(User user) {
        try {
            String hql = "UPDATE User SET active = :active, firstName = :firstName, lastName = :lastName, password = :password, username = :username WHERE id = :id";
            MutationQuery query = getSession().createMutationQuery(hql);
            query.setParameter("active", user.getActive());
            query.setParameter("firstName", user.getFirstName());
            query.setParameter("lastName", user.getLastName());
            query.setParameter("password", user.getPassword());
            query.setParameter("username", user.getUsername());
            query.setParameter("id", user.getId());
            query.executeUpdate();
        } catch (Exception e) {
            log.error("Error updating user: " + e.getMessage());
        }
        return user;
    }
}
