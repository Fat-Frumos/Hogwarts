package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.web.provider.trainer.ExistsTrainerRegistrationsArgumentsProvider;
import com.epam.esm.gym.web.provider.trainer.NotAssignedActiveTrainersArgumentsProvider;
import com.epam.esm.gym.web.provider.trainer.RegisterTrainerArgumentsProvider;
import com.epam.esm.gym.web.provider.trainer.TrainerMissingRegistrationArgumentsProvider;
import com.epam.esm.gym.web.provider.trainer.TrainerProfileArgumentsProvider;
import com.epam.esm.gym.web.provider.trainer.TrainerProfileNotFoundArgumentsProvider;
import com.epam.esm.gym.web.provider.trainer.TrainerTrainingsArgumentsProvider;
import com.epam.esm.gym.web.provider.trainer.UpdateTrainerArgumentsProvider;
import com.epam.esm.gym.web.provider.trainer.UpdateTrainerNotFoundArgumentsProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the {@link TrainerController} class.
 *
 * <p>This class extends {@link ControllerTest} and provides specific test cases for the
 * {@link TrainerController} to ensure that its endpoints function as expected. The tests use
 * Mockito to mock the dependencies and Spring's MockMvc for performing and validating HTTP
 * requests.</p>
 *
 * <p>Test cases include scenarios for creating, retrieving, updating, and deleting trainers.
 * Each test method verifies that the controller interacts correctly with the service layer and
 * handles requests and responses as intended, ensuring the API behaves correctly under various
 * conditions.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
class TrainerControllerTest extends ControllerTest {
    private static final String base_url = "/api/trainers";
    private String username;

    @BeforeEach
    void setUp() {
        username = "Severus.Snape";
    }

