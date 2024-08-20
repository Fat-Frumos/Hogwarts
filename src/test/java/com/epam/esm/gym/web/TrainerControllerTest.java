package com.epam.esm.gym.web;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.web.data.TrainerData;
import com.epam.esm.gym.web.data.TrainingData;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainerControllerTest extends ControllerTest {
    private final String BASE_URL = "/api/trainers";
    private Map<String, String> registration;
    private Map<String, Object> trainer;
    private TrainerRequest trainerRequest;
    private ProfileResponse profileResponse;
    private String username;

    @BeforeEach
    void setUp() {
        username = "Severus.Snape";
        trainer = TrainerData.snapeMap;
        registration = TrainerData.registration;
        trainerRequest = getTrainerRequest(trainer);
        profileResponse = getProfileResponse(registration);
    }

    @Test
    void testTrainerRegistrationMissingFirstName() throws Exception {
        TrainerRequest traineeRequest = TrainerRequest.builder().lastName("Dumbledore")
                .specialization(Specialization.DEFENSE).build();
        String responseContent = mockMvc.perform(post("/api/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName").value("FirstName is required"))
                .andReturn().getResponse().getContentAsString();
        assertThat(responseContent).contains("FirstName is required");
    }

    @Test
    void testTrainerRegistrationMissingLastName() throws Exception {
        TrainerRequest traineeRequest = TrainerRequest.builder().firstName("Albus")
                .specialization(Specialization.DEFENSE).build();
        String responseContent = mockMvc.perform(post("/api/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.lastName")
                        .value("LastName is required"))
                .andReturn().getResponse().getContentAsString();
        assertThat(responseContent).contains("LastName is required");
    }

    @Test
    void testRegisterTrainerWithInvalidSpecialization() throws Exception {
        TrainerRequest traineeRequest = TrainerRequest.builder()
                .firstName("Albus").lastName("Dumbledore").build();
        mockMvc.perform(post("/api/trainers/register")
                        .content(objectMapper.writeValueAsString(traineeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.specialization")
                        .value("Specialization is required"));
    }

    @Test
    void testRegisterTrainerProfile() throws Exception {
        when(trainerService.registerTrainer(trainerRequest)).thenReturn(profileResponse);
        String result = mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertThat(objectMapper.readValue(result, ProfileResponse.class))
                .usingRecursiveComparison()
                .isEqualTo(profileResponse);

        verify(trainerService).registerTrainer(trainerRequest);
    }

    @Test
    void testExistsTrainerRegistrations() throws Exception {
        when(trainerService.registerTrainer(trainerRequest))
                .thenReturn(profileResponse)
                .thenAnswer(this::getProfileResponse);
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.password").value(profileResponse.getPassword()));

        trainer.put("username", username + ".1");

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getTrainerRequest(trainer))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Severus.Snape.1"))
                .andExpect(jsonPath("$.password").value(password));

        verify(trainerService, times(2)).registerTrainer(any(TrainerRequest.class));
    }

    @Test
    void testGetTrainerProfile() throws Exception {
        when(trainerService.getTrainer(username)).thenReturn(TrainerData.TRAINER_PROFILE);
        String result = mockMvc.perform(get(BASE_URL + "/{username}", username))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(objectMapper.readValue(result, TrainerProfile.class))
                .usingRecursiveComparison()
                .isEqualTo(TrainerData.TRAINER_PROFILE);
        verify(trainerService, times(1)).getTrainer(username);
    }


    @Test
    void testUpdateTrainerProfiles() throws Exception {
        final String username = "Horace.Slughorn";
        TrainerProfile expectedProfile = getTrainerProfile(TrainerData.horaceMap);
        when(trainerService.updateTrainer(username, TrainerData.UPDATE_REQUEST)).thenReturn(expectedProfile);
        String result = mockMvc.perform(put("/api/trainers/" + username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TrainerData.UPDATE_REQUEST)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(objectMapper.readValue(result, TrainerProfile.class))
                .usingRecursiveComparison()
                .isEqualTo(expectedProfile);
        verify(trainerService, times(1)).updateTrainer(username, TrainerData.UPDATE_REQUEST);
    }


    @Test
    void testUpdateTrainerProfile() throws Exception {
        final String username = "Horace.Slughorn";
        TrainerProfile updatedProfile = getTrainerProfile(TrainerData.horaceMap);
        when(trainerService.updateTrainer(username, TrainerData.UPDATE_REQUEST)).thenReturn(updatedProfile);
        String result = mockMvc.perform(put("/api/trainers/" + username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TrainerData.UPDATE_REQUEST)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(objectMapper.readValue(result, TrainerProfile.class))
                .usingRecursiveComparison()
                .isEqualTo(updatedProfile);
        verify(trainerService, times(1)).updateTrainer(username, TrainerData.UPDATE_REQUEST);
    }

    @Test
    void testGetTrainerTrainingsList() throws Exception {
        TrainingProfile training = getProfile(TrainingData.training);
        TrainingResponse expectedTrainingResponse = getTrainingResponse(TrainingData.training);
        when(trainingService.getTrainerTrainingsByName(training.getTrainerName(), training))
                .thenReturn(List.of(expectedTrainingResponse));
        String result = mockMvc.perform(get(BASE_URL + "/{username}/trainings", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(training)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(Arrays.asList(objectMapper.readValue(result, TrainingResponse[].class)))
                .usingRecursiveComparison()
                .isEqualTo(List.of(expectedTrainingResponse));
        verify(trainingService, times(1)).getTrainerTrainingsByName("Severus.Snape", training);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testActivateDeactivateTrainer(boolean isActive) throws Exception {
        String expectedActiveStatus = isActive ? "true" : "false";
        mockMvc.perform(patch(BASE_URL + "/" + username + "/activate")
                        .param("active", expectedActiveStatus)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(trainerService).activateDeactivateProfile(username, isActive);
    }

    @Test
    void testGetNotAssignedActiveTrainersSuccess() throws Exception {
        List<TrainerProfile> expectedTrainers = getTrainerProfiles(List.of(TrainerData.snapeMap, TrainerData.horaceMap));
        when(trainerService.getNotAssigned(username)).thenReturn(expectedTrainers);
        String result = mockMvc.perform(get("/api/trainers/{username}/unassigned", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        Assertions.assertThat(Arrays.asList(objectMapper.readValue(result, TrainerProfile[].class)))
                .usingRecursiveComparison()
                .isEqualTo(expectedTrainers);
        verify(trainerService, times(1)).getNotAssigned(username);
    }

    @Test
    void testGetNotAssignedActiveTrainersUsernameNotFound() throws Exception {
        String username = "Ron.Snape";
        when(trainerService.getNotAssigned(username)).thenReturn(Collections.emptyList());
        mockMvc.perform(get(BASE_URL + "/{username}/unassigned", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(trainerService, times(1)).getNotAssigned(username);
    }

    @Test
    void testGetNotAssignedActiveTrainersInvalidUsername() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{username}/unassigned", "user name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failure"))
                .andExpect(jsonPath("$.error").value("Invalid username"));

        verify(trainerService, times(0)).getNotAssigned("user name");
    }
}
