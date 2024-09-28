package com.epam.esm.gym.jms.dto;

import lombok.Getter;

/**
 * Enum representing the type of action to be performed on a resource.
 * This enum defines two possible actions: ADD and DELETE. These values are used to specify
 * the action to be taken in various operations, such as modifying or removing resources.
 */
@Getter
public enum ActionType {

    /**
     * Represents the action of adding a new resource.
     */
    ADD("add.training.queue"),

    /**
     * Represents the action of deleting an existing resource.
     */
    DELETE("delete.training.queue");

    private final String queue;

    ActionType(String queue) {
        this.queue = queue;
    }

    /**
     * Validates if the given ActionType is supported.
     *
     * @param type the ActionType to validate
     * @throws UnsupportedOperationException if the ActionType is not valid
     */
    public static void fromString(ActionType type) {
        try {
            ActionType.valueOf(type.name());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException("Unsupported action type: " + type);
        }
    }
}
