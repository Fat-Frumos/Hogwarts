package com.epam.esm.gym.workload.service;

import com.epam.esm.gym.jms.dto.TrainerProfile;
import com.epam.esm.gym.jms.dto.TrainerStatus;
import com.epam.esm.gym.jms.dto.TrainerWorkloadResponse;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.workload.dao.InMemoryDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing trainer workloads.
 * This service handles operations related to trainers' workloads,
 * including updating workloads and generating reports. It interacts
 * with the necessary data repositories and processes requests to
 * maintain workload integrity and efficiency.
 */
@Slf4j
@Service
@AllArgsConstructor
public class TrainerWorkloadService implements WorkloadService {

    private final InMemoryDao dao;

    /**
     * Retrieves the workload for a specified trainer within a given date range.
     *
     * @param username  the username of the trainer whose workload is to be retrieved
     * @param startDate the starting date of the range for workload retrieval
     * @param endDate   the ending date of the range for workload retrieval
     */
    @Override
    @Transactional
    public TrainerWorkloadResponse getTrainerWorkloadByName(
            String username, LocalDate startDate, LocalDate endDate) {
        TrainerProfile profile = dao.findTrainerByUsername(username);
        return getWorkloadResponse(profile, getWorkloadMap(profile.trainings())
                .entrySet().stream()
                .map(entry -> new TrainerWorkloadResponse.YearSummaryResponse(
                        entry.getKey(),
                        entry.getValue().entrySet().stream()
                                .map(this::getSummaryResponse)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList()));
    }

    private TrainerWorkloadResponse.YearSummaryResponse.MonthSummaryResponse getSummaryResponse(
            Map.Entry<Month, Long> monthEntry) {
        return new TrainerWorkloadResponse.YearSummaryResponse
                .MonthSummaryResponse(monthEntry.getKey(), monthEntry.getValue());
    }

    protected TrainerWorkloadResponse getWorkloadResponse(
            TrainerProfile profile,
            List<TrainerWorkloadResponse.YearSummaryResponse> yearSummaries) {
        return new TrainerWorkloadResponse(
                profile.username(),
                profile.firstName(),
                profile.lastName(),
                profile.active() ? TrainerStatus.INACTIVE : TrainerStatus.ACTIVE,
                yearSummaries
        );
    }

    protected Map<Integer, Map<Month, Long>> getWorkloadMap(List<TrainingResponse> trainings) {
        Map<Integer, Map<Month, Long>> workloadMap = new HashMap<>();
        trainings.forEach(training -> workloadMap
                .computeIfAbsent(training.getTrainingDate().getYear(), k -> new HashMap<>())
                .merge(training.getTrainingDate().getMonth(),
                        training.getTrainingDuration(), Long::sum));
        return workloadMap;
    }
}
