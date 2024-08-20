package com.epam.esm.gym.dto.training;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainingRequest {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private String trainingDate;
    private Integer trainingDuration;
}
