package com.epam.esm.gym.dto.training;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainingResponse {
    private String trainerName;
    private String trainingName;
    private String trainingType;
    private int trainingDuration;
    private LocalDate trainingDate;
}
