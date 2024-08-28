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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileUpdateTests {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("provideUpdatedTrainerProfiles")
    @DisplayName("Update Trainer Profile")
    void updateTrainerProfile(String username, String firstName, String lastName, String specialization, boolean isActive) throws Exception {
        mockMvc.perform(put("/api/trainers/update")
                        .param("username", username)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("specialization", specialization)
                        .param("isActive", String.valueOf(isActive)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.specialization").value(specialization))
                .andExpect(jsonPath("$.isActive").value(isActive));
    }

    @ParameterizedTest
    @MethodSource("provideUpdatedTraineeProfiles")
    @DisplayName("Update Trainee Profile")
    void updateTraineeProfile(String username, String firstName, String lastName, String address, boolean isActive) throws Exception {
        mockMvc.perform(put("/trainees/update")
                        .param("username", username)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("address", address)
                        .param("isActive", String.valueOf(isActive)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.isActive").value(isActive));
    }

    private static Stream<Arguments> provideUpdatedTrainerProfiles() {
        return Stream.of(
                Arguments.of("Harry.Potter", "Harry", "Potter", "Defense Against the Dark Arts", true),
                Arguments.of("Hermione.Granger", "Hermione", "Granger", "Transfiguration", false)
        );
    }

    private static Stream<Arguments> provideUpdatedTraineeProfiles() {
        return Stream.of(
                Arguments.of("Ron.Weasley", "Ron", "Weasley", "Gryffindor Common Room", true),
                Arguments.of("Ginny.Weasley", "Ginny", "Weasley", "Burrow", false)
        );
    }
}