package com.epam.esm.gym.user.service;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.jms.dto.WorkloadRequest;

/**
 * Interface for managing workload-related operations for trainers.
 * This service handles updates to workload information and retrieval of trainer workload summaries.
 */
public interface WorkloadService {

    /**
     * Updates the workload information based on the provided request.
     * This method processes the workload details and updates the database or in-memory structure accordingly.
     *
     * @param request the {@link WorkloadRequest} containing details of the workload update
     * @return a {@link MessageResponse} indicating the result of the operation (e.g., success or failure message)
     */
    MessageResponse updateWorkload(WorkloadRequest request);
}
