package com.epam.esm.gym.workload;

import com.epam.esm.gym.jms.dto.TrainerStatus;
import com.epam.esm.gym.jms.dto.TrainerWorkloadResponse;
import com.epam.esm.gym.workload.service.WorkloadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the TrainingWorkload ControllerTest class.
 */
@WebMvcTest(controllers = TrainingWorkloadController.class)
class TrainingWorkloadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkloadService workloadService;

    private final String baseUrl = "/api/workload/summary/";

    @Test
    void shouldReturnTrainerSummaryWhenValidRequest() throws Exception {
        String username = "harry_potter";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        TrainerWorkloadResponse response = TrainerWorkloadResponse.builder()
                .username("harry_potter")
                .firstName("Harry")
                .lastName("Potter")
                .trainerStatus(TrainerStatus.ACTIVE)
                .summary(List.of(
                        new TrainerWorkloadResponse.YearSummaryResponse(2023, List.of(
                                new TrainerWorkloadResponse.YearSummaryResponse
                                        .MonthSummaryResponse(Month.JANUARY, 1200L)
                        ))
                ))
                .build();

        when(workloadService.getTrainerWorkloadByName(username, startDate, endDate))
                .thenReturn(response);

        mockMvc.perform(get(baseUrl + username)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("harry_potter"))
                .andExpect(jsonPath("$.firstName").value("Harry"))
                .andExpect(jsonPath("$.lastName").value("Potter"))
                .andExpect(jsonPath("$.trainerStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.summary[0].year").value(2023))
                .andExpect(jsonPath("$.summary[0].months[0].month").value("JANUARY"))
                .andExpect(jsonPath("$.summary[0].months[0].totalDuration").value(1200L));
    }

    @Test
    void shouldReturnTrainerSummaryWhenDatesAreMissing() throws Exception {
        String username = "hermione_granger";

        TrainerWorkloadResponse response = TrainerWorkloadResponse.builder()
                .username("hermione_granger")
                .firstName("Hermione")
                .lastName("Granger")
                .trainerStatus(TrainerStatus.ACTIVE)
                .summary(List.of(
                        new TrainerWorkloadResponse.YearSummaryResponse(2023, List.of(
                                new TrainerWorkloadResponse.YearSummaryResponse
                                        .MonthSummaryResponse(Month.MARCH, 1000L)
                        ))
                ))
                .build();

        when(workloadService.getTrainerWorkloadByName(username, null, null))
                .thenReturn(response);

        mockMvc.perform(get(baseUrl + username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("hermione_granger"))
                .andExpect(jsonPath("$.firstName").value("Hermione"))
                .andExpect(jsonPath("$.lastName").value("Granger"))
                .andExpect(jsonPath("$.trainerStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.summary[0].year").value(2023))
                .andExpect(jsonPath("$.summary[0].months[0].month").value("MARCH"))
                .andExpect(jsonPath("$.summary[0].months[0].totalDuration").value(1000L));
    }
}
