package com.epam.esm.gym.user.actuator;

import javax.sql.DataSource;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Monitors the health of the database connection.
 *
 * <p>This class performs health checks on the database connection,
 * verifying connectivity and response times to ensure the database is
 * operational. It is used as part of the application's overall health monitoring
 * strategy.</p>
 *
 * @author Pavlo Poliak
 * @since 1.0
 */
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    /**
     * Constructs a new {@link DatabaseHealthIndicator} instance.
     *
     * <p>This constructor initializes the health indicator with the specified {@link DataSource} object.
     * The {@link DataSource} is used to perform database connectivity checks to determine the health of the database.
     * The constructor is typically called by Spring's dependency injection mechanism to provide the necessary
     * data source for health checks. It ensures that the health indicator has access to the database for monitoring
     * its status and performance.</p>
     *
     * @param dataSource the {@link DataSource} to be used for database health checks
     */
    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Provides the health status of the application's database.
     *
     * <p>This method overrides the {@link HealthIndicator#health()} method to report the
     * current state of the database connection. It checks the database status by invoking
     * the {@code checkDatabase()} method. If the database is available and responsive,
     * it returns a {@link Health#up()} status with a detail message indicating that the
     * database is running. Otherwise, it returns a {@link Health#down()} status with
     * a detail message indicating that the database is unavailable.</p>
     *
     * @return the health status of the database as {@link Health#up()} if the connection is valid,
     * otherwise {@link Health#down()}.
     * This method relies on {@code checkDatabase()} to determine the database's availability.
:     * @see #checkDatabase()
     * @see org.springframework.boot.actuate.health.Health
     * @see org.springframework.boot.actuate.health.HealthIndicator
     * @since 1.0
     */
    @Override
    public Health health() {
        boolean dbIsUp = checkDatabase();
        return dbIsUp ? Health.up().withDetail("database", "Running").build()
                : Health.down().withDetail("database", "Unavailable").build();
    }

    /**
     * Checks the availability and validity of the database connection.
     *
     * <p>This method attempts to obtain a connection from the configured data source and checks
     * if the connection is valid within the specified timeout of 2 seconds. If the connection is valid,
     * it returns {@code true}; otherwise, it returns {@code false}. In the event of a
     * {@link SQLException}, it catches the exception and returns {@code false}, indicating
     * that the database is not accessible.</p>
     *
     * @return {@code true} if the database connection is valid; {@code false} otherwise.
     * This method is used internally by {@code health()} to determine database status.
     * @see java.sql.Connection#isValid(int)
     * @since 1.0
     */
    private boolean checkDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
}
