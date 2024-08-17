package com.epam.esm.gym.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TraineeControllerTest extends ControllerTest {

    private final String BASE_URL = "/api/trainees";
    private Map<String, Object> activateTrainee;
    private Map<String, String> registration;
    private Map<String, Object> trainees;
    private String username;

    @BeforeEach
    void setUp() {

        username = "harry.potter";

        registration = new HashMap<>();
        registration.put("firstName", "Harry");
        registration.put("lastName", "Potter");
        registration.put("dateOfBirth", "1980-07-31");
        registration.put("address", "4 Privet Drive, Little Whinging, Surrey");

        trainees = new HashMap<>();
        trainees.put("username", "harry.potter");
        trainees.put("firstName", "Harry");
        trainees.put("lastName", "Potter");
        trainees.put("dateOfBirth", "1980-07-31");
        trainees.put("address", "12 Grimmauld Place, London");
        trainees.put("isActive", true);

        activateTrainee = new HashMap<>();
        activateTrainee.put("username", "harry.potter");
        activateTrainee.put("isActive", false);
    }

    @Test
    void testTraineeRegistration() throws Exception {

        var expectedCredentials = generateUserCredentials("Harry", "Potter");
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainees)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(expectedCredentials.get("username")))
                .andExpect(jsonPath("$.password").value(expectedCredentials.get("password")));
    }

    @Test
    void testTraineeRegistrationExists() throws Exception {
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void testGetTraineeProfile() throws Exception {

        mockMvc.perform(get(BASE_URL + "/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Harry"))
                .andExpect(jsonPath("$.lastName").value("Potter"))
                .andExpect(jsonPath("$.dateOfBirth").value("1980-07-31"))
                .andExpect(jsonPath("$.address").value("4 Privet Drive, Little Whinging, Surrey"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.isActive").exists())
                .andExpect(jsonPath("$.trainers").isArray())
                .andExpect(jsonPath("$.trainers[0].trainerUsername").exists())
                .andExpect(jsonPath("$.trainers[0].trainerFirstName").exists())
                .andExpect(jsonPath("$.trainers[0].trainerLastName").exists())
                .andExpect(jsonPath("$.trainers[0].trainerSpecialization").exists());
    }

    @Test
    void testUpdateTraineeProfileValueAsString() throws Exception {
        mockMvc.perform(put(BASE_URL + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("harry.potter"))
                .andExpect(jsonPath("$.firstName").value("Harry"))
                .andExpect(jsonPath("$.lastName").value("Potter"))
                .andExpect(jsonPath("$.dateOfBirth").value("1980-07-31"))
                .andExpect(jsonPath("$.address").value("12 Grimmauld Place, London"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.trainers").isArray());
    }

    @Test
    void testUpdateTraineeTrainersList() throws Exception {
        var trainers = List.of(
                Map.of("trainerUsername", "severus.snape"));

        mockMvc.perform(put(BASE_URL + "/{username}/trainers", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainers)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainers[0].trainerUsername").value("severus.snape"));
    }

    @Test
    void testGetNotAssignedTrainerProfiles() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{username}/unassigned", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainerUsername").exists())
                .andExpect(jsonPath("$[0].trainerFirstName").exists())
                .andExpect(jsonPath("$[0].trainerLastName").exists())
                .andExpect(jsonPath("$[0].trainerSpecialization").exists());
    }

    @Test
    void testDeleteTraineeProfile() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/harry.potter"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateTraineeProfile() throws Exception {

        mockMvc.perform(put(BASE_URL + "/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainees)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value("Harry"))
                .andExpect(jsonPath("$.lastName").value("Potter"))
                .andExpect(jsonPath("$.dateOfBirth").value("1980-07-31"))
                .andExpect(jsonPath("$.address").value("4 Privet Drive"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.trainers[0].trainerUsername").exists())
                .andExpect(jsonPath("$.trainers[0].trainerFirstName").exists())
                .andExpect(jsonPath("$.trainers[0].trainerLastName").exists())
                .andExpect(jsonPath("$.trainers[0].trainerSpecialization").exists());
    }

    @Test
    void testUpdateTraineeTrainerList() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("traineeUsername", "harry.potter");
        request.put("trainers", List.of("severus.snape"));

        mockMvc.perform(put(BASE_URL + "/trainers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainers").isArray())
                .andExpect(jsonPath("$.trainers[0].username").value("severus.snape"));
    }

    @Test
    void testGetTraineeTrainingsList() throws Exception {


        mockMvc.perform(get(BASE_URL + "/{username}/trainings", username)
                        .param("periodFrom", "2024-01-01")
                        .param("periodTo", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].trainingName").exists())
                .andExpect(jsonPath("$[0].trainingDate").exists())
                .andExpect(jsonPath("$[0].trainingType").exists())
                .andExpect(jsonPath("$[0].trainingDuration").exists())
                .andExpect(jsonPath("$[0].trainerName").exists());
    }

    @Test
    void testDeactivateTrainee() throws Exception {
        var request = Map.of("username", "harry.potter", "isActive", false);

        mockMvc.perform(patch(BASE_URL + "/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    void testActivateTrainee() throws Exception {
        mockMvc.perform(patch(BASE_URL + "/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activateTrainee)))
                .andExpect(status().isOk());
    }

}