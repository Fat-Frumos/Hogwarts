package com.epam.esm.gym.user.entity;

/**
 * Enumeration of different specializations in the training domain.
 *
 * <p>This enum defines various specializations that are relevant to training and coaching.
 * Each specialization represents a distinct area of expertise or focus.</p>
 */
public enum Specialization {
    DEFAULT,
    CARDIO,
    STRENGTH,
    FLEXIBILITY,
    BALANCE,
    COORDINATION,
    CARE,
    CHARMS,
    POTIONS,
    DEFENSE,
    QUIDDITCH,
    HERBOLOGY,
    DIVINATION,
    TRANSFIGURATION;

    /**
     * Converts a string representation of a specialization to the corresponding {@link Specialization} enum.
     * This method attempts to convert the provided string to an enum constant of {@link Specialization} by
     * matching the string with the enum constants. The comparison is case-insensitive.
     * If the provided string does not match any of the enum constants
     * or if the input is null, the method returns {@link Specialization#DEFAULT}.
     *
     * @param specialization the string representation of the specialization
     * @return the corresponding {@link Specialization} enum constant, or {@link Specialization#DEFAULT} if
     * the input is invalid or null
     */
    public static Specialization fromString(String specialization) {
        try {
            return Specialization.valueOf(specialization.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return Specialization.DEFAULT;
        }
    }
}
