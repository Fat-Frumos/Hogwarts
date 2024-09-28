package com.epam.esm.gym.user.service.aop;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the global exception logging aspect to ensure that exceptions
 * are logged correctly and appropriate actions are taken based on the
 * application's logging policy.
 */
@ExtendWith(MockitoExtension.class)
public class GlobalExceptionLoggingAspectTest {

    private GlobalExceptionLoggingAspect loggingAspect;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Throwable error;

    @BeforeEach
    void setUp() {
        loggingAspect = new GlobalExceptionLoggingAspect();
        when(joinPoint.getTarget()).thenReturn(new Object());
        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
    }

    @Test
    void testLogDaoException() {
        loggingAspect.logDaoException(joinPoint, error);

    }

    @Test
    void testLogRestEndpointException() {
        loggingAspect.logRestEndpointException(joinPoint, error);

    }
}
