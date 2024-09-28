package com.epam.esm.gym.user.dto.profile;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the UserResponseTest class.
 */
class UserResponseTest {

    @Test
    void testGetters() {
        UserResponse userResponse = UserResponse.builder()
                .firstName("Hermione")
                .lastName("Granger")
                .username("hermione_granger")
                .password("securePassword")
                .active(true)
                .build();

        assertThat(userResponse.getFirstName()).isEqualTo("Hermione");
        assertThat(userResponse.getLastName()).isEqualTo("Granger");
        assertThat(userResponse.getUsername()).isEqualTo("hermione_granger");
        assertThat(userResponse.getPassword()).isEqualTo("securePassword");
        assertThat(userResponse.getActive()).isTrue();
    }

    @Test
    void testEqualsAndHashCode() {
        UserResponse user1 = UserResponse.builder()
                .firstName("Ron")
                .lastName("Weasley")
                .username("ron_weasley")
                .password("password123")
                .active(true)
                .build();

        UserResponse user2 = UserResponse.builder()
                .firstName("Ron")
                .lastName("Weasley")
                .username("ron_weasley")
                .password("password123")
                .active(true)
                .build();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());

        UserResponse user3 = UserResponse.builder()
                .firstName("Harry")
                .lastName("Potter")
                .username("harry_potter")
                .password("anotherPassword")
                .active(false)
                .build();

        assertThat(user1).isNotEqualTo(user3);
    }

    @Test
    void testBuilderPattern() {
        UserResponse userResponse = UserResponse.builder()
                .firstName("Draco")
                .lastName("Malfoy")
                .username("draco_malfoy")
                .password("slytherinPassword")
                .active(false)
                .build();

        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getFirstName()).isEqualTo("Draco");
        assertThat(userResponse.getLastName()).isEqualTo("Malfoy");
        assertThat(userResponse.getUsername()).isEqualTo("draco_malfoy");
        assertThat(userResponse.getPassword()).isEqualTo("slytherinPassword");
        assertThat(userResponse.getActive()).isFalse();
    }
}