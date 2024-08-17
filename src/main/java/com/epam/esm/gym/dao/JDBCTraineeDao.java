package com.epam.esm.gym.dao;

import com.epam.esm.gym.domain.Trainee;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCTraineeDao implements TraineeDao {

    private final SessionFactory factory;

    public JDBCTraineeDao(SessionFactory sessionFactory) {
        this.factory = sessionFactory;
    }

    @Override
    public List<Trainee> findAll() {
        return null;
    }

    @Override
    public Optional<Trainee> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Trainee save(Trainee trainee) {
        return null;
    }

    @Override
    public void update(Trainee entity) {

    }

    @Override
    public void delete(Trainee trainee) {

    }
}
