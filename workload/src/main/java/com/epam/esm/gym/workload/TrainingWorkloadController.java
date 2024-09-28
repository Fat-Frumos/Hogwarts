package com.epam.esm.gym.workload;

import com.epam.esm.gym.jms.dto.TrainerWorkloadResponse;
import com.epam.esm.gym.workload.service.WorkloadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * REST controller for managing training sessions.
 * This controller exposes endpoints for handling training sessions, including processing workload,
 * retrieving trainer workload, and summarizing trainer activities. It delegates the operations
 * to the {@link WorkloadService} service.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/workload")
public class TrainingWorkloadController {

    private final WorkloadService workloadService;

    /**
     * Retrieves a summary of training sessions for a specific trainer within a date range.
     * This endpoint fetches a {@link TrainerWorkloadResponse}
     * for a given trainer identified by the username,
     * summarizing their activities between the specified start date and end date.
     *
     * @param username  the username of the trainer whose summary is to be retrieved.
     * @param startDate the start date of the period for which the summary is to be generated.
     * @param endDate   the end date of the period for which the summary is to be generated.
     * @return a {@link TrainerWorkloadResponse}
     * object containing the summary of training sessions.
     */
    @GetMapping("/summary/{username}")
    public ResponseEntity<TrainerWorkloadResponse> getTrainerSummary(
            @PathVariable String username,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate) {

        return ResponseEntity.ok(workloadService.getTrainerWorkloadByName(username, startDate, endDate));
    }
}
