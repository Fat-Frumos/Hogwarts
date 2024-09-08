package com.epam.esm.gym.dao.jpa;

import com.epam.esm.gym.domain.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing {@link TrainingSession} entities.
 * Provides methods to perform CRUD operations and custom queries on training sessions.
 */
@Repository
public interface JpaTrainingSessionRepository extends JpaRepository<TrainingSession, Long> {

    /**
     * Fetches all training sessions occurring within the specified start and end times.
     *
     * @param startTime the start time of the sessions
     * @param endTime   the end time of the sessions
     * @return a list of training sessions within the specified time range
     */
    List<TrainingSession> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
