package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.web.data.TraineeData;
import com.epam.esm.gym.web.data.TrainingData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest extends ControllerTest {

    private final String BASE_URL = "/api/trainees";
    private Map<String, Object> activateTrainee;
    private Map<String, String> registration;
    private Map<String, Object> trainees;
    private List<TrainerProfile> trainers;
    private List<String> trainersUsernames;
    private ProfileResponse profileResponse;
    private ProfileResponse expectedProfile;
    private TraineeRequest traineeRequest;
    private String username;

    @BeforeEach
    void setUp() {
        username = "Harry.Potter";
        trainees = TraineeData.harryMap;
        registration = TraineeData.registration;
        traineeRequest = TraineeData.traineeRequest;
        activateTrainee = TraineeData.activateTrainee;
        profileResponse = getProfileResponse(registration);
        trainersUsernames = List.of("Severus", "Albus", "Dumbledore");
        trainers = List.of(new TrainerProfile(), new TrainerProfile());
        expectedProfile = getProfileResponse("Harry", "Potter");
    }

    @Test
    void testRegisterTrainee1() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/trainees/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(traineeRequest)));
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Harry.Potter"))
                .andExpect(jsonPath("$.password").value("randomGeneratedPassword"));
    }

    @Test
    void testGetTraineeProfile2() throws Exception {
        when(traineeService.register(traineeRequest)).thenReturn(profileResponse);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/trainees/Harry.Potter")
                .content(objectMapper.writeValueAsString(traineeRequest))
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Harry"))
                .andExpect(jsonPath("$.lastName").value("Potter"))
                .andExpect(jsonPath("$.dateOfBirth").value("1980-07-31"))
                .andExpect(jsonPath("$.address").value("4 Privet Drive, Little Whinging, Surrey"));
    }

    @Test
    public void testRegisterTrainee() throws Exception {
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void testTraineeRegistration() throws Exception {
        when(traineeService.register(traineeRequest)).thenReturn(profileResponse);
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.password").value(password));
    }

    @Test
    void testTraineeRegistrations() throws Exception {
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainees)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(expectedProfile.getUsername()))
                .andExpect(jsonPath("$.password").value(expectedProfile.getPassword()));
    }

    @Test
    void testTraineeRegistrationExists() throws Exception {
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isCreated())
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
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.active").exists())
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
                .andExpect(jsonPath("$.username").value("Harry.Potter"))
                .andExpect(jsonPath("$.firstName").value("Harry"))
                .andExpect(jsonPath("$.lastName").value("Potter"))
                .andExpect(jsonPath("$.dateOfBirth").value("1980-07-31"))
                .andExpect(jsonPath("$.address").value("12 Grimmauld Place, London"))
                .andExpect(jsonPath("$.active").value(true))
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
        mockMvc.perform(delete(BASE_URL + "/Harry.Potter"))
                .andExpect(status().is2xxSuccessful());
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
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.trainers[0].trainerUsername").exists())
                .andExpect(jsonPath("$.trainers[0].trainerFirstName").exists())
                .andExpect(jsonPath("$.trainers[0].trainerLastName").exists())
                .andExpect(jsonPath("$.trainers[0].trainerSpecialization").exists());
    }

    @Test
    void testUpdateTraineeTrainerList() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("traineeUsername", "Harry.Potter");
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
        var request = Map.of("username", "Harry.Potter", "active", false);

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

    @Test
    void testUpdateTraineeTrainers() throws Exception {
        when(traineeService.updateTraineeTrainersByName(username, trainersUsernames)).thenReturn(trainers);
        mockMvc.perform(put(BASE_URL + "/" + username + "/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainersUsernames)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("trainer1"))
                .andExpect(jsonPath("$[1].username").value("trainer2"));
    }

    @Test
    void testGetTraineeTrainings() throws Exception {
        TrainingProfile trainingProfile = getProfile(TrainingData.training);
        List<TrainingResponse> trainingResponses = List.of(getTrainingResponse(TrainingData.training));
        when(traineeService.getTraineeTrainingsByName(username, trainingProfile)).thenReturn(trainingResponses);
        mockMvc.perform(get(BASE_URL + "/" + username + "/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("training1"))
                .andExpect(jsonPath("$[1].name").value("training2"));
    }

    @Test
    void testActivateDeactivateTrainee() throws Exception {
        Boolean active = true;
        mockMvc.perform(patch(BASE_URL + "/" + username + "/activate")
                        .param("active", active.toString()))
                .andExpect(status().isOk());
        verify(traineeService).activateDeactivateProfile(username, active);
    }
}
