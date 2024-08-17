package com.epam.esm.gym.dto.trainee;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TraineeRequest {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
}
