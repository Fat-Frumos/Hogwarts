package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

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

/**
 * Integration tests for the {@link TrainingController} class.
 *
 * <p>This class tests the endpoints of the {@link TrainingController} to ensure that they are working
 * correctly and returning the expected results. It uses {@link org.springframework.test.web.servlet.MockMvc}
 * to perform requests and assert responses, ensuring that the training-related API operations.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TrainingControllerTest extends ControllerTest {

    private static final String base_url = "/api/trainings";
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
    @WithMockUser(roles = "ADMIN")
    void testAddTraining() throws Exception {
        mockMvc.perform(post(base_url + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(training)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTrainingTypes() throws Exception {
        when(trainingService.getTrainingTypes()).thenReturn(trainingTypes);

        String result = mockMvc.perform(get(base_url + "/types"))
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
