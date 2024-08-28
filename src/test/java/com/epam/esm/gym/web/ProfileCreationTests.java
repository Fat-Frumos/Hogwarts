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

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileCreationTests {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("provideValidTrainerProfiles")
    @DisplayName("Create Valid Trainer Profile")
    void createValidTrainerProfile(String firstName, String lastName, String specialization) throws Exception {
        mockMvc.perform(post("/api/trainers/register")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("specialization", specialization))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(firstName + "." + lastName))
                .andExpect(jsonPath("$.password").isString())
                .andExpect(jsonPath("$.password").value(matchesRegex("^.{10}$")));
    }

    @ParameterizedTest
    @MethodSource("provideValidTraineeProfiles")
    @DisplayName("Create Valid Trainee Profile")
    void createValidTraineeProfile(String firstName, String lastName, String address) throws Exception {
        mockMvc.perform(post("/api/trainees/register")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("address", address))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(firstName + "." + lastName))
                .andExpect(jsonPath("$.password").isString())
                .andExpect(jsonPath("$.password").value(matchesRegex("^.{10}$")));
    }

    @ParameterizedTest
    @MethodSource("provideDuplicateUsernames")
    @DisplayName("Handle Duplicate Username")
    void handleDuplicateUsername(String firstName, String lastName) throws Exception {
        mockMvc.perform(post("/api/trainers/register")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("specialization", "CARDIO"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/trainers/register")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("specialization", "BALANCE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(firstName + "." + lastName + "1")); // Suffix should be added
    }

    private static Stream<Arguments> provideValidTrainerProfiles() {
        return Stream.of(
                Arguments.of("Harry", "Potter", "Defense Against the Dark Arts"),
                Arguments.of("Hermione", "Granger", "Transfiguration")
        );
    }

    private static Stream<Arguments> provideValidTraineeProfiles() {
        return Stream.of(
                Arguments.of("Ron", "Weasley", "Gryffindor Common Room"),
                Arguments.of("Ginny", "Weasley", "Burrow")
        );
    }

    private static Stream<Arguments> provideDuplicateUsernames() {
        return Stream.of(
                Arguments.of("Harry", "Potter"),
                Arguments.of("Hermione", "Granger")
        );
    }
}
