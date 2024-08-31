package com.epam.esm.gym.web;

import com.epam.esm.gym.service.TraineeService;
import com.epam.esm.gym.service.TrainerService;
import com.epam.esm.gym.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected TrainingService trainingService;
    @MockBean
    protected TrainerService trainerService;
    @MockBean
    protected TraineeService traineeService;
    protected final String password = "Password123";
}
