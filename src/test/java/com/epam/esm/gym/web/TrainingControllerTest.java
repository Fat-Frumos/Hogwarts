package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.gym.domain.Specialization.DEFENSE;
import static com.epam.esm.gym.domain.Specialization.POTIONS;
import static com.epam.esm.gym.domain.Specialization.TRANSFIGURATION;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingControllerTest extends ControllerTest {

    private final String BASE_URL = "/api/trainings";

    public static Map<String, Object> training;
    public static List<TrainingTypeResponse> trainingTypes;

    @BeforeAll
    static void beforeAll() {
        trainingTypes = List.of(
                new TrainingTypeResponse(POTIONS, 1L),
                new TrainingTypeResponse(DEFENSE, 2L),
                new TrainingTypeResponse(TRANSFIGURATION, 3L)
        );

        training = new HashMap<>();
        training.put("traineeUsername", "Parry.Potter");
        training.put("trainerUsername", "Severus.Snape");
        training.put("trainingName", "Potions Mastery");
        training.put("trainingType", "Potions");
        training.put("trainingDate", "2024-08-01");
        training.put("trainingDuration", 2);
    }

    @Test
    void testAddTraining() throws Exception {
        mockMvc.perform(post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(training)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetTrainingTypes() throws Exception {
        when(trainingService.getTrainingTypes()).thenReturn(trainingTypes);

        String result = mockMvc.perform(get(BASE_URL + "/types"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(Arrays.asList(objectMapper.readValue(result, TrainingTypeResponse[].class)))
                .usingRecursiveComparison()
                .isEqualTo(trainingTypes);

        verify(trainingService, times(1)).getTrainingTypes();
    }
}