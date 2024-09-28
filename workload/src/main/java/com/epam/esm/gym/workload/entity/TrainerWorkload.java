package com.epam.esm.gym.workload.entity;

import com.epam.esm.gym.jms.dto.ActionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the workload data for a trainer.
 * This class is used to map the trainer's workload information to the `trainer_workload` table in the database.
 * It contains details about the trainer, including their name, activity status,
 * training dates, duration, and the type of action performed.
 */
@Entity
@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "workload")
public class TrainerWorkload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trainerName;
    private boolean active;

    @Builder.Default
    @OneToMany(mappedBy = "workload", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TrainingWorkload> trainings = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ActionType actionType;

    @Builder.Default
    @OneToMany(mappedBy = "workload", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YearSummary> yearSummaries = new ArrayList<>();

    /**
     * Adds the specified number of hours to the total duration for a given month and year.
     * If the year or month does not exist in the year summaries, it creates a new {@link YearSummary}
     * or {@link MonthSummary} accordingly and then updates the total duration for that month.
     *
     * @param duration the number of hours to add to the total duration for the specified month and year
     * @param year  the year for which the hours are being added
     * @param monthName the month for which the hours are being added
     */
    public void addHours(long duration, Integer year, String monthName) {
        YearSummary yearSummary = yearSummaries.stream()
                .filter(ys -> Objects.equals(ys.getYear(), year))
                .findFirst()
                .orElseGet(() -> {
                    YearSummary newSummary = new YearSummary();
                    newSummary.setYear(year);
                    newSummary.setWorkload(this);
                    yearSummaries.add(newSummary);
                    return newSummary;
                });

        MonthSummary monthSummary = yearSummary.getMonths().stream()
                .filter(ms -> ms.getMonth().toString().equals(monthName))
                .findFirst()
                .orElseGet(() -> {
                    MonthSummary newMonth = new MonthSummary();
                    newMonth.setMonth(Month.valueOf(monthName));
                    newMonth.setYearSummary(yearSummary);
                    yearSummary.getMonths().add(newMonth);
                    return newMonth;
                });
        monthSummary.setTotalDuration(monthSummary.getTotalDuration() + duration);
    }

    /**
     * Removes the specified number of hours from the total duration for a given month and year.
     * If the resulting duration is less than zero, it will be set to zero.
     * The method checks for the existence of the year and month, and updates the total duration if they are found.
     *
     * @param duration the number of hours to remove from the total duration for the specified month and year
     * @param year  the year for which the hours are being removed
     * @param monthName the month for which the hours are being removed
     */

    public void removeHours(long duration, int year, String monthName) {
        yearSummaries.stream()
                .filter(ys -> ys.getYear() == year)
                .findFirst().flatMap(yearSummary -> yearSummary.getMonths().stream()
                        .filter(ms -> ms.getMonth().toString().equals(monthName))
                        .findFirst()).ifPresent(monthSummary -> monthSummary
                        .setTotalDuration(monthSummary.getTotalDuration() - duration));
    }
}
