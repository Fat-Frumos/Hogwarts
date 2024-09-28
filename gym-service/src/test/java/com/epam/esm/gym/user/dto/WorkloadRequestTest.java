package com.epam.esm.gym.user.dto;

import com.epam.esm.gym.jms.dto.WorkloadRequest;
import com.epam.esm.gym.user.provider.InvalidWorkloadRequestArgumentsProvider;
import com.epam.esm.gym.user.provider.ValidWorkloadRequestArgumentsProvider;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class WorkloadRequestTest {

    @ParameterizedTest
    @ArgumentsSource(ValidWorkloadRequestArgumentsProvider.class)
    void testValidWorkloadRequest(WorkloadRequest request) {
        assertNotNull(request.trainerUsername());
        assertNotNull(request.trainerFirstName());
        assertNotNull(request.trainerLastName());
        assertNotNull(request.status());
        assertNotNull(request.trainingDate());
        assertTrue(request.trainingDuration() > 0);
        assertNotNull(request.actionType());
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidWorkloadRequestArgumentsProvider.class)
    void testInvalidWorkloadRequest(WorkloadRequest request) {
        assertThrows(ConstraintViolationException.class, () -> validateWorkloadRequest(request));
    }

    @ParameterizedTest
    @ArgumentsSource(ValidWorkloadRequestArgumentsProvider.class)
    void testEqualsAndHashCode(WorkloadRequest request1) {
        WorkloadRequest request2 = new WorkloadRequest(
                request1.trainerUsername(),
                request1.trainerFirstName(),
                request1.trainerLastName(),
                request1.status(),
                request1.trainingDate(),
                request1.trainingDuration(),
                request1.actionType()
        );

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    private void validateWorkloadRequest(WorkloadRequest request) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<WorkloadRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
