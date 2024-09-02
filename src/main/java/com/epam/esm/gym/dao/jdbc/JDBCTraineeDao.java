package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class JDBCTraineeDao extends AbstractDao<Trainee> implements TraineeDao {

    public JDBCTraineeDao(SessionFactory sessionFactory) {
        super(Trainee.class, sessionFactory);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        String hql = """
                SELECT t
                FROM Trainee t
                JOIN FETCH t.user u
                WHERE u.username = :username
                """;
        return getSession().createQuery(hql, Trainee.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public List<Trainer> findNotAssignedTrainers(String username) {
        String hql = """
                    SELECT t
                    FROM Trainer t
                    LEFT JOIN Training tr ON tr.trainer.id = t.id
                    JOIN t.user u
                    WHERE tr.id IS NULL
                      AND u.username != :username
                """;

        return getSession().createQuery(hql, Trainer.class)
                .setParameter("username", username)
                .getResultList();
    }
}
