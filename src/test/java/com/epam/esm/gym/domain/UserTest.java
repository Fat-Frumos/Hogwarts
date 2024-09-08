package com.epam.esm.gym.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {

    @ParameterizedTest
    @MethodSource("provideEqualUsers")
    @DisplayName("Test equals method")
    void testEquals(User user1, User user2, boolean expected) {
        assertEquals(expected, user1.equals(user2));
    }

    @ParameterizedTest
    @MethodSource("provideEqualUsers")
    @DisplayName("Test hashCode method")
    void testHashCode(User user1, User user2, boolean expected) {
        if (expected) {
            assertEquals(user1.hashCode(), user2.hashCode());
        } else {
            assertNotEquals(user1.hashCode(), user2.hashCode());
        }
    }

    private static Stream<Arguments> provideEqualUsers() {
        User user1 = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("harry.potter")
                .password("password123")
                .active(true)
                .permission(RoleType.ROLE_TRAINEE)
                .build();

        User user2 = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("harry.potter")
                .password("password123")
                .active(true)
                .permission(RoleType.ROLE_TRAINEE)
                .build();

        User user3 = User.builder()
                .id(2)
                .firstName("Hermione")
                .lastName("Granger")
                .username("hermione.granger")
                .password("password456")
                .active(true)
                .permission(RoleType.ROLE_TRAINEE)
                .build();

        return Stream.of(
                Arguments.of(user1, user1, true),
                Arguments.of(user1, user2, true),
                Arguments.of(user1, user3, false)
        );
    }
}
