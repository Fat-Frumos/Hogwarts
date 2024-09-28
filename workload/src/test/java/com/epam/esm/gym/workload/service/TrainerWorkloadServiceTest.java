package com.epam.esm.gym.workload.service;

import com.epam.esm.gym.jms.dto.TrainerProfile;
import com.epam.esm.gym.jms.dto.TrainerWorkloadResponse;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.workload.dao.InMemoryDao;
import com.epam.esm.gym.workload.provider.TrainingResponseArgumentsProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the TrainerWorkload Service class.
 */
@ExtendWith(MockitoExtension.class)
public class TrainerWorkloadServiceTest {

    @Mock
    private InMemoryDao dao;

    @InjectMocks
    private TrainerWorkloadService workloadService;

    @ParameterizedTest
    @ArgumentsSource(TrainingResponseArgumentsProvider.class)
    void shouldReturnWorkloadResponse(
            List<TrainingResponse> trainingResponses,
            TrainerProfile profile, String username) {
        when(dao.findTrainerByUsername(username)).thenReturn(profile);

        TrainerWorkloadResponse response = workloadService.getTrainerWorkloadByName(username,
                LocalDate.now().minusMonths(1), LocalDate.now());

        assertNotNull(response);
        assertEquals(profile.username(), response.username());
        assertEquals(profile.firstName(), response.firstName());
        assertEquals(profile.lastName(), response.lastName());
        Map<Integer, Map<Month, Long>> workloadMap = workloadService.getWorkloadMap(trainingResponses);
        assertFalse(workloadMap.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenTrainerNotFound() {
        String username = "invalid_user";
        when(dao.findTrainerByUsername(username)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> workloadService.getTrainerWorkloadByName(
                username, LocalDate.now().minusMonths(1), LocalDate.now()));
    }
}
