package com.epam.esm.gym.workload.dao;

import com.epam.esm.gym.jms.dto.TrainerProfile;
import com.epam.esm.gym.jms.dto.TrainerSummary;
import com.epam.esm.gym.jms.dto.WorkloadRequest;
import com.epam.esm.gym.workload.client.TrainerClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the Trainer Data Access Object (DAO).
 * This class provides methods for managing trainer data in a temporary storage.
 * It is typically used for testing or when a persistent database is not required.
 */
@Repository
public class InMemoryTrainerDao implements InMemoryDao {
    private final TrainerClient client;

    private final ConcurrentHashMap<String, WorkloadRequest> workloadStore;
    private final ConcurrentHashMap<String, TrainerProfile> profile;
    private TrainerSummary trainerSummary;

    /**
     * In-memory Constructor.
     */
    public InMemoryTrainerDao(TrainerClient client) {
        this.client = client;
        profile = new ConcurrentHashMap<>();
        workloadStore = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves all trainer profiles stored in the in-memory database.
     *
     * @return a list of {@link TrainerProfile} objects.
     */
    @Override
    public List<TrainerProfile> findAll() {
        return new ArrayList<>(profile.values());
    }

    /**
     * Saves a list of trainer profiles to the in-memory database.
     *
     * @param trainerProfiles the list of {@link TrainerProfile} objects to be saved.
     */
    @Override
    public void save(List<TrainerProfile> trainerProfiles) {
        trainerProfiles.forEach(profile -> this.profile.put(profile.username(), profile));
    }

    /**
     * Saves a single trainer profile to the in-memory database.
     *
     * @param trainerProfile the {@link TrainerProfile} object to be saved.
     */
    @Override
    public void save(TrainerProfile trainerProfile) {
        profile.put(trainerProfile.username(), trainerProfile);
    }

    /**
     * Retrieves a single trainer profile from the in-memory database by username.
     *
     * @param username the username of the trainer to be retrieved.
     * @return the {@link TrainerProfile} object or null if not found.
     */
    @Override
    public TrainerProfile findTrainerByUsername(String username) {
        TrainerProfile trainerProfile = profile.get(username);
        if (trainerProfile == null) {
            profile.put(username, client.findByUsername(username));
        }
        return profile.get(username);
    }

    /**
     * Saves a workload request to the in-memory database.
     *
     * @param request the workload request to be saved
     */
    @Override
    public void saveWorkload(WorkloadRequest request) {
        workloadStore.put(request.trainerUsername(), request);
    }

    /**
     * Removes a workload request from the in-memory database.
     *
     * @param request the workload request to be removed
     */
    @Override
    public void removeWorkload(WorkloadRequest request) {
        workloadStore.remove(request.trainerUsername());
    }

    /**
     * Retrieves the current WorkloadRequest from the in-memory database.
     *
     * @return the Workload Request, or null if no summary exists
     */
    public WorkloadRequest getWorkloadRequest(String username){
        return workloadStore.get(username);
    }

    /**
     * Saves a trainer summary to the in-memory database.
     *
     * @param trainerSummary the trainer summary to be saved
     */
    @Override
    public void saveSummary(TrainerSummary trainerSummary) {
        this.trainerSummary = trainerSummary;
    }

    /**
     * Retrieves the current trainer summary from the in-memory database.
     *
     * @return the trainer summary, or null if no summary exists
     */
    @Override
    public TrainerSummary getSummary() {
        return trainerSummary;
    }

}
