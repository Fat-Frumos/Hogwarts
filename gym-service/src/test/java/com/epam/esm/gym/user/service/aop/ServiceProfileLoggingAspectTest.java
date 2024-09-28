package com.epam.esm.gym.user.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
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

@ExtendWith(MockitoExtension.class)
class ServiceProfileLoggingAspectTest {

    private ServiceProfileLoggingAspect loggingAspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @BeforeEach
    void setUp() {
        loggingAspect = new ServiceProfileLoggingAspect();
    }

    @Test
    void testLogServiceProfileSuccess() throws Throwable {
        Object expectedResult = "result";
        when(joinPoint.proceed()).thenReturn(expectedResult);
        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.getTarget()).thenReturn(new Object());
        Object result = loggingAspect.logServiceProfile(joinPoint);
        assertEquals(expectedResult, result);
        verify(joinPoint).proceed();
    }

    @Test
    void testLogServiceProfileException() throws Throwable {
        Throwable exception = new RuntimeException("Error occurred");
        when(joinPoint.proceed()).thenThrow(exception);
        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.getTarget()).thenReturn(new Object());
        assertThrows(RuntimeException.class, () -> loggingAspect.logServiceProfile(joinPoint));
        verify(joinPoint).proceed();
    }
}
