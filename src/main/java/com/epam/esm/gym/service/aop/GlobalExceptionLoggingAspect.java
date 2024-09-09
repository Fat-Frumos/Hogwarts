package com.epam.esm.gym.service.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging exceptions globally in different layers of the application.
 * Extends {@link LoggingAspect} to utilize common exception logging functionalities.
 */
@Aspect
@Component
public class GlobalExceptionLoggingAspect extends LoggingAspect {

    /**
     * Logs exceptions thrown in the DAO layer.
     * Uses {@link #logAfterThrowing(JoinPoint, Throwable)} method from {@link LoggingAspect}
     * to handle logging details including the transaction ID, method name, and error message.
     *
     * @param joinPoint the join point providing method details where the exception occurred
     * @param error     the thrown exception
     */
    @AfterThrowing(pointcut = "com.epam.esm.gym.service.aop.Pointcuts.daoLayer()", throwing = "error")
    public void logDaoException(JoinPoint joinPoint, Throwable error) {
        logAfterThrowing(joinPoint, error);
    }

    /**
     * Logs exceptions thrown in REST endpoints.
     * Uses {@link #logAfterThrowing(JoinPoint, Throwable)} method from {@link LoggingAspect}
     * to handle logging details including the transaction ID, method name, and error message.
     *
     * @param joinPoint the join point providing method details where the exception occurred
     * @param error     the thrown exception
     */
    @AfterThrowing(pointcut = "com.epam.esm.gym.service.aop.Pointcuts.restEndpoints()", throwing = "error")
    public void logRestEndpointException(JoinPoint joinPoint, Throwable error) {
        logAfterThrowing(joinPoint, error);
    }
}
