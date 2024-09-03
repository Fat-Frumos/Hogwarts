package com.epam.esm.gym.config;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for JDBC setup and JPA repositories.
 *
 * <p>This class configures the JDBC properties and sets up JPA repositories for
 * the application. It includes database connection settings and enables JPA
 * repository support within the specified base packages.</p>
 *
 * <p>JDBC configuration is essential for establishing database connections and
 * managing persistence layers in the application.</p>
 *
 * @author Pavlo Poliak
 * @see org.springframework.data.jpa.repository.config.EnableJpaRepositories
 * @since 1.0
 */
@Configuration
public class InMemoryConfig {

    /**
     * Configures a bean for storing trainers using an in-memory {@link Map}.
     *
     * <p>This method initializes a {@link HashMap} to act as the storage for {@link Trainer}
     * objects, identified by a unique string key, such as the trainer's username. This in-memory
     * approach allows for quick access and management of trainer data during application runtime.</p>
     *
     * <p>The storage map is useful in scenarios where trainer data needs to be temporarily held
     * without persisting to a database, enabling testing, caching, or other in-memory operations.</p>
     *
     * @return a new {@link HashMap} instance for managing {@link Trainer} objects.
     * @author Pavlo Poliak
     * @see java.util.HashMap
     * @see com.epam.esm.gym.domain.Trainer
     * @since 1.0
     */
    @Bean
    public Map<String, Trainer> trainerStorage() {
        return new HashMap<>();
    }


    /**
     * Configures a bean for storing trainees using an in-memory {@link Map}.
     *
     * <p>This method sets up a {@link HashMap} to serve as the storage for {@link Trainee}
     * objects, identified by a unique string key. The map provides a simple and efficient
     * way to handle trainee data in-memory, useful for temporary data management during
     * testing or runtime operations.</p>
     *
     * <p>Using this storage solution allows for easy retrieval and manipulation of trainee data
     * without the need for persistent database access.</p>
     *
     * @return a new {@link HashMap} instance for managing {@link Trainee} objects.
     * @see java.util.HashMap
     * @see com.epam.esm.gym.domain.Trainee
     */
    @Bean
    public Map<String, Trainee> traineeStorage() {
        return new HashMap<>();
    }

    /**
     * Configures a bean for storing training sessions using an in-memory {@link Map}.
     *
     * <p>This method initializes a {@link HashMap} to store {@link Training} objects, keyed
     * by a unique string identifier, such as the training session name. This map acts as a
     * temporary storage solution for managing training data during application runtime, allowing
     * for quick access and manipulation.</p>
     *
     * <p>The in-memory storage is particularly useful for non-persistent use cases like testing,
     * caching, or managing session data dynamically.</p>
     *
     * @return a new {@link HashMap} instance for managing {@link Training} objects.
     * @author Pavlo Poliak
     * @see java.util.HashMap
     * @see com.epam.esm.gym.domain.Training
     * @since 1.0
     */
    @Bean
    public Map<String, Training> trainingStorage() {
        return new HashMap<>();
    }

    /**
     * Configures a bean for storing user data using an in-memory {@link Map}.
     *
     * <p>This method sets up a {@link HashMap} to hold {@link User} objects, identified by
     * a unique string key, such as a username. The in-memory map facilitates quick data
     * access and manipulation, making it suitable for scenarios like caching or testing without
     * relying on a persistent database.</p>
     *
     * <p>In-memory user storage provides flexibility for handling user-related operations dynamically
     * during the application's runtime.</p>
     *
     * @return a new {@link HashMap} instance for managing {@link User} objects.
     * @author Pavlo Poliak
     * @see java.util.HashMap
     * @see com.epam.esm.gym.domain.User
     * @since 1.0
     */
    @Bean
    public Map<String, User> userStorage() {
        return new HashMap<>();
    }
}
