package com.epam.esm.gym.workload.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Month;
import java.util.Objects;

/**
 * Represents the summary of training data for a specific month.
 * This class contains the month and the total duration of training in that month.
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "month_summary")
public class MonthSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "month_name")
    @Enumerated(EnumType.STRING)
    private Month month;

    private long totalDuration;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "year_summary_id", nullable = false)
    private YearSummary yearSummary;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MonthSummary that = (MonthSummary) obj;
        return totalDuration == that.totalDuration
                && Objects.equals(id, that.id)
                && Objects.equals(month, that.month)
                && Objects.equals(yearSummary, that.yearSummary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, month, totalDuration, yearSummary);
    }
}
