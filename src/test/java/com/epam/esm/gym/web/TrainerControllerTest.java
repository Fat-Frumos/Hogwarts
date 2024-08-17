package com.epam.esm.gym.web;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainerControllerTest extends ControllerTest {

    private final String BASE_URL = "/api/trainers";
    private Map<String, Object> activateTrainer;
    private Map<String, String> registration;
    private Map<String, Object> trainers;
    private String username;

    @BeforeEach
    void setUp() {
        username = "severus.snape";

        registration = new HashMap<>();
        registration.put("firstName", "Severus");
        registration.put("lastName", "Snape");
        registration.put("specialization", "Potions");

        trainers = new HashMap<>();
        trainers.put("username", "severus.snape");
        trainers.put("firstName", "Severus");
        trainers.put("lastName", "Snape");
        trainers.put("specialization", "Potions");
        trainers.put("isActive", true);

        activateTrainer = new HashMap<>();
        activateTrainer.put("username", "severus.snape");
        activateTrainer.put("isActive", false);
    }

    @Test
    void testTrainerRegistrationExists() throws Exception {
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void testTrainerRegistration() throws Exception {
        var request = Map.of(
                "firstName", "Severus",
                "lastName", "Snape",
                "specialization", "Potions");

        var expectedCredentials = generateUserCredentials("Severus", "Snape");
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(expectedCredentials.get("username")))
                .andExpect(jsonPath("$.password").value(expectedCredentials.get("password")));
    }

    @Test
    void testGetTrainerProfile() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Severus"))
                .andExpect(jsonPath("$.lastName").value("Snape"))
                .andExpect(jsonPath("$.specialization").value("Potions"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.trainees[0].traineeUsername").exists())
                .andExpect(jsonPath("$.trainees[0].traineeFirstName").exists())
                .andExpect(jsonPath("$.trainees[0].traineeLastName").exists())
                .andExpect(jsonPath("$.isActive").exists())
                .andExpect(jsonPath("$.trainees").isArray());
    }


    @Test
    void testUpdateTrainerProfile() throws Exception {
        mockMvc.perform(put(BASE_URL + "/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainers)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value("Severus"))
                .andExpect(jsonPath("$.lastName").value("Snape"))
                .andExpect(jsonPath("$.specialization").value("Potions"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.trainees[0].traineeUsername").exists())
                .andExpect(jsonPath("$.trainees[0].traineeFirstName").exists())
                .andExpect(jsonPath("$.trainees[0].traineeLastName").exists())
                .andExpect(jsonPath("$.trainees").isArray());
    }

    @Test
    void testGetUnassignedTrainers() throws Exception {
        mockMvc.perform(get(BASE_URL + "/unassigned")
                        .param("username", "harry.potter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").exists());
    }

    @Test
    void testGetTrainerTrainingsList() throws Exception {
        mockMvc.perform(get(BASE_URL + "/severus.snape/trainings")
                        .param("periodFrom", "2024-01-01")
                        .param("periodTo", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].trainingName").exists())
                .andExpect(jsonPath("$[0].trainingDate").exists())
                .andExpect(jsonPath("$[0].trainingType").exists())
                .andExpect(jsonPath("$[0].trainingDuration").exists())
                .andExpect(jsonPath("$[0].traineeName").exists());
    }

    @Test
    void testActivateTrainer() throws Exception {
        mockMvc.perform(patch(BASE_URL + "/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activateTrainer)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeactivateTrainer() throws Exception {
        var request = Map.of("username", "severus.snape", "isActive", false);
        mockMvc.perform(patch(BASE_URL + "/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
