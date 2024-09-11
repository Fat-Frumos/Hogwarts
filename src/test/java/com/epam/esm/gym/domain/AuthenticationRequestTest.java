package com.epam.esm.gym.domain;

import com.epam.esm.gym.dto.auth.AuthenticationRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link AuthenticationRequest} class.
 * This class contains tests for the  auth Request dto. Each test verifies the correctness
 * of Equals and HashCode between different instances and entities used in the application.
 */
public class AuthenticationRequestTest {

    @Test
    void testEqualsSameObject() {
        AuthenticationRequest request = new AuthenticationRequest("user", "pass");
        assertTrue(request.equals(request));
    }

    @Test
    void testEqualsNullObject() {
        AuthenticationRequest request = new AuthenticationRequest("user", "pass");
        assertFalse(request.equals(null));
    }

    @Test
    void testEqualsDifferentClass() {
        AuthenticationRequest request = new AuthenticationRequest("user", "pass");
        String differentClassObject = "String";
        assertFalse(request.equals(differentClassObject));
    }

    @Test
    void testEqualsDifferentUsername() {
        AuthenticationRequest request1 = new AuthenticationRequest("user1", "pass");
        AuthenticationRequest request2 = new AuthenticationRequest("user2", "pass");
        assertFalse(request1.equals(request2));
    }

    @Test
    void testEqualsDifferentPassword() {
        AuthenticationRequest request1 = new AuthenticationRequest("user", "pass1");
        AuthenticationRequest request2 = new AuthenticationRequest("user", "pass2");
        assertFalse(request1.equals(request2));
    }

    @Test
    void testEqualsSameUsernameAndPassword() {
        AuthenticationRequest request1 = new AuthenticationRequest("user", "pass");
        AuthenticationRequest request2 = new AuthenticationRequest("user", "pass");
        assertTrue(request1.equals(request2));
    }

    @Test
    void testHashCodeSameValues() {
        AuthenticationRequest request1 = new AuthenticationRequest("user", "pass");
        AuthenticationRequest request2 = new AuthenticationRequest("user", "pass");
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testHashCodeDifferentValues() {
        AuthenticationRequest request1 = new AuthenticationRequest("user1", "pass");
        AuthenticationRequest request2 = new AuthenticationRequest("user2", "pass");
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testHashCodeDifferentPassword() {
        AuthenticationRequest request1 = new AuthenticationRequest("user", "pass1");
        AuthenticationRequest request2 = new AuthenticationRequest("user", "pass2");
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testHashCodeNullValues() {
        AuthenticationRequest request1 = new AuthenticationRequest(null, null);
        AuthenticationRequest request2 = new AuthenticationRequest(null, null);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}
