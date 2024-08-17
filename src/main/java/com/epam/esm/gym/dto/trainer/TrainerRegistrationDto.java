package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.dto.profile.UserDto;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRegistrationDto {
    private Long id;
    private UserDto user;
    private Date dateOfBirth;
    private String address;
}
