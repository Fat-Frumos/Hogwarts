package com.epam.esm.gym.user.service.aop;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Aspect for managing transactions.
 * This class intercepts methods annotated with {@link org.springframework.transaction.annotation.Transactional}
 * and manages the transaction lifecycle (commit/rollback).
 */
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class TransactionAspect extends LoggingAspect {

    private final PlatformTransactionManager transactionManager;

    /**
     * Manages the transaction for methods annotated with
     * {@link org.springframework.transaction.annotation.Transactional}.
     *
     * @param pjp the join point providing method details
     * @return the result of the method execution
     * @throws Throwable if the method throws an exception
     */
    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object manageTransaction(ProceedingJoinPoint pjp) throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            log.info("Current Transaction: " + TransactionSynchronizationManager.getCurrentTransactionName());
            Object result = pjp.proceed();
            transactionManager.commit(status);
            logAfterMethod(pjp, result);
            return result;
        } catch (Throwable ex) {
            transactionManager.rollback(status);
            logAfterThrowing(pjp, ex);
            throw ex;
        } finally {
            log.info("After Save Transaction: " + TransactionSynchronizationManager.getCurrentTransactionName());
        }
    }
}
