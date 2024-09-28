package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.trainer.UpdateTrainerRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import com.epam.esm.gym.user.dto.trainer.TrainerRequest;
import com.epam.esm.gym.user.dto.training.TrainingProfile;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.provider.trainer.AssignTraineeToTrainerArgumentsProvider;
import com.epam.esm.gym.user.provider.trainer.DeleteTrainerArgumentsProvider;
import com.epam.esm.gym.user.provider.trainer.GetAllTrainersArgumentsProvider;
import com.epam.esm.gym.user.provider.trainer.GetNotAssignedActiveTrainersArgumentsProvider;
import com.epam.esm.gym.user.provider.trainer.GetTrainerProfileArgumentsProvider;
import com.epam.esm.gym.user.provider.trainer.NotAssignedActiveTrainersArgumentsProvider;
import com.epam.esm.gym.user.provider.trainer.RegisterTrainerArgumentsProvider;
import com.epam.esm.gym.user.provider.trainer.TrainerMissingRegistrationArgumentsProvider;
import com.epam.esm.gym.user.provider.trainer.TrainerProfileArgumentsProvider;
import com.epam.esm.gym.user.provider.trainer.TrainerTrainingsArgumentsProvider;
import com.epam.esm.gym.user.provider.trainer.UpdateStatusTrainerArgumentsProvider;
import com.epam.esm.gym.user.provider.trainer.UpdateTrainerArgumentsProvider;
import com.epam.esm.gym.user.security.service.BruteForceProtectionService;
import com.epam.esm.gym.user.security.service.JwtProvider;
import com.epam.esm.gym.user.security.service.SecurityUserDetailsService;
import com.epam.esm.gym.user.service.TrainerService;
import com.epam.esm.gym.user.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link TrainerController}.
 * This class contains tests for all the endpoints in the {@link TrainerController},
 * ensuring that each endpoint behaves as expected under various conditions.
 * The tests are run with the "test" profile and use a mock security configuration.
 */
