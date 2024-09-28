package com.epam.esm.gym.user.exception;

/**
 * Exception thrown when a token is not found in the system.
 *
 * <p>This exception indicates that the requested token could not be located.
 * Is used in scenarios where token retrieval fails.</p>
 */
public class TokenNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code TokenNotFoundException} with the specified detail message.
     *
     * @param message the detail message
     */
    public TokenNotFoundException(String message) {
        super(message);
    }
}
