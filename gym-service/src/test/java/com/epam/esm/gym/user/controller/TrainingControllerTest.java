package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.jms.dto.ActionType;
import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.jms.dto.TrainerStatus;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.jms.dto.WorkloadRequest;
import com.epam.esm.gym.user.dto.training.TrainingRequest;
import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.security.service.BruteForceProtectionService;
import com.epam.esm.gym.user.security.service.JwtProvider;
import com.epam.esm.gym.user.security.service.SecurityUserDetailsService;
import com.epam.esm.gym.user.service.TrainingService;
import com.epam.esm.gym.user.service.WorkloadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.UnsupportedByAuthenticationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(TrainingController.class)
@Import(SecurityConfigTestMock.class)
class TrainingControllerTest {

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
    private TrainingService trainingService;
    @MockBean
    private WorkloadService workloadService;

    private static final String baseUrl = "/api/trainings";

    @Test
    @WithMockUser(roles = "TRAINER")
    void testGetTrainingTypes() throws Exception {
        List<TrainingTypeDto> trainingTypes = List.of(
                TrainingTypeDto.builder().specialization(Specialization.TRANSFIGURATION).build(),
                TrainingTypeDto.builder().specialization(Specialization.DEFENSE).build()
        );

        when(trainingService.getTrainingTypes()).thenReturn(trainingTypes);

        mockMvc.perform(get(baseUrl + "/types")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(trainingTypes)));

        verify(trainingService, times(1)).getTrainingTypes();
    }

    @Test
    @WithMockUser(roles = "TRAINEE")
    void testGetTrainings() throws Exception {
        List<TrainingResponse> trainings = List.of(
                new TrainingResponse("Minerva McGonagall", "Advanced Transfiguration",
                        "TRANSFIGURATION", 60, LocalDate.of(2024, 1, 10))
        );

        when(trainingService.getAllTrainings()).thenReturn(trainings);

        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(trainings)));

        verify(trainingService, times(1)).getAllTrainings();
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void updateTrainingSession_shouldReturnOk() throws Exception {
        WorkloadRequest request = new WorkloadRequest(
                "Hermione.Granger",
                "Hermione",
                "Granger",
                TrainerStatus.ACTIVE,
                LocalDate.now().plusDays(1),
                60,
                ActionType.ADD

        );

        when(workloadService.updateWorkload(request)).thenReturn(new MessageResponse("Success"));

        mockMvc.perform(post(baseUrl + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void updateTrainingSessionShouldReturnOk() throws Exception {
        WorkloadRequest request = new WorkloadRequest(
                null, null, null, null, null, 0, null);
        when(workloadService.updateWorkload(request)).thenReturn(new MessageResponse("Success"));

        mockMvc.perform(post(baseUrl + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void addTraining_shouldReturnCreated() throws Exception {
        TrainingRequest request = TrainingRequest.builder()
                .traineeUsername("Harry.Potter")
                .trainerUsername("Hermione.Granger")
                .trainingName("Potions Class")
                .trainingDuration(60)
                .trainingDate(LocalDate.now().plusDays(1))
                .build();

        TrainingResponse trainingResponse = new TrainingResponse();
        when(trainingService.createTraining(request)).thenReturn(trainingResponse);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void addTrainingShouldReturnBadRequest() throws Exception {
        TrainingRequest request = TrainingRequest.builder().build();
        TrainingResponse trainingResponse = new TrainingResponse();
        when(trainingService.createTraining(request)).thenReturn(trainingResponse);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void addTraining_shouldReturnBadRequest_whenRequestInvalid() throws Exception {
        TrainingRequest request = new TrainingRequest();
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void deleteTraining_shouldReturnOk() throws Exception {
        String trainingName = "testTraining";
        when(trainingService.removeTraining(trainingName)).thenReturn(new MessageResponse("Deleted"));

        mockMvc.perform(delete(baseUrl + "/" + trainingName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }

    @Test
    @WithMockUser(roles = "TRAINER")
    void deleteTraining_shouldReturnNotFound_whenTrainingDoesNotExist() throws Exception {
        String trainingName = "nonExistentTraining";
        when(trainingService.removeTraining(trainingName))
                .thenThrow(new UserNotFoundException("Training not found"));
        mockMvc.perform(delete(baseUrl + "/" + trainingName))
                .andExpect(status().isNotFound());
    }

    @Test
    void addTraining_shouldReturnUnauthorizedWhenUserIsNotAuthorized() throws Exception {
        TrainingRequest request = new TrainingRequest();
        when(trainingService.createTraining(request))
                .thenThrow(new UnsupportedByAuthenticationException("Unauthorized"));

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
