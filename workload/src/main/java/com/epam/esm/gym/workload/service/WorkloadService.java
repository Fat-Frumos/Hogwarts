package com.epam.esm.gym.workload.service;

import com.epam.esm.gym.jms.dto.TrainerWorkloadResponse;

import java.time.LocalDate;

/**
 * Service class for managing training sessions.
 * This service provides operations to process training sessions, retrieve trainer workloads,
 * and generate summaries of trainer activities. It interacts with the
 * {@link com.epam.esm.gym.workload.dao.jpa.TrainerWorkloadRepository}
 */
public interface WorkloadService {

    /**
     * Retrieves the workload summary for a specific trainer by their username within the specified date range.
     * This method fetches the number of training hours
     * or other workload details for the trainer between the start and end dates.
     *
     * @param username  the username of the trainer whose workload is to be retrieved
     * @param startDate the start date for the period to retrieve workload data
     * @param endDate   the end date for the period to retrieve workload data
     * for the trainer during the specified date range
     */
    TrainerWorkloadResponse getTrainerWorkloadByName(String username, LocalDate startDate, LocalDate endDate);
}
