package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.domain.Training;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JDBCTrainingDao implements TrainingDao {

    private final SessionFactory factory;

    @Override
    public List<Training> findAll() {
        return null;
    }

    @Override
    public Optional<Training> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Training> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Training save(Training training) {
        return null;
    }

    @Override
    public void update(Training entity) {

    }

    @Override
    public void delete(Training training) {

    }
}
