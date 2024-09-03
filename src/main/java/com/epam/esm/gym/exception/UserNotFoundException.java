package com.epam.esm.gym.exception;

/**
 * Exception thrown when a user is not found in the system.
 *
 * <p>This exception is used to indicate that the specified user could not be located.
 * and is typically used in user-related operations where the user does not exist.</p>
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code UserNotFoundException} with the specified detail message.
     *
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
