package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.auth.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.SlimTrainerProfile;
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
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the {@link TraineeController} class.
 *
 * <p>This class extends {@link ControllerTest} and provides specific test cases for the
 * {@link TraineeController} to ensure that its endpoints are functioning as expected. It uses
 * Mockito for mocking dependencies and Spring's MockMvc to perform and validate HTTP requests.</p>
 *
 * <p>Tests cover various scenarios for the TraineeController's endpoints including creating,
 * retrieving, and updating trainees. Each test method is designed to verify that the controller
 * interacts with the service layer correctly and handles requests and responses as intended.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
class TraineeControllerTest extends ControllerTest {

    private static final String base_url = "/api/trainees";
    private static final String username = "Harry.Potter";
    private Map<String, String> params;

    @BeforeEach
    void setUp() {
        params = Map.of(
                "periodFrom", "2024-01-01",
                "periodTo", "2024-12-31",
                "trainerName", "Minerva McGonagall",
                "trainingType", "TRANSFIGURATION"
        );
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeMissingRegistrationArgumentsProvider.class)
    void testTraineeRegistrationMissingRequiredFields(
            TraineeRequest traineeRequest, ResponseEntity<MessageResponse> expectedResponse) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/trainees/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(traineeRequest)));
        resultActions.andExpect(status().is(expectedResponse.getStatusCode().value()));
        String content = resultActions.andReturn().getResponse().getContentAsString();
        String actualMessage = objectMapper.readValue(content, MessageResponse.class).getMessage();
        Arrays.stream(Objects.requireNonNull(expectedResponse.getBody()).getMessage().split(", "))
                .forEach(expectedMessage -> assertThat(actualMessage).contains(expectedMessage));
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeRegistrationArgumentsProvider.class)
    void testRegisterTrainee(TraineeRequest request, ResponseEntity<ProfileResponse> response) throws Exception {
        when(service.register(request)).thenReturn(response);
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
    void testGetTraineeProfile(
            String username, ResponseEntity<TraineeProfile> expectedResponse) throws Exception {
        when(service.getTraineeProfileByName(username)).thenReturn(expectedResponse);

        String result = mockMvc.perform(get("/api/trainees/{username}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedResponse.getStatusCode().value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        TraineeProfile actualProfile = objectMapper.readValue(result, TraineeProfile.class);
        Assertions.assertThat(actualProfile)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse.getBody());
        verify(service).getTraineeProfileByName(username);
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(NotFoundTraineeProfileArgumentsProvider.class)
    void testGetNotFoundTraineeProfile(
            String username, ResponseEntity<TraineeProfile> expectedResponse) throws Exception {
        when(service.getTraineeProfileByName(username)).thenReturn(expectedResponse);
        mockMvc.perform(get("/api/trainees/{username}", username))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(TraineeTrainersResponseEntityProfileArgumentsProvider.class)
    void testUpdateTraineeTrainers(
            String username, List<String> trainersUsernames,
            ResponseEntity<List<SlimTrainerProfile>> expectedResponse) throws Exception {
        when(service.updateTraineeTrainersByName(username, trainersUsernames)).thenReturn(expectedResponse);
        ResultActions resultActions = mockMvc.perform(put("/api/trainees/{username}/trainers", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainersUsernames)));
        resultActions.andExpect(status().is(expectedResponse.getStatusCode().value()));
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        assertThat(responseContent).isEqualTo(objectMapper.writeValueAsString(expectedResponse.getBody()));
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TraineeTrainingResponseArgumentsProvider.class)
    void testGetTraineeTrainings(
            String username, TrainingProfile request,
            ResponseEntity<List<TrainingResponse>> expectedResponse) throws Exception {
        when(service.getTraineeTrainingsByName(username, params)).thenReturn(expectedResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/trainees/{username}/trainings", username)
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
        mockMvc.perform(delete(base_url + "/" + username))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testActivateDeactivateTrainee() throws Exception {
        Boolean active = true;
        mockMvc.perform(patch(base_url + "/" + username + "/activate")
                        .param("active", active.toString()))
                .andExpect(status().isOk());
        verify(service).activateDeactivateProfile(username, active);
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TraineeTrainingsNotFoundArgumentsProvider.class)
    void testTraineeTrainingsNotFound(
            String username, ResponseEntity<List<TrainingResponse>> expectedResponse) throws Exception {
        when(service.getTraineeProfileByName(username)).thenReturn(ResponseEntity.status(NOT_FOUND).build());
        mockMvc.perform(get("/api/trainees/{username}/trainings", username))
                .andExpect(status().is(expectedResponse.getStatusCode().value()));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(TraineeTrainingsNotFoundArgumentsProvider.class)
    void testTraineeProfileNotFound(String username) throws Exception {
        when(service.getTraineeProfileByName(username)).thenReturn(ResponseEntity.status(NOT_FOUND).build());
        mockMvc.perform(get("/api/trainees/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(TraineeTrainingsNotFoundArgumentsProvider.class)
    void testTraineeProfileNotFoundWhenDelete(String username) throws Exception {
        when(service.deleteTrainee(username)).thenReturn(ResponseEntity.status(NOT_FOUND).build());
        ResultActions resultActions = mockMvc.perform(delete("/api/trainees/{username}", username));
        resultActions.andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void testUpdateTraineeProfile(
            String username, ResponseEntity<TraineeProfile> response, TraineeRequest request) throws Exception {
        when(service.updateTrainee(username, request)).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(put("/api/trainees/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().is(response.getStatusCode().value()));

        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        String expectedContent = objectMapper.writeValueAsString(response.getBody());

        assertThat(responseContent).isEqualTo(expectedContent);
    }
}
