package com.epam.esm.gym.dto.trainee;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TraineeRequest {
    @NotNull(message = "FirstName is required")
    @Size(min = 1, max = 50, message = "Firstname must be between 1 and 50 characters")
    private String firstName;
    @NotNull(message = "LastName is required")
    @Size(min = 1, max = 50, message = "LastName must be between 1 and 50 characters")
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
