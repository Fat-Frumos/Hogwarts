package com.epam.esm.gym.user.dto.profile;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test class for ProfileRequest functionalities.
 */
class ProfileRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateProfileRequestSuccessfully() {
        ProfileRequest profileRequest = ProfileRequest.builder()
                .username("harry.potter")
                .oldPassword("oldPassword123")
                .newPassword("newPassword123")
                .build();

        assertThat(profileRequest.getUsername()).isEqualTo("harry.potter");
        assertThat(profileRequest.getOldPassword()).isEqualTo("oldPassword123");
        assertThat(profileRequest.getNewPassword()).isEqualTo("newPassword123");
    }

    @Test
    void shouldThrowValidationErrorWhenUsernameIsNull() {
        ProfileRequest profileRequest = ProfileRequest.builder()
                .oldPassword("oldPassword123")
                .newPassword("newPassword123")
                .build();
        Set<ConstraintViolation<ProfileRequest>> violations = validator.validate(profileRequest);
        assertThat(violations).hasSize(1);
    }

    @Test
    void shouldThrowValidationErrorWhenOldPasswordIsNull() {
        ProfileRequest profileRequest = ProfileRequest.builder()
                .username("harry.potter")
                .newPassword("newPassword123")
                .build();

        Set<ConstraintViolation<ProfileRequest>> violations = validator.validate(profileRequest);

        assertThat(violations).hasSize(1);
    }

    @Test
    void shouldThrowValidationErrorWhenNewPasswordIsNull() {
        ProfileRequest profileRequest = ProfileRequest.builder()
                .username("harry.potter")
                .oldPassword("oldPassword123")
                .build();

        Set<ConstraintViolation<ProfileRequest>> violations = validator.validate(profileRequest);

        assertThat(violations).hasSize(1);
    }

    @Test
    void shouldReturnTrueForEqualObjects() {
        ProfileRequest request1 = ProfileRequest.builder()
                .username("harry.potter")
                .oldPassword("oldPassword123")
                .newPassword("newPassword123")
                .build();

        ProfileRequest request2 = ProfileRequest.builder()
                .username("harry.potter")
                .oldPassword("oldPassword123")
                .newPassword("newPassword123")
                .build();

        assertThat(request1).isEqualTo(request2);
    }

    @Test
    void shouldReturnFalseForNonEqualObjects() {
        ProfileRequest request1 = ProfileRequest.builder()
                .username("harry.potter")
                .oldPassword("oldPassword123")
                .newPassword("newPassword123")
                .build();

        ProfileRequest request2 = ProfileRequest.builder()
                .username("ron.weasley")
                .oldPassword("oldPassword123")
                .newPassword("newPassword123")
                .build();

        assertThat(request1).isNotEqualTo(request2);
    }

    @Test
    void shouldReturnCorrectHashCode() {
        ProfileRequest request = ProfileRequest.builder()
                .username("harry.potter")
                .oldPassword("oldPassword123")
                .newPassword("newPassword123")
                .build();

        int expectedHashCode = Objects.hash("harry.potter", "oldPassword123", "newPassword123");
        assertThat(request.hashCode()).isEqualTo(expectedHashCode);
    }
}
