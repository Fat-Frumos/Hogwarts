package com.epam.esm.gym.web;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingControllerTest extends ControllerTest {

    private Map<String, Object> addTraining;
    private final String BASE_URL = "/api/trainings";

    @BeforeEach
    void setUp() {

        addTraining = new HashMap<>();
        addTraining.put("traineeUsername", "harry.potter");
        addTraining.put("trainerUsername", "severus.snape");
        addTraining.put("trainingName", "Potions Mastery");
        addTraining.put("trainingDate", "2024-08-01");
        addTraining.put("trainingDuration", "2 hours");

    }

    @Test
    void testAddTraining() throws Exception {
        mockMvc.perform(post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addTraining)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTrainingTypes() throws Exception {
        mockMvc.perform(get(BASE_URL + "/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].trainingType").exists())
                .andExpect(jsonPath("$[0].trainingTypeName").value("Potions"))
                .andExpect(jsonPath("$[0].trainingTypeId").value(1))
                .andExpect(jsonPath("$[1].trainingTypeName").value("Defense Against the Dark Arts"))
                .andExpect(jsonPath("$[1].trainingTypeId").value(2))
                .andExpect(jsonPath("$[2].trainingTypeName").value("Transfiguration"))
                .andExpect(jsonPath("$[2].trainingTypeId").value(3));
    }
}
