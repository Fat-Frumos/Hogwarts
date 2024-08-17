package com.epam.esm.gym.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
}
