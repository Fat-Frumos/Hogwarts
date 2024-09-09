package com.epam.esm.gym.service.aop;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Defines pointcuts for various layers of the application to facilitate logging and aspect-oriented programming.
 * Each pointcut matches method executions in specific packages or annotations.
 */
@Component
public class Pointcuts {

    /**
     * Pointcut for methods in the web layer.
     * Matches all methods within the {@code com.epam.esm.gym.web} package and its sub-packages.
     */
    @Pointcut("execution(* com.epam.esm.gym.web..*(..))")
    public void controllerLayer() {
    }

    /**
     * Pointcut for methods in the security service layer.
     * Matches all methods within the {@code com.epam.esm.gym.security.service} package and its sub-packages.
     */
    @Pointcut("execution(* com.epam.esm.gym.security.service..*(..))")
    public void securityLayer() {
    }

    /**
     * Pointcut for methods in the DAO (Data Access Object) layer.
     * Matches all methods within the {@code com.epam.esm.gym.dao} package and its sub-packages.
     */
    @Pointcut("execution(* com.epam.esm.gym.dao..*(..))")
    public void daoLayer() {
    }

    /**
     * Pointcut for methods in the service profile layer.
     * Matches all methods within the {@code com.epam.esm.gym.service.profile} package and its sub-packages.
     */
    @Pointcut("execution(* com.epam.esm.gym.service.profile..*(..))")
    public void serviceLayer() {
    }

    /**
     * Pointcut for REST endpoints.
     * Matches methods annotated with {@code @RequestMapping}.
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void restEndpoints() {
    }
}
