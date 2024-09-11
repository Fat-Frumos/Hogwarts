package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.dao.jpa.TrainingSessionRepository;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.TrainingSession;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.service.profile.TrainingSessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingSessionServiceTest {

    @Mock
    private TrainerDao trainerDao;
    @Mock
    private TrainingSessionRepository trainingRepository;

    @InjectMocks
    private TrainingSessionService trainingSessionService;

    @Mock
    private Logger log;

    @Test
    void testGenerateWeeklyReport() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfWeekDateTime = today.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeekDateTime = today.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

        Trainer trainer = Trainer.builder().user(User.builder().username("testUser").build()).build();
        TrainingSession session = TrainingSession.builder()
                .trainer(trainer)
                .startTime(LocalDateTime.of(2024, 9, 1, 10, 0))
                .endTime(LocalDateTime.of(2024, 9, 1, 12, 0))
                .build();

        when(trainingRepository.findByStartTimeBetween(startOfWeekDateTime, endOfWeekDateTime))
                .thenReturn(List.of(session));

        Map<String, Long> expectedReport = new HashMap<>();
        expectedReport.put("testUser", 120L);

        Map<String, Long> actualReport = trainingSessionService.generateWeeklyReport();
        assertEquals(expectedReport, actualReport);
    }
}