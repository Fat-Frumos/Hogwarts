package com.epam.esm.gym.user.domain;

import com.epam.esm.gym.user.dto.auth.UserPrincipal;
import com.epam.esm.gym.user.entity.RoleType;
import com.epam.esm.gym.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    void testToString() {
        User user = mock(User.class);
        when(user.toString()).thenReturn("User{username=user1}");

        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertEquals("UserPrincipal{user=User{username=user1}}", userPrincipal.toString());
    }

    @Test
    void testGetPassword() {
        User user = mock(User.class);
        when(user.getPassword()).thenReturn("password123");

        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertEquals("password123", userPrincipal.getPassword());
    }

    @Test
    void testGetUsername() {
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("user1");

        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertEquals("user1", userPrincipal.getUsername());
    }

    @Test
    void testEqualsSameObject() {
        User user = mock(User.class);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertTrue(userPrincipal.equals(userPrincipal));
    }

    @Test
    void testEqualsNullObject() {
        User user = mock(User.class);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertFalse(userPrincipal.equals(null));
    }

    @Test
    void testEqualsDifferentClass() {
        User user = mock(User.class);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        String differentClassObject = "String";
        assertFalse(userPrincipal.equals(differentClassObject));
    }

    @Test
    void testEqualsSameUser() {
        User user = mock(User.class);
        UserPrincipal userPrincipal1 = new UserPrincipal(user);
        UserPrincipal userPrincipal2 = new UserPrincipal(user);

        assertTrue(userPrincipal1.equals(userPrincipal2));
    }

    @Test
    void testIsAccountNonExpired() {
        User user = mock(User.class);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertTrue(userPrincipal.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        User user = mock(User.class);
        when(user.getActive()).thenReturn(true);

        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertTrue(userPrincipal.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        User user = mock(User.class);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertTrue(userPrincipal.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        User user = mock(User.class);
        when(user.getActive()).thenReturn(true);

        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertTrue(userPrincipal.isEnabled());
    }
}
