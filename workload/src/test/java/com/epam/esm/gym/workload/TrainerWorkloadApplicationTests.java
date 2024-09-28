package com.epam.esm.gym.workload;

import com.epam.esm.gym.workload.service.TrainerReceiverService;
import com.epam.esm.gym.workload.service.TrainerWorkloadService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration test class for the {@link WorkloadApplication} application.
 * This class is responsible for loading the Spring application context and verifying
 * that it starts up correctly. It uses the {@link SpringBootTest} annotation to bootstrap
 * the full application context and {@link AutoConfigureMockMvc} for configuring
 * MockMvc to test the web layer. Custom properties are provided to configure the Spring
 * Cloud Config client and load application test properties from a YAML file.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {
        "spring.cloud.config.enabled=true",
        "spring.cloud.config.uri=http://localhost:8888"
})
class TrainerWorkloadApplicationTests {

    @MockBean
    private TrainerReceiverService receiver;

    @InjectMocks
    private TrainerWorkloadService trainerWorkloadService;

    @Test
    void contextLoads() {
    }
}
