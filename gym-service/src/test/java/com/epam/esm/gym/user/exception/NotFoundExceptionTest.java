package com.epam.esm.gym.user.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    void testUserNotFoundException() {
        String message = "User not found";
        UserNotFoundException exception = new UserNotFoundException(message);
        assertEquals(message, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void testTokenNotFoundException() {
        String message = "Token not found";
        TokenNotFoundException exception = new TokenNotFoundException(message);
        assertEquals(message, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void testExceptionMessage() {
        String message = "InvalidJwtAuthenticationException";
        InvalidJwtAuthenticationException exception = new InvalidJwtAuthenticationException(message);
        assertEquals(message, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }
}