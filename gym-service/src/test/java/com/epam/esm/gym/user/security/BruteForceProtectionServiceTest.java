package com.epam.esm.gym.user.security;

import com.epam.esm.gym.user.security.service.BruteForceProtectionService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BruteForceProtectionServiceTest {

    private final BruteForceProtectionService bruteForceProtectionService = new BruteForceProtectionService();
    private static final String username = "username";

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
    void testResetAttempts() {
        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        bruteForceProtectionService.registerFailedAttempt(username);
        assertTrue(bruteForceProtectionService.isLocked(username));
        bruteForceProtectionService.resetAttempts(username);
        assertFalse(bruteForceProtectionService.isLocked(username));
    }
}
