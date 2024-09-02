package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingSession;
import com.epam.esm.gym.domain.TrainingType;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class JDBCTrainingDao extends AbstractDao<Training> implements TrainingDao {
    public JDBCTrainingDao(SessionFactory sessionFactory) {
        super(Training.class, sessionFactory);
    }

    @Override
    public Optional<Training> findByUsername(String username) {
        String hql = """
                SELECT tr
                FROM Training tr
                LEFT JOIN FETCH tr.trainer trainer
                LEFT JOIN FETCH tr.trainee trainee
                LEFT JOIN FETCH trainer.user trainerUser
                LEFT JOIN FETCH trainee.user traineeUser
                WHERE trainerUser.username = :username
                   OR traineeUser.username = :username
                """;
        return getSession().createQuery(hql, Training.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public List<TrainingType> findAllTrainingTypes() {
        return getSession().createQuery("FROM TrainingType", TrainingType.class)
                .getResultList();
    }

    @Override
    public List<Training> findTrainingsByTrainerUsername(String username) {
        String hql = """
                    SELECT t
                    FROM Training t
                    JOIN t.trainer tr
                    JOIN tr.user u
                    WHERE u.username = :username
                """;
        return getSession().createQuery(hql, Training.class)
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public List<TrainingSession> findByStartTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        String hql = "SELECT t FROM TrainingSession t WHERE t.startTime BETWEEN :startDateTime AND :endDateTime";
        return getSession().createQuery(hql, TrainingSession.class)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .getResultList();
    }
}
