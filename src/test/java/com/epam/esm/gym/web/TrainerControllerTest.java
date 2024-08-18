package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.service.TraineeService;
import com.epam.esm.gym.service.TrainerService;
import com.epam.esm.gym.service.TrainingService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    private TrainingService trainingService;

    @MockBean
    private TrainerService trainerService;

    @MockBean
    private TraineeService traineeService;

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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
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
    void testDeactivateTrainer() throws Exception {
        var request = Map.of("username", "severus.snape", "isActive", false);
        mockMvc.perform(patch(BASE_URL + "/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTrainerTrainings() throws Exception {
        TrainingProfile request = new TrainingProfile();
        List<TrainingResponse> trainingResponses = List.of(new TrainingResponse(), new TrainingResponse());
        when(trainingService.getTrainerTrainingsByName(username, request)).thenReturn(trainingResponses);
        mockMvc.perform(get(BASE_URL + "/" + username + "/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("training1"))
                .andExpect(jsonPath("$[1].name").value("training2"));
    }

    @Test
    void testUpdateTraineeTrainers() throws Exception {
        TrainerProfile trainer1 = new TrainerProfile();
        trainer1.setUsername("trainer1");
        trainer1.setFirstName("Trainer");
        trainer1.setLastName("One");
        trainer1.setSpecialization("Specialization1");
        trainer1.setActive(true);

        TrainerProfile trainer2 = new TrainerProfile();
        trainer2.setUsername("trainer2");
        trainer2.setFirstName("Trainer");
        trainer2.setLastName("Two");
        trainer2.setSpecialization("Specialization2");
        trainer2.setActive(true);
        List<TrainerProfile> trainerProfiles = List.of(trainer1, trainer2);
        List<String> trainerUsernames = List.of("trainer1", "trainer2");
        when(traineeService.updateTraineeTrainersByName(username, trainerUsernames)).thenReturn(trainerProfiles);

        mockMvc.perform(put(BASE_URL + "/" + username + "/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerUsernames)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("trainer1"))
                .andExpect(jsonPath("$[0].firstName").value("Trainer"))
                .andExpect(jsonPath("$[0].lastName").value("One"))
                .andExpect(jsonPath("$[0].specialization").value("Specialization1"))
                .andExpect(jsonPath("$[0].isActive").value(true))
                .andExpect(jsonPath("$[1].username").value("trainer2"))
                .andExpect(jsonPath("$[1].firstName").value("Trainer"))
                .andExpect(jsonPath("$[1].lastName").value("Two"))
                .andExpect(jsonPath("$[1].specialization").value("Specialization2"))
                .andExpect(jsonPath("$[1].isActive").value(true));
    }

    @Test
    void testActivateTrainer() throws Exception {
        mockMvc.perform(patch(BASE_URL + "/" + username + "/activate")
                        .param("isActive", "false"))
                .andExpect(status().isOk());
        verify(trainerService).activateDeactivateProfile(username, false);
    }

    @Test
    void testRegisterTrainerProfile() throws Exception {
        ProfileResponse profileResponse = getProfileResponse(registration);
        TrainerRequest trainerRequest = getTrainerRequest(trainers);
        when(trainerService.registerTrainer(trainerRequest)).thenReturn(profileResponse);
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("severus.snape"))
                .andExpect(jsonPath("$.password").value("randomPassword123"));
    }
}
