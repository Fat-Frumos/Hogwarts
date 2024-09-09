package com.epam.esm.gym.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging method executions in the security layer.
 * Extends {@link LoggingAspect} to utilize common logging functionalities.
 */
@Aspect
@Component
public class SecurityLoggingAspect extends LoggingAspect {
    /**
     * Logs method execution details within the security layer.
     * This includes method entry, result, execution time, and exceptions.
     *
     * @param joinPoint the join point at which advice is applied
     * @return the result of the method execution
     * @throws Throwable if the method throws an exception
     */
    @Around("com.epam.esm.gym.service.aop.Pointcuts.securityLayer()")
    public Object logSecurityLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            logBeforeMethod(joinPoint);
            Object result = joinPoint.proceed();
            logAfterMethod(joinPoint, result);
            logExecutionTime(joinPoint, System.currentTimeMillis());
            return result;
        } catch (Throwable ex) {
            logAfterThrowing(joinPoint, ex);
            throw ex;
        } finally {
            clearTransactionId();
        }
    }
}