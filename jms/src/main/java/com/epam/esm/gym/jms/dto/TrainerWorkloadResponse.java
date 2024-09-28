package com.epam.esm.gym.jms.dto;

import java.time.Month;
import java.util.List;
import lombok.Builder;

/**
 * Represents the workload response for a trainer, including details such as the trainer's name, status,
 * and the summarized training data over the years.
 *
 * @param username      The unique username of the trainer.
 * @param firstName     The first name of the trainer.
 * @param lastName      The last name of the trainer.
 * @param trainerStatus The current status of the trainer (e.g., active, inactive).
 * @param summary       A list of year summaries representing the workload of the trainer, broken down by years.
 */
@Builder
public record TrainerWorkloadResponse(
        String username,
        String firstName,
        String lastName,
        TrainerStatus trainerStatus,
        List<YearSummaryResponse> summary) {
    /**
     * Represents the summary of a specific year including a list of months with their respective total durations.
     *
     * @param year   the year for which the summary is generated
     * @param months the list of {@link MonthSummaryResponse}
     *               representing the summary of each month in the year
     */
    public record YearSummaryResponse(
            long year,
            List<MonthSummaryResponse> months
    ) {
        /**
         * Represents the summary of a specific month including the total duration of activities within that month.
         *
         * @param month         the name of the month (e.g., "January", "February", etc.)
         * @param totalDuration the total duration of activities for the month
         */
        public record MonthSummaryResponse(
                Month month,
                long totalDuration
        ) {
        }
    }
}