@ActiveProfiles("test")
@WebMvcTest(TrainerController.class)
@Import(SecurityConfigTestMock.class)
class TrainerControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private BruteForceProtectionService bruteForceProtectionService;
    @MockBean
    private SecurityUserDetailsService userDetailsService;
    @MockBean
    private TrainerService trainerService;
    @MockBean
    private TrainingService trainingService;

    private static final String base_url = "/api/trainers";
    private String username;

    @BeforeEach
    void setUp() {
        username = "Severus.Snape";
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(GetAllTrainersArgumentsProvider.class)
    void getAllTrainers(List<TrainerProfile> expectedTrainers) throws Exception {
        when(trainerService.findAll()).thenReturn(expectedTrainers);
        mockMvc.perform(get(base_url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedTrainers)));

        verify(trainerService, times(1)).findAll();
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(RegisterTrainerArgumentsProvider.class)
    void registerTrainerProfile(TrainerRequest request, ProfileResponse expectedResponse) throws Exception {
        when(trainerService.registerTrainer(any(TrainerRequest.class))).thenReturn(expectedResponse);
        mockMvc.perform(post(base_url + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(trainerService, times(1)).registerTrainer(any(TrainerRequest.class));
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(AssignTraineeToTrainerArgumentsProvider.class)
    void assignTraineeToTrainer(String traineeUsername, String expectedMessage) throws Exception {
        mockMvc.perform(post(base_url + "/assign")
                        .param("traineeUsername", traineeUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));

        verify(trainerService, times(1)).assignTraineeToTrainer(traineeUsername);
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(DeleteTrainerArgumentsProvider.class)
    void deleteTrainer(String username, MessageResponse expectedResponse) throws Exception {
        when(trainerService.deleteTrainer(username)).thenReturn(expectedResponse);

        mockMvc.perform(delete(base_url + "/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(trainerService, times(1)).deleteTrainer(username);
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(GetTrainerProfileArgumentsProvider.class)
    void getTrainerProfile(String username, TrainerProfile expectedProfile) throws Exception {
        when(trainerService.getTrainerProfileByName(username)).thenReturn(expectedProfile);

        mockMvc.perform(get(base_url + "/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProfile)));

        verify(trainerService, times(1)).getTrainerProfileByName(username);
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(UpdateTrainerArgumentsProvider.class)
    void updateTrainerProfile(
            String username, UpdateTrainerRequest request, TrainerProfile expectedProfile) throws Exception {
        when(trainerService.updateTrainer(username, request)).thenReturn(expectedProfile);

        mockMvc.perform(put(base_url + "/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProfile)));

        verify(trainerService, times(1))
                .updateTrainer(eq(username), any(UpdateTrainerRequest.class));
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TrainerTrainingsArgumentsProvider.class)
    void getTrainerTrainings(
            String username, TrainingProfile trainingProfile,
            List<TrainingResponse> expectedTrainings) throws Exception {
        when(trainingService.getTrainerTrainingsByName(trainingProfile)).thenReturn(expectedTrainings);
        mockMvc.perform(get(base_url + "/{username}/trainings", username)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(UpdateStatusTrainerArgumentsProvider.class)
    void updateStatusTrainer(String username, Boolean active, MessageResponse expectedResponse) throws Exception {
        when(trainerService.updateStatusTrainerProfile(username, active)).thenReturn(expectedResponse);

        mockMvc.perform(patch(base_url + "/{username}", username)
                        .param("active", active.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
        verify(trainerService, times(1)).updateStatusTrainerProfile(username, active);
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(GetNotAssignedActiveTrainersArgumentsProvider.class)
    void getNotAssignedActiveTrainers(String username, List<TrainerProfile> expectedTrainers) throws Exception {
        when(trainerService.getNotAssigned(username)).thenReturn(expectedTrainers);

        mockMvc.perform(get(base_url + "/{username}/unassigned", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedTrainers)));

        verify(trainerService, times(1)).getNotAssigned(username);
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TrainerProfileArgumentsProvider.class)
    void testGetTrainerProfile(String username, TrainerProfile expectedResponse) throws Exception {
        when(trainerService.getTrainerProfileByName(username)).thenReturn(expectedResponse);
        ResultActions resultActions = mockMvc.perform(get(base_url + "/{username}", username)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TrainerTrainingsArgumentsProvider.class)
    void testGetTrainerTrainings(
            String username, TrainingProfile request,
            List<TrainingResponse> expectedResponse) throws Exception {

        when(trainingService.getTrainerTrainingsByName(request)).thenReturn(expectedResponse);

        ResultActions resultActions = mockMvc.perform(get(base_url + "/{username}/trainings", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testGetNotAssignedActiveTrainersInvalidUsername() throws Exception {
        mockMvc.perform(get(base_url + "/{username}/unassigned", "user name")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

        verify(trainerService, times(0)).getNotAssigned("user name");
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testAssignTraineeToTrainer() throws Exception {
        String traineeUsername = "Hermione.Granger";
        mockMvc.perform(MockMvcRequestBuilders.post(base_url + "/assign")
                        .param("traineeUsername", traineeUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Trainee assigned successfully"));
        verify(trainerService).assignTraineeToTrainer(traineeUsername);
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testGetAllTrainers() throws Exception {
        List<TrainerProfile> trainerProfiles = List.of(TrainerProfile.builder().username(username).build());
        when(trainerService.findAll()).thenReturn(trainerProfiles);
        mockMvc.perform(MockMvcRequestBuilders.get(base_url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(trainerProfiles)));

        verify(trainerService).findAll();
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testGetTrainerProfileWhenNotFound() throws Exception {
        when(trainerService.getTrainerProfileByName(username))
                .thenThrow(new EntityNotFoundException("Trainer not found"));

        mockMvc.perform(get(base_url + "/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(UpdateTrainerArgumentsProvider.class)
    void testUpdateTrainerProfile(
            String username, UpdateTrainerRequest request, TrainerProfile expectedResponse) throws Exception {
        when(trainerService.updateTrainer(username, request)).thenReturn(expectedResponse);

        mockMvc.perform(put(base_url + "/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ValueSource(booleans = {true, false})
    void testActivateDeactivateTrainer(boolean isActive) throws Exception {
        mockMvc.perform(patch(base_url + "/{username}", username)
                        .param("active", String.valueOf(isActive))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(trainerService, times(1)).updateStatusTrainerProfile(username, isActive);
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(NotAssignedActiveTrainersArgumentsProvider.class)
    void testGetNotAssignedActiveTrainersSuccess(
            String username, List<TrainerProfile> expectedResponse)throws Exception {
        when(trainerService.getNotAssigned(username)).thenReturn(expectedResponse);

        mockMvc.perform(get(base_url + "/{username}/unassigned", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(NotAssignedActiveTrainersArgumentsProvider.class)
    void testGetNotAssignedActiveTrainersUsernameNotFound(String username) throws Exception {
        when(trainerService.getNotAssigned(username)).thenThrow(new EntityNotFoundException("Username not found"));

        mockMvc.perform(get(base_url + "/{username}/unassigned", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TrainerProfileArgumentsProvider.class)
    void testGetProfile(String username, TrainerProfile expectedResponse) throws Exception {
        when(trainerService.getTrainerProfileByName(username)).thenReturn(expectedResponse);

        mockMvc.perform(get(base_url + "/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void testDeleteTrainer() throws Exception {
        mockMvc.perform(delete(base_url + "/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(trainerService, times(1)).deleteTrainer(username);
    }

    @ParameterizedTest
    @WithMockUser(roles = "TRAINER")
    @ArgumentsSource(TrainerMissingRegistrationArgumentsProvider.class)
    void testTraineeRegistrationMissingRequiredFields(
            TrainerRequest traineeRequest, MessageResponse expectedResponse) throws Exception {
        mockMvc.perform(post(base_url + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(expectedResponse.message())));
    }
}
