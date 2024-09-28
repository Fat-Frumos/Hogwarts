package com.epam.esm.gym.workload.service;

import com.epam.esm.gym.jms.dto.TrainerStatus;
import com.epam.esm.gym.jms.dto.TrainerWorkloadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the WeeklyReportService class, which handles the generation and sending
 * of weekly training reports for trainers.
 */
@ExtendWith(MockitoExtension.class)
public class WeeklyReportServiceTest {
    private TrainerReceiverService receiver;
    private WeeklyReportService weeklyReportService;
    private List<TrainerWorkloadResponse.YearSummaryResponse> yearSummaryResponses;

    @BeforeEach
    void setUp() {
        receiver = mock(TrainerReceiverService.class);
        weeklyReportService = spy(new WeeklyReportService(receiver));
        yearSummaryResponses = List.of(
                new TrainerWorkloadResponse.YearSummaryResponse(2024, List.of(
                        new TrainerWorkloadResponse.YearSummaryResponse.MonthSummaryResponse(Month.JANUARY, 60),
                        new TrainerWorkloadResponse.YearSummaryResponse.MonthSummaryResponse(Month.FEBRUARY, 30)
                ))
        );
    }

    @Test
    void generateWeeklyReportShouldNotSendEmailWhenUsernameIsNull() {
        List<TrainerWorkloadResponse.YearSummaryResponse> yearSummaryResponses = List.of(
                new TrainerWorkloadResponse.YearSummaryResponse(2024, Collections.emptyList()));
        TrainerWorkloadResponse response = new TrainerWorkloadResponse(
                null, "Null", "User", TrainerStatus.ACTIVE, yearSummaryResponses);
        weeklyReportService.generateWeeklyReport(response);
        verify(weeklyReportService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void sendEmailShouldLogCorrectMessage() {
        String to = "example@i.ua";
        String subject = "Test Subject";
        String text = "Test Body";
        doNothing().when(weeklyReportService).sendEmail(to, subject, text);
        assertDoesNotThrow(() -> weeklyReportService.sendEmail(to, subject, text));
    }

    @Test
    void sendWeeklyReportsShouldFetchTrainersAndGenerateReports() {
        TrainerWorkloadResponse workload = TrainerWorkloadResponse.builder()
                .username("Hermione.Granger")
                .firstName("Hermione")
                .lastName("Granger")
                .summary(yearSummaryResponses)
                .build();

        when(receiver.findAllTrainers()).thenReturn(List.of(workload));

        weeklyReportService.sendWeeklyReports();

        verify(receiver).findAllTrainers();
        verify(weeklyReportService).generateWeeklyReport(workload);
    }

    @Test
    void generateWeeklyReportShouldSendEmailWithCorrectDetails() {
        TrainerWorkloadResponse workload = TrainerWorkloadResponse.builder()
                .username("Hermione.Granger")
                .summary(yearSummaryResponses)
                .build();

        ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> textCaptor = ArgumentCaptor.forClass(String.class);

        weeklyReportService.generateWeeklyReport(workload);
        verify(weeklyReportService).sendEmail(toCaptor.capture(), subjectCaptor.capture(), textCaptor.capture());
        assertThat(toCaptor.getValue()).isEqualTo("Hermione.Granger@i.ua");
        assertThat(subjectCaptor.getValue()).isEqualTo("Weekly Training Report");
        assertThat(textCaptor.getValue()).contains("Dear Hermione.Granger,");
        assertThat(textCaptor.getValue()).contains("Your total training duration for the week is 90 minutes.");
    }


    @Test
    void generateWeeklyReportShouldNotSendEmailIfUsernameIsNull() {
        TrainerWorkloadResponse workload = TrainerWorkloadResponse.builder()
                .username(null).summary(yearSummaryResponses).build();
        weeklyReportService.generateWeeklyReport(workload);
        verify(weeklyReportService, never()).sendEmail(anyString(), anyString(), anyString());
    }
}
