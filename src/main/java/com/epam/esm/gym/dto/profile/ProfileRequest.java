package com.epam.esm.gym.dto.profile;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private String username;
    private String password;
    private String oldPassword;
    private String newPassword;
}
