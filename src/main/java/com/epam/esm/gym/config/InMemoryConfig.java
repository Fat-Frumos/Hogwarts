package com.epam.esm.gym.config;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class InMemoryConfig {

    @Bean
    public Map<String, Trainer> trainerStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, Trainee> traineeStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, Training> trainingStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, User> userStorage() {
        return new HashMap<>();
    }
}
