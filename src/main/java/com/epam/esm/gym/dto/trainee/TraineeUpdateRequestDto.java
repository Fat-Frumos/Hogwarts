package com.epam.esm.gym.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TraineeUpdateRequestDto extends TraineeRegistrationRequestDto {
    private Boolean isActive;
}
