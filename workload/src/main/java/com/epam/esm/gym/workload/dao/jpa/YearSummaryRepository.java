package com.epam.esm.gym.workload.dao.jpa;

import com.epam.esm.gym.workload.entity.YearSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing YearSummary entities.
 */
@Repository
public interface YearSummaryRepository extends JpaRepository<YearSummary, Long> {

    /**
     * Finds all YearSummary entries by workload ID.
     *
     * @param workloadId the ID of the TrainerWorkload
     * @return a list of YearSummary entities
     */
    List<YearSummary> findByWorkloadId(Long workloadId);
}
