package com.epam.esm.gym.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging transaction details in the DAO layer.
 * Extends {@link LoggingAspect} to utilize common logging functionalities.
 * This aspect logs method entry, exit, and exceptions, and ensures transaction ID clearance.
 */
@Aspect
@Component
public class TransactionLoggingAspect extends LoggingAspect {

    /**
     * Logs details before and after method execution, including handling exceptions.
     * Ensures the transaction ID is cleared after method execution.
     *
     * @param joinPoint the join point providing method details
     * @return the result of the method execution
     * @throws Throwable if the method throws an exception
     */
    @Around("com.epam.esm.gym.service.aop.Pointcuts.daoLayer()")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            logBeforeMethod(joinPoint);
            Object result = joinPoint.proceed();
            logAfterMethod(joinPoint, result);
            return result;
        } catch (Throwable ex) {
            logAfterThrowing(joinPoint, ex);
            throw ex;
        } finally {
            clearTransactionId();
        }
    }
}