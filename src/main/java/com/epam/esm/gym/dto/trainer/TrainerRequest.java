package com.epam.esm.gym.dto.trainer;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRequest {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private String specialization;
}
