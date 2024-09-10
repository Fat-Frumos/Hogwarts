package com.epam.esm.gym.web;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileCreationTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerService trainerService;

    @MockBean
    private TrainerDao trainerDao;

    @ParameterizedTest
    @MethodSource("provideValidTrainerProfiles")
    @DisplayName("Create Valid Trainer Profile")
    void createValidTrainerProfile(String firstName, String lastName, String specialization) throws Exception {
        when(trainerService.registerTrainer(any(TrainerRequest.class)))
                .thenReturn(ResponseEntity.ok(new ProfileResponse(firstName + "." + lastName, "Specialization123")));

        Trainer trainer = new Trainer(1L, new User(), new ArrayList<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(trainerDao.save(any(Trainer.class))).thenReturn(trainer);
        String requestBody = String.format(
                readPayloadJson("src/test/resources/validTraineeProfiles.json"),
                firstName, lastName, specialization);
        mockMvc.perform(post("/api/trainers/register").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(firstName + "." + lastName))
                .andExpect(jsonPath("$.password").isString());
    }

    @ParameterizedTest
    @MethodSource("provideDuplicateUsernames")
    @DisplayName("Handle Duplicate Username")
    void handleDuplicateUsernames(String firstName, String lastName, String specialization) throws Exception {
        when(trainerService.registerTrainer(any(TrainerRequest.class)))
                .thenAnswer(invocation -> {
                    TrainerRequest request = invocation.getArgument(0);
                    return request.getFirstName().equals(firstName) && request.getLastName().equals(lastName)
                            ? new ResponseEntity<>(HttpStatus.OK)
                            : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                });

        Trainer trainer = new Trainer(1L, new User(), new ArrayList<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(trainerDao.save(any(Trainer.class))).thenReturn(trainer);

        String requestBody = String.format(readPayloadJson("src/test/resources/validTraineeProfiles.json"),
                firstName, lastName, specialization);

        mockMvc.perform(post("/api/trainers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isOk());

        requestBody = String.format(readPayloadJson("src/test/resources/validTraineeProfiles.json"),
                firstName, lastName, specialization);

        mockMvc.perform(post("/api/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    private static Stream<Arguments> provideValidTrainerProfiles() {
        return Stream.of(
                Arguments.of("Harry", "Potter", "DEFENSE"),
                Arguments.of("Hermione", "Granger", "TRANSFIGURATION")
        );
    }

    private static Stream<Arguments> provideDuplicateUsernames() {
        return Stream.of(
                Arguments.of("Harry", "Potter", "CARDIO"),
                Arguments.of("Hermione", "Granger", "BALANCE")
        );
    }

    public static String readPayloadJson(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
