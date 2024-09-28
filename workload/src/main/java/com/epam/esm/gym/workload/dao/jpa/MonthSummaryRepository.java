package com.epam.esm.gym.workload.dao.jpa;

import com.epam.esm.gym.workload.entity.MonthSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing MonthSummary entities.
 */
@Repository
public interface MonthSummaryRepository extends JpaRepository<MonthSummary, Long> {

    /**
     * Finds all MonthSummary entries by year summary ID.
     *
     * @param yearSummaryId the ID of the YearSummary
     * @return a list of MonthSummary entities
     */
    List<MonthSummary> findByYearSummaryId(Long yearSummaryId);
}
