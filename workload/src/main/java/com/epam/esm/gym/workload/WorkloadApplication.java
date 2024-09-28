package com.epam.esm.gym.workload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The entry point of the Session Application.
 * This class is responsible for bootstrapping the Spring Boot application by running the {@link SpringApplication}.
 * It enables Feign clients through the {@link EnableFeignClients} annotation to allow for declarative HTTP clients.
 */
@EnableFeignClients
@SpringBootApplication
public class WorkloadApplication {
    /**
     * The main method, which is the entry point of the Spring Boot application.
     * It initializes the Spring application context, starting the application
     * and enabling the various configurations provided by the Spring Boot application.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(WorkloadApplication.class, args);
    }
}
