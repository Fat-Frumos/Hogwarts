package com.epam.esm.gym.exception;

/**
 * Exception thrown when an invalid JWT authentication is encountered.
 *
 * <p>This exception is used to signal that the provided JWT token is invalid or has failed authentication checks.
 * It extends {@link RuntimeException} and is intended for scenarios where JWT validation fails.</p>
 */
public class InvalidJwtAuthenticationException extends RuntimeException {
    /**
     * Constructs a new {@code InvalidJwtAuthenticationException} with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }
}
