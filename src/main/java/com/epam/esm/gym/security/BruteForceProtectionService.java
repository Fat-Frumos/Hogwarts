package com.epam.esm.gym.security;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BruteForceProtectionService {

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 5 * 60 * 1000;

    private final Map<String, Integer> attemptsCache = new HashMap<>();
    private final Map<String, Long> lockTimeCache = new HashMap<>();

    public void registerFailedAttempt(String username) {
        int attempts = attemptsCache.getOrDefault(username, 0) + 1;
        attemptsCache.put(username, attempts);
        if (attempts >= MAX_ATTEMPTS) {
            lockTimeCache.put(username, System.currentTimeMillis());
        }
    }

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

    public void resetAttempts(String username) {
        attemptsCache.remove(username);
        lockTimeCache.remove(username);
    }
}
