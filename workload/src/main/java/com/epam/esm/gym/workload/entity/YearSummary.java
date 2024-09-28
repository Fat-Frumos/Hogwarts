package com.epam.esm.gym.workload.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the summary of training data for a specific year.
 * This class contains the year and a list of {@link MonthSummary} objects that detail
 * the trainer's performance on a monthly basis within that year.
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "year_summary")
public class YearSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "workload_id", nullable = false)
    private TrainerWorkload workload;

    @Builder.Default
    @OneToMany(mappedBy = "yearSummary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthSummary> months = new ArrayList<>();
}
