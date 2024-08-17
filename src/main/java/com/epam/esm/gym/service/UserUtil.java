package com.epam.esm.gym.service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.Set;

public class UserUtil {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;
    private static final Random RANDOM = new SecureRandom();

    public static String generateUsername(String firstName, String lastName, Set<String> existingUsernames) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;
        while (existingUsernames.contains(username)) {
            username = baseUsername + "." + suffix++;
        }
        return username;
    }

    public static String generatePassword() {
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            sb.append(ALPHANUMERIC_STRING.charAt(RANDOM.nextInt(ALPHANUMERIC_STRING.length())));
        }
        return sb.toString();
    }
}
