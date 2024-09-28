package com.epam.esm.gym.user.dto.trainer;

import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.provider.trainer.RegisterTrainerArgumentsProvider;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test class for TrainerRequest functionalities.
 */
class TrainerRequestTest {

   private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @ParameterizedTest
    @ArgumentsSource(RegisterTrainerArgumentsProvider.class)
    void testTrainerRequest(TrainerRequest request) {
        assertNotNull(request);
        assertEquals("Remus", request.getFirstName());
        assertEquals("Lupin", request.getLastName());
        assertEquals(LocalDate.parse("1960-03-10"), request.getDateOfBirth());
        assertEquals("Hogwarts", request.getAddress());
        assertEquals(Specialization.DEFENSE.name(), request.getSpecialization());
    }

    /**
     * Test for validating TrainerRequest with missing required fields.
     */
    @Test
    void testInvalidTrainerRequest() {
        TrainerRequest invalidRequest = TrainerRequest.builder()
                .firstName("")
                .specialization(null)
                .lastName("Lupin")
                .dateOfBirth(LocalDate.parse("1960-03-10"))
                .address("Hogwarts")
                .build();

        Set<ConstraintViolation<TrainerRequest>> violations = validator.validate(invalidRequest);

        assertFalse(violations.isEmpty());
        assertEquals(4, violations.size());
    }

    @Test
    void testEquals() {
        TrainerRequest request1 = TrainerRequest.builder()
                .firstName("Remus")
                .lastName("Lupin")
                .specialization(Specialization.DEFENSE.name())
                .dateOfBirth(LocalDate.parse("1960-03-10"))
                .address("Hogwarts")
                .build();

        TrainerRequest request2 = TrainerRequest.builder()
                .firstName("Remus")
                .lastName("Lupin")
                .specialization(Specialization.DEFENSE.name())
                .dateOfBirth(LocalDate.parse("1960-03-10"))
                .address("Hogwarts")
                .build();

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}
