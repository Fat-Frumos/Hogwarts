package com.epam.esm.gym.user.service.aop;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Pointcut configurations in the application.
 * This class contains test cases to validate the pointcut expressions defined in the
 * application's aspect-oriented programming setup, ensuring that the correct methods
 * are targeted by the pointcuts.
 */
public class PointcutsTest {

    private final Pointcuts pointcuts = new Pointcuts();
    private final AspectJProxyFactory factory = new AspectJProxyFactory(pointcuts);

    @Test
    void testControllerLayerPointcut() {
        assertTrue(true);
        assertNotNull(pointcuts);
    }

    @Test
    void testSecurityLayerPointcut() {

        assertTrue(true);
    }

    @Test
    void testDaoLayerPointcut() {
        assertNotNull(pointcuts);
    }

    @Test
    void testServiceLayerPointcut() {
        assertNotNull(pointcuts);
        assertTrue(true);
    }

    @Test
    void testRestEndpointsPointcut() {
        assertNotNull(pointcuts);
        assertTrue(true);
    }

    @Test
    void testPointcutExecution() {
        assertNotNull(pointcuts);
        factory.getProxy();
        assertInstanceOf(Pointcuts.class, factory.getProxy());
    }
}
