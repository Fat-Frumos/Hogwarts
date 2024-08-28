package com.epam.esm.gym.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrainingManagementTests {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("provideTrainingSessions")
    @DisplayName("Add Training Session")
    void addTrainingSession(String trainerUsername, String traineeUsername, String trainingType, int duration) throws Exception {
        mockMvc.perform(post("/api/trainings/add")
                        .param("trainerUsername", trainerUsername)
                        .param("traineeUsername", traineeUsername)
                        .param("trainingType", trainingType)
                        .param("duration", String.valueOf(duration)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainerUsername").value(trainerUsername))
                .andExpect(jsonPath("$.traineeUsername").value(traineeUsername))
                .andExpect(jsonPath("$.trainingType").value(trainingType))
                .andExpect(jsonPath("$.duration").value(duration));
    }

    @ParameterizedTest
    @MethodSource("provideTrainingSessionsFetchCriteria")
    @DisplayName("Fetch Training Sessions")
    void fetchTrainingSessions(String trainerUsername, String traineeUsername, String trainingType) throws Exception {
        mockMvc.perform(get("/api/trainings")
                        .param("trainerUsername", trainerUsername)
                        .param("traineeUsername", traineeUsername)
                        .param("trainingType", trainingType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(0)));
    }

    private static Stream<Arguments> provideTrainingSessions() {
        return Stream.of(
                Arguments.of("Harry.Potter", "Ron.Weasley", "Dueling", 60),
                Arguments.of("Hermione.Granger", "Ginny.Weasley", "Potions", 45)
        );
    }

    private static Stream<Arguments> provideTrainingSessionsFetchCriteria() {
        return Stream.of(
                Arguments.of("Harry.Potter", "Ron.Weasley", "Dueling"),
                Arguments.of("Hermione.Granger", "Ginny.Weasley", "Potions")
        );
    }
}
