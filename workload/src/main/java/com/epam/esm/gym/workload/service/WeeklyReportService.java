package com.epam.esm.gym.workload.service;

import com.epam.esm.gym.jms.dto.TrainerWorkloadResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service responsible for generating weekly reports based on trainers' workloads.
 * This service fetches trainer profiles from a queue, calculates their workloads for the week,
 * and generates the corresponding reports. It uses a scheduled task to execute this process
 * at a specific time each week.
 */
@Slf4j
@Service
@AllArgsConstructor
public class WeeklyReportService {

    private final TrainerReceiverService receiver;

    /**
     * Sends the weekly reports to all trainers.
     * This method is scheduled to run every Monday at midnight, generating weekly training reports for all trainers.
     * The start of the week is calculated as Monday and the end as Sunday. For each trainer, their
     * weekly workload is fetched using their username and the time range (from Monday to Sunday),
     * and then the report is generated and emailed using the {@link #generateWeeklyReport} method.
     */
    @Scheduled(cron = "1 0 0 * * MON")
    public void sendWeeklyReports() {
        receiver.findAllTrainers().forEach(this::generateWeeklyReport);
        log.debug("Weekly report generated");
    }

    /**
     * Generates a weekly report of training sessions.
     * This method collects data related to training sessions for the past week and compiles it into a report.
     * The resulting map includes relevant information, such as the number of sessions and other metrics.
     */
    public void generateWeeklyReport(TrainerWorkloadResponse workload) {
        for (TrainerWorkloadResponse.YearSummaryResponse yearEntry : workload.summary()) {
            long totalDuration = yearEntry.months().stream()
                    .mapToLong(TrainerWorkloadResponse.YearSummaryResponse.MonthSummaryResponse::totalDuration)
                    .sum();

            if (workload.username() != null) {
                String message = """
                        Dear %s,%n%nYour total training duration for the week is %d minutes.%n%n
                        Best regards,%nTraining Team
                        """;
                String subject = "Weekly Training Report";
                String text = String.format(message, workload.username(), totalDuration);
                try {
                    sendEmail(workload.username() + "@i.ua", subject, text);
                    log.info("Report sent to trainer");
                } catch (Exception e) {
                    log.error("Failed to send email to trainer {}", e.getMessage());
                }
            }
        }
    }

    /**
     * Sends an email with the given details.
     * This method is responsible for initiating the process of sending an email to the specified recipient. It logs
     * the details of the email being sent, including the recipient address, subject, and email body text. Currently,
     * the method only logs the email details and does not perform actual email sending. This logging can be useful
     * for debugging and verifying email content before implementing the actual email dispatch logic.
     *
     * @param to      the recipient's email address
     * @param subject the subject of the email
     * @param text    the body text of the email
     */
    protected void sendEmail(String to, String subject, String text) {
        log.info("Starting weekly report email sending process: {} {} {}", to, subject, text);
    }
}
