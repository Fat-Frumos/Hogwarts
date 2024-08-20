package com.epam.esm.gym.dto.trainee;

import com.epam.esm.gym.dto.trainer.TrainerProfile;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TraineeProfile  {
    private String firstName;
    private String lastName;
    private String username;
    private String address;
    private Boolean active;
    private LocalDate dateOfBirth;
    private List<TrainerProfile> trainers;
}
