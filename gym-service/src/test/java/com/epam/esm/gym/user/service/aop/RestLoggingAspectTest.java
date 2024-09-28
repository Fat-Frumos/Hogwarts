package com.epam.esm.gym.user.service.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestLoggingAspectTest {

    private RestLoggingAspect restLoggingAspect;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private HttpServletRequest request;

    @Mock
    private JoinPoint joinPoint;

    @BeforeEach
    void setUp() {
        restLoggingAspect = new RestLoggingAspect();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void testLogRequestExecutionTime() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenReturn("result");
        when(proceedingJoinPoint.getSignature()).thenReturn(mock(MethodSignature.class));
        when(proceedingJoinPoint.getTarget()).thenReturn(new Object());

        Object result = restLoggingAspect.logRequestExecutionTime(proceedingJoinPoint);

        assertEquals("result", result);
        verify(proceedingJoinPoint).proceed();
    }

    @Test
    void testLogRequestAfterReturning() {
        Object result = "success";
        when(proceedingJoinPoint.getSignature()).thenReturn(mock(MethodSignature.class));
        when(proceedingJoinPoint.getTarget()).thenReturn(new Object());
        restLoggingAspect.logRequestAfterReturning(proceedingJoinPoint, result);

        assertNotNull(result);
    }

    @Test
    void testLogRequestAfterThrowing() {
        Throwable error = new RuntimeException("Error occurred");
        when(proceedingJoinPoint.getSignature()).thenReturn(mock(MethodSignature.class));
        when(proceedingJoinPoint.getTarget()).thenReturn(new Object());
        restLoggingAspect.logRequestAfterThrowing(proceedingJoinPoint, error);
        assertNotNull(error);
    }

    @Test
    void testLogRequestBefore() {
        when(joinPoint.getSignature()).thenReturn(mock(MethodSignature.class));
        restLoggingAspect.logRequestBefore(joinPoint);
        verify(request, times(1)).getMethod();
        verify(request, times(1)).getRequestURI();
    }
}
