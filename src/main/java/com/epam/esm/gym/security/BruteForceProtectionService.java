package com.epam.esm.gym.security;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for protecting against brute-force attacks by tracking failed login attempts and locking accounts.
 * <p>
 * This service maintains two caches: one for counting failed login attempts and another for storing lock times
 * when the maximum number of failed attempts is reached. It provides methods to register failed attempts, check
 * if an account is locked, and reset the failed attempt counts and lock status.
 * </p>
 */
@Service
public class BruteForceProtectionService {

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 5 * 60 * 1000;

    private final Map<String, Integer> attemptsCache = new HashMap<>();
    private final Map<String, Long> lockTimeCache = new HashMap<>();

    /**
     * Registers a failed login attempt for the specified username.
     * <p>
     * Increments the failed attempt count for the given username. If the number of failed attempts reaches the
     * maximum allowed attempts, the account is locked by recording the current time in the lock time cache.
     * </p>
     *
     * @param username the username for which the failed attempt is registered
     */
    public void registerFailedAttempt(String username) {
        int attempts = attemptsCache.getOrDefault(username, 0) + 1;
        attemptsCache.put(username, attempts);
        if (attempts >= MAX_ATTEMPTS) {
            lockTimeCache.put(username, System.currentTimeMillis());
        }
    }

    /**
     * Checks if the account for the given username is currently locked.
     * <p>
     * Determines if the account is locked by checking if the lock time is present and whether the lock time
     * duration has expired. If the lock time duration has expired, the account is unlocked by resetting the
     * attempts and lock time caches.
     * </p>
     *
     * @param username the username to check for lock status
     * @return {@code true} if the account is locked, {@code false} otherwise
     */
    public boolean isLocked(String username) {
        if (!lockTimeCache.containsKey(username)) {
            return false;
        }
        if (System.currentTimeMillis() - lockTimeCache.get(username) > LOCK_TIME_DURATION) {
            resetAttempts(username);
            return false;
        }
        return true;
    }

    /**
     * Resets the failed attempt count and lock status for the specified username.
     * <p>
     * Clears the failed attempts and lock time caches for the given username,
     * effectively unlocking the account and resetting the failed attempt count.
     * </p>
     *
     * @param username the username for which the attempts and lock status should be reset
     */
    public void resetAttempts(String username) {
        attemptsCache.remove(username);
        lockTimeCache.remove(username);
    }
}
