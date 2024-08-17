package com.epam.esm.gym.dto;

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
public class TraineeDto {
    private Long id;
    private UserDto user;
    private Date dateOfBirth;
    private String address;
}
