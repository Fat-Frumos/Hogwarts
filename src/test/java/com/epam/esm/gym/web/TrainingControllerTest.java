package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import com.epam.esm.gym.web.data.TrainingData;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingControllerTest extends ControllerTest {

    private final String BASE_URL = "/api/trainings";

    @Test
    void testAddTraining() throws Exception {
        mockMvc.perform(post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TrainingData.training)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetTrainingTypes() throws Exception {
        List<TrainingTypeResponse> expectedTypes = TrainingData.trainingTypes;
        when(trainingService.getTrainingTypes()).thenReturn(expectedTypes);

        String result = mockMvc.perform(get(BASE_URL + "/types"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(Arrays.asList(objectMapper.readValue(result, TrainingTypeResponse[].class)))
                .usingRecursiveComparison()
                .isEqualTo(expectedTypes);

        verify(trainingService, times(1)).getTrainingTypes();
    }

}
