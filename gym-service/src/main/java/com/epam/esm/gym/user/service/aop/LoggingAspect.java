package com.epam.esm.gym.user.service.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.UUID;

/**
 * Abstract class providing common logging functionalities for aspects.
 * Includes methods for logging method entry, exit, exceptions, and execution time,
 * as well as handling a unique transaction ID for tracing.
 */
@Slf4j
public abstract class LoggingAspect {

    private static final ThreadLocal<String> transactionIdHolder = new ThreadLocal<>();

    /**
     * Sets a new transaction ID for the current thread.
     * This ID is used to trace the execution flow of a transaction.
     */
    private void setTransactionId() {
        transactionIdHolder.set(UUID.randomUUID().toString());
    }

    /**
     * Retrieves the current transaction ID.
     * If no transaction ID exists, a new one is generated.
     *
     * @return the current transaction ID
     */
    protected String getTransactionId() {
        if (transactionIdHolder.get() == null) {
            setTransactionId();
        }
        return transactionIdHolder.get();
    }

    /**
     * Clears the transaction ID for the current thread.
     * This method is typically called after method execution to clean up.
     */
    protected void clearTransactionId() {
        transactionIdHolder.remove();
    }

    /**
     * Logs method entry details, including the transaction ID, method name, and arguments.
     *
     * @param joinPoint the join point providing method details
     */
    protected void logBeforeMethod(JoinPoint joinPoint) {
        String transactionId = getTransactionId();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("{} | {} | Arguments: {} | ID: {} ",
                className, joinPoint.getSignature().getName(), joinPoint.getArgs(), transactionId);
    }

    /**
     * Logs details of the incoming HTTP request before method execution.
     * Includes HTTP method, URL, method name, and arguments.
     *
     * @param joinPoint the join point providing method details
     * @param request   the HTTP request
     */
    protected void logBeforeMethod(JoinPoint joinPoint, HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURI();
        log.info("HTTP: {} | URL: {} | Method: {} | Arguments: {} | ID: {}",
                method, url, joinPoint.getSignature().getName(), joinPoint.getArgs(), getTransactionId());
    }

    /**
     * Logs method exit details, including the transaction ID, method name, and result.
     *
     * @param joinPoint the join point providing method details
     * @param result    the result of the method execution
     */
    protected void logAfterMethod(JoinPoint joinPoint, Object result) {
        String transactionId = getTransactionId();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("{} | {} | Result: {} | ID: {} ",
                className, joinPoint.getSignature().getName(), result, transactionId);
    }

    /**
     * Logs exception details when a method throws an exception.
     * Includes the transaction ID, method name, and exception message.
     *
     * @param joinPoint the join point providing method details
     * @param error     the thrown exception
     */
    protected void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        String transactionId = getTransactionId();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.error("{} | {} | Error: {} | ID: {} ",
                className, joinPoint.getSignature().getName(), error.getMessage(), transactionId);
    }

    /**
     * Logs the execution time of a method.
     * Calculates the time taken since the start time and logs it.
     *
     * @param joinPoint the join point providing method details
     * @param startTime the time when the method execution started
     */
    protected void logExecutionTime(ProceedingJoinPoint joinPoint, long startTime) {
        String transactionId = getTransactionId();
        long timeTaken = System.currentTimeMillis() - startTime;
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("{} | {} | Execution Time: {} ms | ID: {}",
                className, joinPoint.getSignature().getName(), timeTaken, transactionId);
    }
}
