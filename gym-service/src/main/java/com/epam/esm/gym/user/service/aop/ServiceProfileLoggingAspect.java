package com.epam.esm.gym.user.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging service profile details.
 * Extends {@link LoggingAspect} to utilize common logging functionalities.
 * This aspect logs execution time and handles exceptions in the service layer.
 */
@Aspect
@Component
public class ServiceProfileLoggingAspect extends LoggingAspect {

    /**
     * Logs execution time and handles exceptions for methods in the service layer.
     *
     * @param joinPoint the join point providing method details
     * @return the result of the method execution
     * @throws Throwable if the method throws an exception
     */
    @Around("com.epam.esm.gym.user.service.aop.Pointcuts.serviceLayer()")
    public Object logServiceProfile(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            logExecutionTime(joinPoint, System.currentTimeMillis());
            return result;
        } catch (Throwable ex) {
            logAfterThrowing(joinPoint, ex);
            throw ex;
        }
    }
}
