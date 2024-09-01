package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.web.provider.trainee.NotFoundTraineeProfileArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeMissingRegistrationArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeProfileArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeRegistrationArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainersResponseEntityProfileArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainingResponseArgumentsProvider;
import com.epam.esm.gym.web.provider.trainee.TraineeTrainingsNotFoundArgumentsProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TraineeControllerTest extends ControllerTest {

    private final String BASE_URL = "/api/trainees";
    private String username;
    private Map<String, String> params;

    @BeforeEach
    void setUp() {
        username = "Harry.Potter";
        params = Map.of(
                "periodFrom", "2024-01-01",
                "periodTo", "2024-12-31",
                "trainerName", "Minerva McGonagall",
                "trainingType", "TRANSFIGURATION"
        );
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeMissingRegistrationArgumentsProvider.class)
    void testTraineeRegistrationMissingRequiredFields(TraineeRequest traineeRequest, ResponseEntity<MessageResponse> expectedResponse) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/trainees/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(traineeRequest)));
        resultActions.andExpect(status().is(expectedResponse.getStatusCode().value()));
        String actualMessage = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), MessageResponse.class).getMessage();
        Arrays.stream(Objects.requireNonNull(expectedResponse.getBody()).getMessage().split(", "))
                .forEach(expectedMessage -> assertThat(actualMessage).contains(expectedMessage));
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeRegistrationArgumentsProvider.class)
    void testRegisterTrainee(TraineeRequest request, ResponseEntity<ProfileResponse> response) throws Exception {
        when(traineeService.register(request)).thenReturn(response);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/api/trainees/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();
        ProfileResponse actualProfile = objectMapper.readValue(result, ProfileResponse.class);
        Assertions.assertThat(actualProfile).usingRecursiveComparison().isEqualTo(response.getBody());
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void testGetTraineeProfile(String username, ResponseEntity<TraineeProfile> expectedResponse) throws Exception {
        when(traineeService.getTraineeProfileByName(username)).thenReturn(expectedResponse);

        String result = mockMvc.perform(get("/api/trainees/{username}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedResponse.getStatusCode().value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        TraineeProfile actualProfile = objectMapper.readValue(result, TraineeProfile.class);
        Assertions.assertThat(actualProfile)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse.getBody());
        verify(traineeService).getTraineeProfileByName(username);
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(NotFoundTraineeProfileArgumentsProvider.class)
    public void testGetNotFoundTraineeProfile(String username, ResponseEntity<TraineeProfile> expectedResponse) throws Exception {
        when(traineeService.getTraineeProfileByName(username)).thenReturn(expectedResponse);
        mockMvc.perform(get("/api/trainees/{username}", username))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(TraineeTrainersResponseEntityProfileArgumentsProvider.class)
    void testUpdateTraineeTrainers(String username, List<String> trainersUsernames, ResponseEntity<List<TrainerProfile>> expectedResponse) throws Exception {
        when(traineeService.updateTraineeTrainersByName(username, trainersUsernames)).thenReturn(expectedResponse);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/trainees/{username}/trainers", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainersUsernames)));
        resultActions.andExpect(status().is(expectedResponse.getStatusCode().value()));
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        assertThat(responseContent).isEqualTo(objectMapper.writeValueAsString(expectedResponse.getBody()));
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TraineeTrainingResponseArgumentsProvider.class)
    void testGetTraineeTrainings(String username, TrainingProfile request, ResponseEntity<List<TrainingResponse>> expectedResponse) throws Exception {
        when(traineeService.getTraineeTrainingsByName(username, params)).thenReturn(expectedResponse);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/trainees/{username}/trainings", username)
                .contentType(MediaType.APPLICATION_JSON)
                .param("periodFrom", request.getPeriodFrom() != null ? request.getPeriodFrom().toString() : "")
                .param("periodTo", request.getPeriodTo() != null ? request.getPeriodTo().toString() : "")
                .param("trainerName", request.getTrainerName() != null ? request.getTrainerName() : "")
                .param("trainingType", request.getTrainingType() != null ? request.getTrainingType() : ""));

        resultActions.andExpect(status().isOk());
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        String expectedContent = objectMapper.writeValueAsString(expectedResponse.getBody());
        assertThat(responseContent).isEqualTo(expectedContent);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteTraineeProfile() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + username))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testActivateDeactivateTrainee() throws Exception {
        Boolean active = true;
        mockMvc.perform(patch(BASE_URL + "/" + username + "/activate")
                        .param("active", active.toString()))
                .andExpect(status().isOk());
        verify(traineeService).activateDeactivateProfile(username, active);
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TraineeTrainingsNotFoundArgumentsProvider.class)
    void testTraineeTrainingsNotFound(String username, ResponseEntity<List<TrainingResponse>> expectedResponse) throws Exception {
        when(traineeService.getTraineeProfileByName(username)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/trainees/{username}/trainings", username));
        resultActions.andExpect(status().is(expectedResponse.getStatusCode().value()));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(TraineeTrainingsNotFoundArgumentsProvider.class)
    void testTraineeProfileNotFound(String username) throws Exception {
        when(traineeService.getTraineeProfileByName(username)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        mockMvc.perform(get("/api/trainees/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(TraineeTrainingsNotFoundArgumentsProvider.class)
    void testTraineeProfileNotFoundWhenDelete(String username) throws Exception {
        when(traineeService.deleteTrainee(username)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/trainees/{username}", username));
        resultActions.andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void testUpdateTraineeProfile(String username, ResponseEntity<TraineeProfile> expectedResponse, TraineeRequest request) throws Exception {
        when(traineeService.updateTrainee(username, request)).thenReturn(expectedResponse);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/trainees/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        resultActions.andExpect(status().is(expectedResponse.getStatusCode().value()));
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        String expectedContent = objectMapper.writeValueAsString(expectedResponse.getBody());
        assertThat(responseContent).isEqualTo(expectedContent);
    }
}
