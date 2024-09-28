package com.epam.esm.gym.user.entity;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit test class for TrainingSessionTest functionalities.
 */
class TrainingSessionTest {

    @Test
    void testGetDuration() {
        Trainer trainer = Trainer.builder()
                .build();

        Training training = Training.builder()
                .trainingName("Defense Against the Dark Arts")
                .build();

        LocalDateTime startTime = LocalDateTime.of(2024, 9, 25, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 9, 25, 12, 0);

        TrainingSession session = TrainingSession.builder()
                .trainer(trainer)
                .training(training)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        Duration duration = session.getDuration();
        assertEquals(Duration.ofHours(2), duration);
    }

    @Test
    void testEqualsAndHashCode() {
        Trainer trainer1 = Trainer.builder()
                .build();

        Training training1 = Training.builder()
                .trainingName("Defense Against the Dark Arts")
                .build();

        TrainingSession session1 = TrainingSession.builder()
                .id(1L)
                .trainer(trainer1)
                .training(training1)
                .startTime(LocalDateTime.of(2024, 9, 25, 10, 0))
                .endTime(LocalDateTime.of(2024, 9, 25, 12, 0))
                .build();

        TrainingSession session2 = TrainingSession.builder()
                .id(1L)
                .trainer(trainer1)
                .training(training1)
                .startTime(LocalDateTime.of(2024, 9, 25, 10, 0))
                .endTime(LocalDateTime.of(2024, 9, 25, 12, 0))
                .build();

        assertEquals(session1, session2);
        assertEquals(session1.hashCode(), session2.hashCode());

        TrainingSession session3 = TrainingSession.builder()
                .id(2L)
                .trainer(trainer1)
                .training(training1)
                .startTime(LocalDateTime.of(2024, 9, 25, 10, 0))
                .endTime(LocalDateTime.of(2024, 9, 25, 13, 0))
                .build();

        assertNotEquals(session1, session3);
    }

    @Test
    void testBuilder() {
        Trainer trainer = Trainer.builder()
                .build();

        Training training = Training.builder()
                .build();

        LocalDateTime startTime = LocalDateTime.of(2024, 9, 25, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 9, 25, 11, 0);

        TrainingSession session = TrainingSession.builder()
                .trainer(trainer)
                .training(training)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        assertEquals(trainer, session.getTrainer());
        assertEquals(training, session.getTraining());
        assertEquals(startTime, session.getStartTime());
        assertEquals(endTime, session.getEndTime());
    }

    @Test
    void testEqualsDifferentClasses() {
        TrainingSession session = TrainingSession.builder().build();
        assertNotEquals(session, new Object());
    }

    @Test
    void testEqualsNull() {
        TrainingSession session = TrainingSession.builder().build();
        assertNotEquals(session, null);
    }
}
