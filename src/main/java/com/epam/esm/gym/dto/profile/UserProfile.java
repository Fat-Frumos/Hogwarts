package com.epam.esm.gym.dto.profile;

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
public class UserProfile {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean active;
}
