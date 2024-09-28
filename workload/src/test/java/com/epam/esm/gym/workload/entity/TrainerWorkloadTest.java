package com.epam.esm.gym.workload.entity;

import com.epam.esm.gym.workload.provider.WorkloadEntityArgumentsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadTest {
   private int year;
   private String monthName;
   private long durationToAdd;
   private long expectedDuration;
   private long durationToRemove;

    @BeforeEach
    void setUp() {
        year = 2024;
        monthName = "FEBRUARY";
        durationToAdd = 20;
        expectedDuration = 20;
        durationToRemove = 20;
    }

    @ParameterizedTest
    @ArgumentsSource(WorkloadEntityArgumentsProvider.class)
    void testAddHours(TrainerWorkload trainerWorkload) {
        trainerWorkload.addHours(durationToAdd, year, monthName);
        YearSummary yearSummary = trainerWorkload.getYearSummaries().stream()
                .filter(ys -> ys.getYear() == year)
                .findFirst()
                .orElseThrow();

        MonthSummary monthSummary = yearSummary.getMonths().stream()
                .filter(ms -> ms.getMonth().toString().equals(monthName))
                .findFirst()
                .orElseThrow();

        assertEquals(expectedDuration, monthSummary.getTotalDuration());
    }

    @ParameterizedTest
    @ArgumentsSource(WorkloadEntityArgumentsProvider.class)
    void testRemoveHours(TrainerWorkload trainerWorkload) {
        trainerWorkload.addHours(expectedDuration, year, monthName);
        trainerWorkload.removeHours(durationToRemove, year, monthName);
        YearSummary yearSummary = trainerWorkload.getYearSummaries().stream()
                .filter(ys -> ys.getYear() == year)
                .findFirst()
                .orElseThrow();

        MonthSummary monthSummary = yearSummary.getMonths().stream()
                .filter(ms -> ms.getMonth().toString().equals(monthName))
                .findFirst()
                .orElseThrow();

        long expectedAfterRemoval = Math.max(0, monthSummary.getTotalDuration() - durationToRemove);
        assertEquals(expectedAfterRemoval, monthSummary.getTotalDuration());
    }

    @ParameterizedTest
    @ArgumentsSource(WorkloadEntityArgumentsProvider.class)
    void getId(TrainerWorkload trainerWorkload) {
        assertNull(trainerWorkload.getId());
    }


    @ParameterizedTest
    @ArgumentsSource(WorkloadEntityArgumentsProvider.class)
    void isActive(TrainerWorkload trainerWorkload) {
        assertTrue(trainerWorkload.isActive());
    }

    @ParameterizedTest
    @ArgumentsSource(WorkloadEntityArgumentsProvider.class)
    void getTrainingDate(TrainerWorkload trainerWorkload) {
        trainerWorkload.getTrainings().forEach(training -> assertEquals(
                LocalDate.of(2024, 2, 2), training.getTrainingDate()));
    }

    @ParameterizedTest
    @ArgumentsSource(WorkloadEntityArgumentsProvider.class)
    void getYearSummaries(TrainerWorkload trainerWorkload) {
        assertTrue(trainerWorkload.getYearSummaries().isEmpty());
    }
}
