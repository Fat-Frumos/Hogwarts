package com.epam.esm.gym.dto.trainee;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TraineeRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
