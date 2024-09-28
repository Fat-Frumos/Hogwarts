package com.epam.esm.gym.user.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Exception thrown when a user is not found in the system.
 *
 * <p>This exception is used to indicate that the specified user could not be located.
 * and is typically used in user-related operations where the user does not exist.</p>
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserNotFoundException extends RuntimeException {

    private final String message;
}
