package com.epam.esm.gym.user.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

/**
 * Configuration class for setting up an in-memory H2 database
 * and JPA components for testing purposes.
 *
 * <p>This configuration class is active only when the "test" profile is active.
 * It defines beans for the data source, entity manager factory, and transaction manager
 * necessary for running tests with an embedded database.</p>
 */
@Configuration
@Profile("test")
@EnableJpaRepositories(basePackages = "com.epam.esm.gym.user.dao")
@EntityScan(basePackages = "com.epam.esm.gym.user.domain")
public class MockJdbcConfig {

    /**
     * Configures a data source bean using an in-memory H2 database.
     *
     * <p>This method sets up an {@link DataSource} using {@link EmbeddedDatabaseBuilder}
     * to create an H2 database instance for use during tests.</p>
     *
     * @return a configured {@link DataSource} instance.
     */
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    /**
     * Configures the entity manager factory bean for JPA integration with the test database.
     *
     * <p>This method sets up a {@link LocalContainerEntityManagerFactoryBean} using the provided
     * {@link DataSource}. It specifies the package to scan for JPA entities and configures the
     * JPA vendor adapter as {@link HibernateJpaVendorAdapter}.</p>
     *
     * @param dataSource the configured {@link DataSource} for database connections.
     * @return a configured {@link LocalContainerEntityManagerFactoryBean} instance.
     */
    @Bean
    @Primary
    @Qualifier("mockEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.epam.esm.gym.user.domain");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return factoryBean;
    }

    /**
     * Configures the transaction manager bean for JPA transactions.
     *
     * <p>This method sets up a {@link JpaTransactionManager} using the provided
     * {@link EntityManagerFactory}. The transaction manager handles transaction management
     * in the application.</p>
     *
     * @param entityManagerFactory the configured {@link EntityManagerFactory} for managing persistence.
     * @return a configured {@link JpaTransactionManager} instance.
     */
    @Bean
    @Primary
    @Qualifier("mockTransactionManager")
    public JpaTransactionManager transactionManager(
            @Qualifier("mockEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
