package com.epam.esm.gym.jms.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents the response object for a trainer's details.
 * This record encapsulates the essential information about a trainer,
 * including their start and end duration.
 */
public record TrainerSummary(TrainerRequest profile, LocalDate startDate, LocalDate endDate) implements Serializable {
}
