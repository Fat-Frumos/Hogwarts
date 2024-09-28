package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.user.config.MockJdbcConfig;
import com.epam.esm.gym.user.service.TraineeService;
import com.epam.esm.gym.user.service.TrainerService;
import com.epam.esm.gym.user.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Base class for integration tests of Spring Boot controllers.
 *
 * <p>This abstract class sets up the test environment for controllers by configuring
 * {@link MockMvc} to perform HTTP requests and validate responses. It is intended to be
 * extended by specific controller test classes to leverage the pre-configured {@link MockMvc}
 * instance for end-to-end testing of controller endpoints.</p>
 *
 * <p>The class uses {@link SpringBootTest} to load the full application context and
 * {@link AutoConfigureMockMvc} to configure and inject a {@link MockMvc} instance. This allows
 * for testing of controllers in an environment that closely resembles production.</p>
 *
 * <p>It provides a common setup for controller tests, including loading the Spring context
 * and configuring mock HTTP requests and responses.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(MockJdbcConfig.class)
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
