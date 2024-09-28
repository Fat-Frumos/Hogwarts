package com.epam.esm.gym.user.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Logs details before and after method execution, including handling exceptions.
 * Ensures the transaction ID is cleared after method execution.
 */
@ExtendWith(MockitoExtension.class)
class TransactionLoggingAspectTest {

    private TransactionLoggingAspect transactionLoggingAspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @BeforeEach
    void setUp() {
        transactionLoggingAspect = new TransactionLoggingAspect();
    }

    @Test
    void testLogTransactionSuccess() throws Throwable {
        Object expectedResult = "result";
        when(joinPoint.proceed()).thenReturn(expectedResult);
        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.getTarget()).thenReturn(new Object());
        Object result = transactionLoggingAspect.logTransaction(joinPoint);
        assertEquals(expectedResult, result);
        verify(joinPoint).proceed();
    }

    @Test
    void testLogTransactionException() throws Throwable {
        Throwable exception = new RuntimeException("Error occurred");
        when(joinPoint.proceed()).thenThrow(exception);
        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.getTarget()).thenReturn(new Object());
        assertThrows(RuntimeException.class, () -> transactionLoggingAspect.logTransaction(joinPoint));
        verify(joinPoint).proceed();
    }

    @Test
    void testLogTransactionNullResult() throws Throwable {
        when(joinPoint.proceed()).thenReturn(null);
        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.getTarget()).thenReturn(new Object());

        Object result = transactionLoggingAspect.logTransaction(joinPoint);

        assertNull(result);
        verify(joinPoint).proceed();
    }
}
