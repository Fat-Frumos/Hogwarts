package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.domain.Trainee;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JDBCTraineeDao implements TraineeDao {

    private final SessionFactory factory;

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
        factory.close();
    }
}
