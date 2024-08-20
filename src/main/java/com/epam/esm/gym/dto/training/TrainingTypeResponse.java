package com.epam.esm.gym.dto.training;

import com.epam.esm.gym.domain.Specialization;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeResponse {
    private Specialization trainingType;
    private Long trainingTypeId;
}