    @ParameterizedTest
    @ArgumentsSource(RegisterTrainerArgumentsProvider.class)
    @ArgumentsSource(TrainerProfileNotFoundArgumentsProvider.class)
    void testRegisterTrainerProfileWhenNotFound(
            TrainerRequest request, ResponseEntity<ProfileResponse> expectedResponse) throws Exception {
        when(trainerService.registerTrainer(request)).thenReturn(expectedResponse);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/trainers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().is(expectedResponse.getStatusCode().value()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse.getBody())));
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testGetTrainerProfileWhenNotFound() throws Exception {
        String username = "Trainer";
        ResponseEntity<TrainerProfile> expectedResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(trainerService.getTrainerProfileByName(username)).thenReturn(expectedResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/trainers/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedResponse.getStatusCode().value()));
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TrainerProfileNotFoundArgumentsProvider.class)
    void testGetTrainerProfileWhenNotFounds(
            TrainerRequest request, ResponseEntity<TrainerProfile> expectedResponse) throws Exception {
        when(trainerService.getTrainerProfileByName(request.getFirstName())).thenReturn(expectedResponse);
        ResultActions resultActions = mockMvc.perform(get("/api/trainers/{username}", request.getFirstName())
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().is(expectedResponse.getStatusCode().value()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse.getBody())));
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(UpdateTrainerNotFoundArgumentsProvider.class)
    void testUpdateTrainerProfileWhenNotFound(
            String username, TrainerUpdateRequest request,
            ResponseEntity<TrainerProfile> expectedResponse) throws Exception {
        when(trainerService.updateTrainer(username, request)).thenReturn(expectedResponse);
        ResultActions resultActions = mockMvc.perform(put("/api/trainers/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        resultActions.andExpect(status().is(expectedResponse.getStatusCode().value()));
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TrainerProfileArgumentsProvider.class)
    void testGetTrainerProfile(String username, TrainerProfile expectedResponse) throws Exception {
        when(trainerService.getTrainerProfileByName(username)).thenReturn(ResponseEntity.ok(expectedResponse));
        ResultActions resultActions = mockMvc.perform(get("/api/trainers/{username}", username)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(UpdateTrainerArgumentsProvider.class)
    void testUpdateTrainerProfile(
            String username, TrainerUpdateRequest request,
            ResponseEntity<TrainerProfile> expectedResponse) throws Exception {
        when(trainerService.updateTrainer(username, request)).thenReturn(expectedResponse);
        ResultActions resultActions = mockMvc.perform(put("/api/trainers/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().is(expectedResponse.getStatusCode().value()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse.getBody())));
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TrainerTrainingsArgumentsProvider.class)
    void testGetTrainerTrainings(
            String username, TrainingProfile request,
            ResponseEntity<List<TrainingResponse>> expectedResponse) throws Exception {
        when(trainingService.getTrainerTrainingsByName(username, request)).thenReturn(expectedResponse);
        ResultActions resultActions = mockMvc.perform(get("/api/trainers/{username}/trainings", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().is(expectedResponse.getStatusCode().value()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse.getBody())));
    }


    @ParameterizedTest
    @ArgumentsSource(RegisterTrainerArgumentsProvider.class)
    void testRegisterTrainerProfiles(
            TrainerRequest trainerRequest,
            ResponseEntity<ProfileResponse> expectedResponse) throws Exception {
        when(trainerService.registerTrainer(trainerRequest)).thenReturn(expectedResponse);
        String result = mockMvc.perform(post("/api/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ProfileResponse expectedBody = expectedResponse.getBody();

        assertThat(objectMapper.readValue(result, ProfileResponse.class))
                .usingRecursiveComparison()
                .isEqualTo(expectedBody);
        verify(trainerService).registerTrainer(trainerRequest);
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ValueSource(booleans = {true, false})
    void testActivateDeactivateTrainer(boolean isActive) throws Exception {
        String expectedActiveStatus = isActive ? "true" : "false";
        mockMvc.perform(patch(base_url + "/" + username + "/activate")
                        .param("active", expectedActiveStatus)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(trainerService).activateDeactivateProfile(username, isActive);
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(NotAssignedActiveTrainersArgumentsProvider.class)
    void testGetNotAssignedActiveTrainersSuccess(
            String username, ResponseEntity<List<TrainerProfile>> expectedResponse) throws Exception {
        when(trainerService.getNotAssigned(username)).thenReturn(expectedResponse);
        String result = mockMvc.perform(get("/api/trainers/{username}/unassigned", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();
        List<TrainerProfile> actualTrainers = Arrays.asList(objectMapper.readValue(result, TrainerProfile[].class));

        assertThat(actualTrainers)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse.getBody());
        verify(trainerService, times(1)).getNotAssigned(username);
    }


    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(NotAssignedActiveTrainersArgumentsProvider.class)
    void testGetNotAssignedActiveTrainersUsernameNotFound(
            String username, ResponseEntity<List<TrainerProfile>> expectedTrainers) throws Exception {
        when(trainerService.getNotAssigned(username)).thenReturn(expectedTrainers);
        MvcResult result = mockMvc.perform(get(base_url + "/{username}/unassigned", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        List<TrainerProfile> actualTrainers = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertThat(actualTrainers)
                .usingRecursiveComparison()
                .isEqualTo(expectedTrainers.getBody());

        verify(trainerService, times(1)).getNotAssigned(username);
    }

    @ParameterizedTest
    @ArgumentsSource(ExistsTrainerRegistrationsArgumentsProvider.class)
    void testExistsTrainerRegistrations(
            String username, ResponseEntity<ProfileResponse> profileResponse,
            TrainerRequest trainerRequest) throws Exception {
        when(trainerService.registerTrainer(trainerRequest)).thenReturn(profileResponse);
        String actual = Objects.requireNonNull(profileResponse.getBody()).getPassword();
        mockMvc.perform(post(base_url + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.password").value(actual));
        String updatedUsername = username + ".1";
        ProfileResponse response = new ProfileResponse(updatedUsername, profileResponse.getBody().getPassword());
        ResponseEntity<ProfileResponse> updatedResponse = ResponseEntity.ok(response);
        when(trainerService.registerTrainer(trainerRequest)).thenReturn(updatedResponse);

        mockMvc.perform(post(base_url + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(updatedUsername))
                .andExpect(jsonPath("$.password").value(password));
        verify(trainerService, times(2)).registerTrainer(trainerRequest);
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TrainerProfileArgumentsProvider.class)
    void testGetProfile(String username, TrainerProfile expectedResponse) throws Exception {
        when(trainerService.getTrainerProfileByName(username)).thenReturn(ResponseEntity.ok(expectedResponse));
        String result = mockMvc.perform(get(base_url + "/{username}", username))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(objectMapper.readValue(result, TrainerProfile.class))
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
        verify(trainerService, times(1)).getTrainerProfileByName(username);
    }


    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(UpdateTrainerArgumentsProvider.class)
    void testUpdateTrainerProfiles(
            String username, TrainerUpdateRequest request,
            ResponseEntity<TrainerProfile> expectedProfile) throws Exception {
        when(trainerService.updateTrainer(username, request)).thenReturn(expectedProfile);
        String result = mockMvc.perform(put("/api/trainers/" + username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(objectMapper.readValue(result, TrainerProfile.class))
                .usingRecursiveComparison()
                .isEqualTo(expectedProfile.getBody());
        verify(trainerService, times(1)).updateTrainer(username, request);
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testGetNotAssignedActiveTrainersInvalidUsername() throws Exception {
        mockMvc.perform(get(base_url + "/{username}/unassigned", "user name")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

        verify(trainerService, times(0)).getNotAssigned("user name");
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerMissingRegistrationArgumentsProvider.class)
    void testTraineeRegistrationMissingRequiredFields(
            TrainerRequest traineeRequest, ResponseEntity<MessageResponse> expectedResponse) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/trainers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(traineeRequest)));
        resultActions.andExpect(status().is(expectedResponse.getStatusCode().value()));
        String content = resultActions.andReturn().getResponse().getContentAsString();
        String actualMessage = objectMapper.readValue(content, MessageResponse.class).getMessage();

        Arrays.stream(Objects.requireNonNull(expectedResponse.getBody()).getMessage().split(", "))
                .forEach(expectedMessage -> assertThat(actualMessage).contains(expectedMessage));
    }
}
