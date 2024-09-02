package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.dao.TrainingDao;
import com.epam.esm.gym.domain.Trainer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TrainingSessionService {

    private final TrainingDao trainingRepository;
    private final TrainerDao trainerRepository;

    public Map<String, Long> generateWeeklyReport() {
        LocalDate today = LocalDateTime.now().toLocalDate();
        LocalDateTime startOfWeekDateTime = today.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeekDateTime = today.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        return trainingRepository.findByStartTimeBetween(startOfWeekDateTime, endOfWeekDateTime)
                .stream()
                .collect(Collectors.groupingBy(session -> session.getTrainer().getUser().getUsername(),
                        Collectors.summingLong(session -> session.getDuration().toMinutes())));
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void sendWeeklyReports() {
        log.debug("Weekly report generated");
        Map<String, Long> weeklyReport = generateWeeklyReport();
        for (Map.Entry<String, Long> entry : weeklyReport.entrySet()) {
            String username = entry.getKey();
            Long totalMinutes = entry.getValue();
            Trainer trainer = trainerRepository.findByUsername(username).orElse(null);
            if (trainer != null) {
                String subject = "Weekly Training Report";
                String text = String.format("Dear %s,%n%nYour total training duration for the week is %d minutes.%n%nBest regards,%nTraining Team",
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

    private void sendEmail(String to, String subject, String text) {
        log.info("Starting weekly report email sending process: {} {} {}", to, subject, text);
    }
}
