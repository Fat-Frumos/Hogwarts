package com.epam.esm.gym.user.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the security logging aspect to verify that security-related
 * events are logged as expected, ensuring proper monitoring and auditing
 * of security activities in the application.
 */
@ExtendWith(MockitoExtension.class)
public class SecurityLoggingAspectTest {

    private SecurityLoggingAspect securityLoggingAspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @BeforeEach
    void setUp() {
        securityLoggingAspect = new SecurityLoggingAspect();
    }

    @Test
    void testLogSecurityLayerSuccess() throws Throwable {
        Object expectedResult = "result";
        when(joinPoint.proceed()).thenReturn(expectedResult);
        MethodSignature signature = mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getTarget()).thenReturn(new Object());

        Object result = securityLoggingAspect.logSecurityLayer(joinPoint);

        assertEquals(expectedResult, result);
        verify(joinPoint).proceed();
    }

    @Test
    void testLogSecurityLayerException() throws Throwable {
        Throwable exception = new RuntimeException("Error occurred");
        when(joinPoint.proceed()).thenThrow(exception);
        MethodSignature signature = mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getTarget()).thenReturn(new Object());

        Throwable thrown = assertThrows(RuntimeException.class, () ->
                securityLoggingAspect.logSecurityLayer(joinPoint));

        assertEquals("Error occurred", thrown.getMessage());
    }
}
