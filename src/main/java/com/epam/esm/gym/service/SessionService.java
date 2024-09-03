package com.epam.esm.gym.service;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;

/**
 * Provides methods for generating and sending weekly session reports.
 * <p>
 * The `SessionService` interface defines operations for generating reports on session activities and sending
 * those reports on a weekly schedule. Implementations of this interface should handle the generation of reports
 * that summarize session activities and ensure that these reports are sent out at the specified schedule.
 * </p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public interface SessionService {

    /**
     * Generates a weekly report summarizing session activities.
     * <p>
     * This method collects and processes data related to sessions, such as usage statistics, user activity,
     * or other relevant metrics, and returns the results as a map where the keys represent the report categories
     * or metrics and the values represent the corresponding data points. The report is intended to provide an overview
     * of session activities for the past week.
     * </p>
     *
     * @return a map containing weekly report data, where keys are report categories and values are data points
     */
    Map<String, Long> generateWeeklyReport();

    /**
     * Sends weekly reports on a scheduled basis.
     * <p>
     * This method is executed automatically according to the specified cron expression ("0 0 0 * * MON"),
     * which corresponds to every Monday at midnight. It triggers the sending of weekly session reports, ensuring
     * that the generated reports are distributed to the intended recipients in a timely manner.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * MON")
    void sendWeeklyReports();
}
