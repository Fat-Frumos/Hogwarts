package com.epam.esm.gym.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing a training session.
 *
 * <p>This class defines the structure of a training session, including the trainer, training, start and end times.
 * It is used to manage the scheduling and assignment of training sessions.</p>
 */
@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sessions")
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id")
    private Training training;

    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * Calculates the duration between the start and end times.
     *
     * <p>This method computes the duration of an event by subtracting the
     * {@code startTime} from the {@code endTime}. It is useful for determining
     * the length of an activity or session.</p>
     *
     * @return a {@link Duration} representing the time difference between
     * {@code startTime} and {@code endTime}
     */
    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrainingSession that = (TrainingSession) obj;
        return Objects.equals(id, that.id)
                && Objects.equals(trainer, that.trainer)
                && Objects.equals(startTime, that.startTime)
                && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainer, startTime, endTime);
    }
}
