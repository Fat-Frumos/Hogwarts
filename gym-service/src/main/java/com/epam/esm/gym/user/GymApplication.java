package com.epam.esm.gym.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The entry point of the Gym application.
 *
 * <p>This class contains the main method which is used to launch the Spring Boot application.</p>
 */
@EnableFeignClients
@SpringBootApplication
public class GymApplication {

    /**
     * The main method that starts the Spring Boot application.
     *
     * <p>This method initializes the application context and starts the embedded server.</p>
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(GymApplication.class, args);
    }
}
