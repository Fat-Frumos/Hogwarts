package com.epam.esm.gym.user.dto.profile;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {UserProfileTest class.
 */
class UserProfileTest {

    private final Validator validator;

    public UserProfileTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void testValidUserProfile() {
        UserProfile userProfile = UserProfile.builder()
                .firstName("Harry")
                .lastName("Potter")
                .build();

        assertThat(userProfile.firstName()).isEqualTo("Harry");
        assertThat(userProfile.lastName()).isEqualTo("Potter");

        var violations = validator.validate(userProfile);
        assertThat(violations).isEmpty();
    }

    @Test
    void testUserProfileGetters() {
        UserProfile userProfile = UserProfile.builder()
                .firstName("Harry")
                .lastName("Potter")
                .build();

        assertThat(userProfile.firstName()).isEqualTo("Harry");
        assertThat(userProfile.lastName()).isEqualTo("Potter");
    }

    @Test
    void testUserProfileWithNullValues() {
        UserProfile userProfile = UserProfile.builder()
                .firstName(null)
                .lastName(null)
                .build();

        assertThat(userProfile.firstName()).isNull();
        assertThat(userProfile.lastName()).isNull();
    }

    @Test
    void testUserProfileWithEmptyStrings() {
        UserProfile userProfile = UserProfile.builder()
                .firstName("")
                .lastName("")
                .build();

        assertThat(userProfile.firstName()).isEqualTo("");
        assertThat(userProfile.lastName()).isEqualTo("");
    }
}
