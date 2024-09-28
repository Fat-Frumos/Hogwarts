package com.epam.esm.gym.workload.dao;

import com.epam.esm.gym.jms.dto.TrainerProfile;
import com.epam.esm.gym.jms.dto.TrainerSummary;
import com.epam.esm.gym.jms.dto.WorkloadRequest;

import java.util.List;

/**
 * InMemoryDao defines methods for managing trainer profiles in an in-memory database.
 */
public interface InMemoryDao {

    /**
     * Retrieves all trainer profiles from the in-memory database.
     *
     * @return a list of {@link TrainerProfile} objects stored in the database.
     */
    List<TrainerProfile> findAll();

    /**
     * Saves a list of trainer profiles to the in-memory database.
     *
     * @param trainerProfiles the list of {@link TrainerProfile} objects to be saved.
     */
    void save(List<TrainerProfile> trainerProfiles);

    /**
     * Saves a single trainer profile to the in-memory database.
     *
     * @param trainerProfile the {@link TrainerProfile} object to be saved.
     */
    void save(TrainerProfile trainerProfile);

    /**
     * Retrieves a trainer profile by username from the in-memory database.
     *
     * @param username the username of the trainer to be retrieved.
     * @return the {@link TrainerProfile} object if found, otherwise null.
     */
    TrainerProfile findTrainerByUsername(String username);

    /**
     * Saves a workload request to the in-memory database.
     *
     * @param request the workload request to be saved
     */
    void saveWorkload(WorkloadRequest request);

    /**
     * Removes a workload request from the in-memory database.
     *
     * @param request the workload request to be removed
     */
    void removeWorkload(WorkloadRequest request);

    /**
     * Saves a trainer summary to the in-memory database.
     *
     * @param trainerSummary the trainer summary to be saved
     */
    void saveSummary(TrainerSummary trainerSummary);

    /**
     * Retrieves Trainer Summary from the in-memory database.
     *
     * @return a list of {@link TrainerSummary} objects stored in the database.
     */
    TrainerSummary getSummary();

    /**
     * Retrieves WorkloadRequest from the in-memory database.
     *
     * @return a {@link WorkloadRequest} object stored in the database.
     */
    WorkloadRequest getWorkloadRequest(String username);
}
