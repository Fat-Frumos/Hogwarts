package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.domain.Trainer;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JDBCTrainerDao extends AbstractDao<Trainer> implements TrainerDao {

    public JDBCTrainerDao(SessionFactory sessionFactory) {
        super(Trainer.class, sessionFactory);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        String hql = """
                    SELECT t
                    FROM Trainer t
                    JOIN FETCH t.user u
                    WHERE u.username = :username
                """;
        return getSession().createQuery(hql, Trainer.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public void activateTrainer(String username, Boolean active) {
        String hql = """
                    UPDATE User u
                    SET u.active = :active
                    WHERE u.id IN (
                        SELECT t.user.id
                        FROM Trainer t
                        WHERE t.user.username = :username
                    )
                """;
        getSession().createMutationQuery(hql)
                .setParameter("active", active)
                .setParameter("username", username)
                .executeUpdate();
    }


    @Override
    public List<Trainer> findNotAssigned(String username) {
        String hql = """
                    SELECT t
                    FROM Trainer t
                    WHERE t.id NOT IN (
                        SELECT tr.trainer.id
                        FROM Training tr
                    )
                    AND t.user.username != :username
                """;
        return getSession().createQuery(hql, Trainer.class)
                .setParameter("username", username)
                .getResultList();
    }

    public void assignTraineeToTrainer(String trainerUsername, String traineeUsername) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();

            String hql = """
                    UPDATE Trainee t SET t.trainers = (
                    SELECT tr FROM Trainer tr WHERE tr.user.username = :trainerUsername)
                    WHERE t.user.username = :traineeUsername
                    """;
            int updatedEntities = session.createMutationQuery(hql)
                    .setParameter("trainerUsername", trainerUsername)
                    .setParameter("traineeUsername", traineeUsername)
                    .executeUpdate();

            session.getTransaction().commit();

            if (updatedEntities == 0) {
                throw new EntityNotFoundException("Failed to assign trainee. Trainee might not exist.");
            }
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw new RuntimeException(e.getMessage());
        }
    }
}
