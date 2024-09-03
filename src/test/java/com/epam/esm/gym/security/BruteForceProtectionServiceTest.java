package com.epam.esm.gym.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BruteForceProtectionServiceTest {

    private final BruteForceProtectionService bruteForceProtectionService = new BruteForceProtectionService();
    private static final String username = "username";
    private static final long LOCK_TIME_DURATION = 0;

    @Test
    void testRegisterFailedAttempt() {
        String username = "user1";

        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        assertTrue(bruteForceProtectionService.isLocked(username));
    }

    @Test
    void testIsLockedNotLocked() {
        String username = "user2";
        assertFalse(bruteForceProtectionService.isLocked(username));
    }

    @Test
    void testIsLockedAfterLockDuration() throws InterruptedException {

        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);

        Thread.sleep(LOCK_TIME_DURATION + 1000);

        assertTrue(bruteForceProtectionService.isLocked(username));
    }

    @Test
    void testResetAttempts() {
        String username = "user4";
        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        assertTrue(bruteForceProtectionService.isLocked(username));
        bruteForceProtectionService.resetAttempts(username);
        assertFalse(bruteForceProtectionService.isLocked(username));
    }
}
