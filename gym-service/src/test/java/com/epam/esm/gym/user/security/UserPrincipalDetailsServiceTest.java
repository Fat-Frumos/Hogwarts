package com.epam.esm.gym.user.security;

import com.epam.esm.gym.user.dao.JpaUserDao;
import com.epam.esm.gym.user.security.service.SecurityUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the UserPrincipalDetailsService class.
 * This class uses Mockito to mock dependencies and validate the behavior of
 * the UserPrincipalDetailsService methods.
 * Tests are focused on ensuring correct user details retrieval and authorization logic.
 */
@ExtendWith(MockitoExtension.class)
class UserPrincipalDetailsServiceTest {

    @Mock
    private JpaUserDao userRepository;

    @InjectMocks
    private SecurityUserDetailsService securityUserDetailsService;

    @Test
    void testLoadUserByUsernameUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                securityUserDetailsService.loadUserByUsername("nonexistent")
        );
    }
}
