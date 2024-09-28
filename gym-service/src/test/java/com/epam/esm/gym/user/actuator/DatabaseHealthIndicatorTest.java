package com.epam.esm.gym.user.actuator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DatabaseHealthIndicator}.
 * This class contains test cases that validate the behavior of the
 * {@link DatabaseHealthIndicator} when checking the database health status.
 * It uses mocks to simulate database connectivity scenarios.
 */
@ExtendWith(MockitoExtension.class)
public class DatabaseHealthIndicatorTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @InjectMocks
    private DatabaseHealthIndicator healthIndicator;

    @Test
    void testHealthUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(2)).thenReturn(true);
        Health health = healthIndicator.health();
        assertEquals(Health.up().withDetail("database", "Running").build(), health);
    }

    @Test
    void testHealthDown() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(2)).thenReturn(false);
        Health health = healthIndicator.health();
        assertEquals(Health.down().withDetail("database", "Unavailable").build(), health);
    }

    @Test
    void testHealthException() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException("Database connection error"));
        Health health = healthIndicator.health();
        assertEquals(Health.down().withDetail("database", "Unavailable").build(), health);
    }
}
