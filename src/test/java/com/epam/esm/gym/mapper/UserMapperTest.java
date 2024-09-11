package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.UserProfile;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private final UserMapper userProfileMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToDtoValidUser() {
        User user = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("Harry.Potter")
                .password("password123")
                .active(true)
                .permission(RoleType.ROLE_GUEST)
                .build();

        UserProfile dto = UserMapper.toDto(user);
        assertNotNull(dto);
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getPassword(), dto.getPassword());
        assertEquals(user.getActive(), dto.getActive());
    }

    @Test
    void testToDtoNullUser() {
        UserProfile dto = UserMapper.toDto(null);
        assertNull(dto);
    }

    @Test
    void testToUserValidInput() {
        String firstName = "Hermione";
        String lastName = "Granger";
        String password = "password";
        String username = "Hermione.Granger";
        RoleType role = RoleType.ROLE_ADMIN;
        User user = userProfileMapper.toUser(firstName, lastName, username, password, role);
        assertNotNull(user);
        assertEquals(firstName, user.getFirstName());
        assertEquals(password, user.getPassword());
        assertEquals(lastName, user.getLastName());
        assertEquals(username, user.getUsername());
        assertEquals(role, user.getPermission());
        assertFalse(user.getActive());
    }

    @Test
    void testToUserNullInput() {
        User user = userProfileMapper.toUser(null, null, null, null, null);
        assertNotNull(user);
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getUsername());
        assertNull(user.getPermission());
        assertFalse(user.getActive());
    }
}
