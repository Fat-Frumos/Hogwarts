package com.epam.esm.gym.security;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BruteForceProtectionServiceTest {

    private final BruteForceProtectionService bruteForceProtectionService = new BruteForceProtectionService();
    private static final String username = "username";
    private static final long LOCK_TIME_DURATION = 0;

    @Test
    void testRegisterFailedAttempt() {
        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        assertTrue(bruteForceProtectionService.isLocked(username));
    }

    @Test
    void testIsLockedNotLocked() {
        assertFalse(bruteForceProtectionService.isLocked(username));
    }

    @Test
    void testIsLockedAfterLockDuration() {
        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        try (ScheduledExecutorService executor = Executors.newScheduledThreadPool(1)) {
            executor.schedule(() -> assertTrue(bruteForceProtectionService.isLocked(username)),
                    LOCK_TIME_DURATION + 1000, TimeUnit.MILLISECONDS);
        }
        assertTrue(bruteForceProtectionService.isLocked(username));
    }

    @Test
    void testResetAttempts() {
        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        assertTrue(bruteForceProtectionService.isLocked(username));
        bruteForceProtectionService.resetAttempts(username);
        assertFalse(bruteForceProtectionService.isLocked(username));
    }
}
