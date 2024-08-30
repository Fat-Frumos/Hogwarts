package com.epam.esm.gym.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRequest {
    @NotBlank(message = "FirstName is required")
    @Size(min = 1, max = 50, message = "Firstname must be between 1 and 50 characters")
    private String firstName;
    @NotBlank(message = "LastName is required")
    @Size(min = 1, max = 50, message = "LastName must be between 1 and 50 characters")
    @NotNull(message = "Specialization is required")
    private String specialization;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
