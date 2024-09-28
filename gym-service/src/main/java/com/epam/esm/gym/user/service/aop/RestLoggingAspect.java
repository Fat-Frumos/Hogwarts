package com.epam.esm.gym.user.service.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * Aspect for logging REST request details and execution times.
 * Extends {@link LoggingAspect} to utilize common logging functionalities.
 * This aspect logs request details before method execution,
 * execution time, method results, and exceptions in the controller layer.
 */
@Aspect
@Component
@AllArgsConstructor
public class RestLoggingAspect extends LoggingAspect {

    /**
     * Logs details of the incoming HTTP request before method execution.
     * Includes HTTP method, URL, method name, and arguments.
     *
     * @param joinPoint the join point providing method details
     */
    @Before("com.epam.esm.gym.user.service.aop.Pointcuts.controllerLayer()")
    public void logRequestBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        logBeforeMethod(joinPoint, request);
    }

    /**
     * Logs the execution time of methods in the controller layer.
     * Logs the time taken for the method execution.
     *
     * @param joinPoint the join point providing method details
     * @return the result of the method execution
     * @throws Throwable if the method throws an exception
     */
    @Around("com.epam.esm.gym.user.service.aop.Pointcuts.controllerLayer()")
    public Object logRequestExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            logExecutionTime(joinPoint, startTime);
            return result;
        } catch (Throwable ex) {
            logAfterThrowing(joinPoint, ex);
            throw ex;
        }
    }

    /**
     * Logs method results after successful execution.
     * Logs the transaction ID, method name, and result.
     *
     * @param joinPoint the join point providing method details
     * @param result    the result of the method execution
     */
    @AfterReturning(pointcut = "com.epam.esm.gym.user.service.aop.Pointcuts.controllerLayer()", returning = "result")
    public void logRequestAfterReturning(JoinPoint joinPoint, Object result) {
        logAfterMethod(joinPoint, result);
    }

    /**
     * Logs details when a method throws an exception.
     * Includes transaction ID, method name, and exception message.
     *
     * @param joinPoint the join point providing method details
     * @param error     the thrown exception
     */
    @AfterThrowing(pointcut = "com.epam.esm.gym.user.service.aop.Pointcuts.controllerLayer()", throwing = "error")
    public void logRequestAfterThrowing(JoinPoint joinPoint, Throwable error) {
        logAfterThrowing(joinPoint, error);
    }
}
