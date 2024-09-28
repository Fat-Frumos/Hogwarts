package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse;
import com.epam.esm.gym.user.dto.trainee.PostTraineeRequest;
import com.epam.esm.gym.user.dto.trainee.PutTraineeRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerResponse;
import com.epam.esm.gym.user.dto.trainer.TrainerResponseDto;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.provider.trainee.TraineeProfileArgumentsProvider;
import com.epam.esm.gym.user.provider.trainee.TraineeRegistrationArgumentsProvider;
import com.epam.esm.gym.user.security.service.BruteForceProtectionService;
import com.epam.esm.gym.user.security.service.JwtProvider;
import com.epam.esm.gym.user.security.service.SecurityUserDetailsService;
import com.epam.esm.gym.user.service.TraineeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for TraineeController.
 * This class is responsible for testing the REST API endpoints of the TraineeController
 */
@ActiveProfiles("test")
@WebMvcTest(TraineeController.class)
@Import(SecurityConfigTestMock.class)
class TraineeControllerTest {
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TraineeService service;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private BruteForceProtectionService bruteForceProtectionService;
    @MockBean
    private SecurityUserDetailsService userDetailsService;

    private static final String base_url = "/api/trainees";

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTrainees() throws Exception {
        List<FullTraineeProfileResponse> profiles = List.of(
                createProfileResponse("Harry Potter"),
                createProfileResponse("Hermione Granger")
        );
        when(service.findAll(any())).thenReturn(profiles);
        mockMvc.perform(get(base_url))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(profiles)));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(TraineeRegistrationArgumentsProvider.class)
    void registerTrainee(
            PostTraineeRequest request, ResponseEntity<ProfileResponse> expectedResponse) throws Exception {
        when(service.register(any())).thenReturn(expectedResponse.getBody());

        mockMvc.perform(post(base_url + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse.getBody())));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void getTraineeProfile(
            String username, FullTraineeProfileResponse expectedResponse,
            PutTraineeRequest request) throws Exception {
        when(service.getTraineeProfileByName(username)).thenReturn(expectedResponse);

        mockMvc.perform(get(base_url + "/" + username))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void testGetAllTraineesBatch(String username, FullTraineeProfileResponse expectedResponse) throws Exception {
        List<String> usernames = List.of("Harry.Potter", "Hermione.Granger");
        List<FullTraineeProfileResponse> expectedResponses = List.of(expectedResponse);
        when(service.findAll(usernames)).thenReturn(expectedResponses);

        mockMvc.perform(get(base_url + "/batch")
                        .param("usernames", usernames.toArray(new String[0])))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponses)));
        verify(service, times(1)).findAll(usernames);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateTraineeProfile() throws Exception {
        String username = "Luna Lovegood";
        PutTraineeRequest request = PutTraineeRequest.builder()
                .username(username)
                .firstName("Luna")
                .lastName("Lovegood")
                .active(true)
                .dateOfBirth(LocalDate.parse("1981-02-13"))
                .address("Hogwarts")
                .build();

        FullTraineeProfileResponse response = createProfileResponse(username);
        when(service.updateTrainee(username, request)).thenReturn(response);

        mockMvc.perform(put(base_url + "/" + username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTraineeProfile() throws Exception {
        String username = "Ginny Weasley";
        MessageResponse responseMessage = new MessageResponse(String.format(
                "Trainee with username '%s' has been successfully deleted.", username));
        when(service.deleteTrainee(username)).thenReturn(
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseMessage));
        mockMvc.perform(delete(base_url + "/" + username)).andExpect(status().isNoContent());
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @ArgumentsSource(TraineeProfileArgumentsProvider.class)
    void updateTraineeTrainers(String username, FullTraineeProfileResponse expectedResponse,
            PutTraineeRequest request) throws Exception {
        List<String> trainersUsernames = List.of("Severus Snape", "Minerva McGonagall");
        List<TrainerResponse> updatedTrainers = expectedResponse.trainers();
        when(service.updateTraineeTrainersByName(username, trainersUsernames)).thenReturn(updatedTrainers);
        mockMvc.perform(put(base_url + "/" + username + "/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainersUsernames)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedTrainers)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTraineeTrainings() throws Exception {
        String username = "Cedric Diggory";
        Map<String, String> params = new HashMap<>();
        List<TrainingResponse> trainings = List.of(new TrainingResponse(
                "Alastor Moody", "Defense Against the Dark Arts", "Lecture",
                120, LocalDate.of(2024, 9, 20)));

        when(service.getTraineeTrainingsByName(username, params)).thenReturn(trainings);

        mockMvc.perform(get(base_url + "/" + username + "/trainings")
                        .param("filter", "upcoming"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void activateDeactivateTrainee() throws Exception {
        String username = "Peeves";
        boolean active = false;

        MessageResponse response = new MessageResponse("Trainee deactivated");

        when(service.updateStatusProfile(username, active)).thenReturn(response);

        mockMvc.perform(patch(base_url + "/" + username + "/activate")
                        .param("active", String.valueOf(active)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getActiveTrainers() throws Exception {
        String username = "Harry Potter";
        List<TrainerResponseDto> activeTrainers = List.of(new TrainerResponseDto(
                "Lupin.Remus", "Lupin", "Remus", "Defense"));

        when(service.getActiveTrainersForTrainee(username)).thenReturn(activeTrainers);

        mockMvc.perform(get(base_url + "/" + username + "/active"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(activeTrainers)));
    }

    private FullTraineeProfileResponse createProfileResponse(String username) {
        return FullTraineeProfileResponse.builder()
                .username(username)
                .firstName("First")
                .lastName("Last")
                .dateOfBirth(LocalDate.parse("2000-01-01"))
                .address("Hogwarts")
                .active(true)
                .build();
    }
}
