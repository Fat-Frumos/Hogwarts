package com.epam.esm.gym.dto.training;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeResponse {
    private String trainingTypeName;
    private Long trainingTypeId;
}
