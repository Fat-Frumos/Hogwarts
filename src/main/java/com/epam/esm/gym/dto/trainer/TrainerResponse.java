package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.dto.profile.UserProfile;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
    private Long id;
    private UserProfile user;
    private Set<String> specializations;
}
