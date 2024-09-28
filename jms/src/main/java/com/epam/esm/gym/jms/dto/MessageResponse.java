package com.epam.esm.gym.jms.dto;

/**
 * Represents a response containing a message.
 *
 * <p>This class is used to encapsulate a single message to be returned in API responses,
 * typically for confirmation or error reporting.</p>
 */
public record MessageResponse(String message) {
}
