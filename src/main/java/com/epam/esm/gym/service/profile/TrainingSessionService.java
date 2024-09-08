package com.epam.esm.gym.service.profile;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.dao.jpa.TrainingSessionRepository;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.service.SessionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link SessionService} that manages training sessions.
 * <p>
 * This class provides functionality for generating weekly reports on training sessions and scheduling the
 * dispatch of these reports. The implementation uses repositories to interact with the database and perform
 * necessary operations related to training sessions and trainers.
 * </p>
 */
@Slf4j
@Service
@AllArgsConstructor
public class TrainingSessionService implements SessionService {

    private final TrainingSessionRepository dao;
    private final TrainerDao trainerDao;

    /**
     * {@inheritDoc}
     * Generates a weekly report of training sessions.
     * <p>
     * This method collects data related to training sessions for the past week and compiles it into a report.
     * The resulting map includes relevant information, such as the number of sessions and other metrics.
     * </p>
     *
     * @return a map containing the weekly report data
     */
    @Override
    public Map<String, Long> generateWeeklyReport() {
        LocalDate today = LocalDateTime.now().toLocalDate();
        LocalDateTime startOfWeekDateTime = today.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeekDateTime = today.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        return dao.findByStartTimeBetween(startOfWeekDateTime, endOfWeekDateTime)
                .stream()
                .collect(Collectors.groupingBy(session -> session.getTrainer().getUser().getUsername(),
                        Collectors.summingLong(session -> session.getDuration().toMinutes())));
    }

    /**
     * {@inheritDoc}
     * Sends weekly training session reports.
     * <p>
     * This scheduled method runs every Monday at midnight and sends out the weekly training session reports.
     * The method is triggered by the scheduled task configuration and ensures timely delivery of reports.
     * </p>
     */
    @Override
    @Scheduled(cron = "0 0 0 * * MON")
    public void sendWeeklyReports() {
        log.debug("Weekly report generated");
        Map<String, Long> weeklyReport = generateWeeklyReport();
        for (Map.Entry<String, Long> entry : weeklyReport.entrySet()) {
            String username = entry.getKey();
            Long totalMinutes = entry.getValue();
            Trainer trainer = trainerDao.findByUsername(username).orElse(null);
            if (trainer != null) {
                String message = """
                        Dear %s,%n%nYour total training duration for the week is %d minutes.%n%n
                        Best regards,%nTraining Team"
                        """;
                String subject = "Weekly Training Report";
                String text = String.format(message,
                        trainer.getUser().getUsername(), totalMinutes);
                try {
                    sendEmail(trainer.getUser().getUsername() + "@i.ua", subject, text);
                    log.info("Report sent to trainer");
                } catch (Exception e) {
                    log.error("Failed to send email to trainer {}", e.getMessage());
                }
            }
        }
    }

    /**
     * Sends an email with the given details.
     * <p>
     * This method is responsible for initiating the process of sending an email to the specified recipient. It logs
     * the details of the email being sent, including the recipient address, subject, and email body text. Currently,
     * the method only logs the email details and does not perform actual email sending. This logging can be useful
     * for debugging and verifying email content before implementing the actual email dispatch logic.
     * </p>
     *
     * @param to      the recipient's email address
     * @param subject the subject of the email
     * @param text    the body text of the email
     */
    private void sendEmail(String to, String subject, String text) {
        log.info("Starting weekly report email sending process: {} {} {}", to, subject, text);
    }
}
