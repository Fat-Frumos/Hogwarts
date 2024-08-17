package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.dto.profile.UserProfile;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRequest {
    private UserProfile user;
    private Date dateOfBirth;
    private String address;
    private String specialization;
}
