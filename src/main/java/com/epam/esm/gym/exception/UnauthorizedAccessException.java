package com.epam.esm.gym.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception thrown when a user attempts to access a resource they are not authorized to access.
 * Extends {@link AuthenticationException} to indicate authentication-related errors.
 * <p>
 * This exception is used to signal that the user does not have the necessary permissions
 * or their account is in a state that prevents access to the requested resource.
 * </p>
 */
public class UnauthorizedAccessException extends AuthenticationException {

    /**
     * Constructs a new {@code UnauthorizedAccessException} with the specified detail message.
     *
     * @param message the detail message which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
