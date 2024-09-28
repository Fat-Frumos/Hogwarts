package com.epam.esm.gym.workload.client;

import com.epam.esm.gym.jms.dto.TrainerProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

/**
 * Feign client interface for interacting with the User service.
 * The client is configured to connect to the User service endpoint at {@code http://localhost:8222/api/users}.
 */
@FeignClient(name = "workload-service", url = "http://localhost:8090/api/trainers")
public interface TrainerClient {

    /**
     * Retrieves a Trainer profile by username.
     *
     * @param username the username of the user to retrieve
     * @return an {@link Optional} containing TrainerRequest if found,
     * or {@link Optional#empty()} if not found
     */
    @GetMapping("/{username}")
    TrainerProfile findByUsername(@PathVariable("username") String username);

    /**
     * Retrieves a list of all trainers from the system.
     * This method queries the underlying data source to fetch
     * the complete list of trainers, returning their details
     * encapsulated in {@link com.epam.esm.gym.jms.dto.TrainerProfile} objects.
     *
     * @return a list of {@link com.epam.esm.gym.jms.dto.TrainerProfile} containing details
     * such as usernames, names, and active status of all trainers.
     * If no trainers are found, an empty list is returned.
     */
    @GetMapping
    List<TrainerProfile> findAll();
}
