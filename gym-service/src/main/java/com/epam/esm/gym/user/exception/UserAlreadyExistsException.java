package com.epam.esm.gym.user.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Exception thrown when a user already exists in the system.
 * This exception is used to indicate that an operation failed due to the presence of a user
 * with the same identifier or details already existing in the system.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserAlreadyExistsException extends RuntimeException {

    private final String message;
}
