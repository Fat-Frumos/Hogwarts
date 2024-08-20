package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.domain.Specialization;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "FirstName is required")
    @Size(min = 1, max = 50, message = "Firstname must be between 1 and 50 characters")
    private String firstName;
    @NotNull(message = "LastName is required")
    @Size(min = 1, max = 50, message = "LastName must be between 1 and 50 characters")
    private String lastName;
    private Date dateOfBirth;
    private String address;
    @NotNull(message = "Specialization is required")
    private Specialization specialization;
}
