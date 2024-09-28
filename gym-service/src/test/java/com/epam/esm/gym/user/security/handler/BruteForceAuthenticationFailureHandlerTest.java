package com.epam.esm.gym.user.security.handler;

import com.epam.esm.gym.user.security.service.BruteForceProtectionService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BruteForceAuthenticationFailureHandlerTest {

    @InjectMocks
    private BruteForceAuthenticationFailureHandler handler;

    @Mock
    private BruteForceProtectionService protectionService;

    @Test
    void testOnAuthenticationFailure() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException exception = mock(AuthenticationException.class);
        request.setParameter("username", "testUser");
        handler.onAuthenticationFailure(request, response, exception);
        verify(protectionService).registerFailedAttempt("testUser");
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertTrue(Objects.requireNonNull(response.getErrorMessage()).contains("Authentication Failed"));
    }
}
