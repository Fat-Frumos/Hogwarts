package com.epam.esm.gym.security;

import com.epam.esm.gym.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPrincipalDetailsServiceTest {

    @Mock
    private UserDao userRepository;

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
