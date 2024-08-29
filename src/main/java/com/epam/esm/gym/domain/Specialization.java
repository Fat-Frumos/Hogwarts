package com.epam.esm.gym.domain;

public enum Specialization {
    CARDIO,
    BALANCE,
    STRENGTH,
    FLEXIBILITY,
    COORDINATION,
    CARE,
    POTIONS,
    DEFENSE,
    HERBOLOGY,
    DIVINATION,
    TRANSFIGURATION;

    public static Specialization fromString(String specialization) {
        try {
            return Specialization.valueOf(specialization.trim().toUpperCase());
        } catch (Exception e) {
            return FLEXIBILITY;
        }
    }
}
