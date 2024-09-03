package com.epam.esm.gym.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

/**
 * Configuration class for JDBC setup and JPA repositories.
 *
 * <p>This class configures the JDBC properties and sets up JPA repositories for
 * the application. It includes database connection settings and enables JPA
 * repository support within the specified base packages.</p>
 *
 * <p>JDBC configuration is essential for establishing database connections and
 * managing persistence layers in the application.</p>
 *
 * @author Pavlo Poliak
 * @see org.springframework.data.jpa.repository.config.EnableJpaRepositories
 * @since 1.0
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.epam.esm.gym.dao")
public class JdbcConfig {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dataSourceDriver;

    /**
     * Configures the data source bean for the application.
     *
     * <p>This method sets up the {@link DataSource} using {@link HikariConfig} to define the
     * database connection properties, such as the JDBC URL, username, password, and driver class name.
     * The configured {@link HikariDataSource} is then returned, providing connection pooling capabilities.</p>
     *
     * <p>The data source is a fundamental component for managing connections to the database,
     * allowing the application to execute SQL queries and manage transactions.</p>
     *
     * @return a configured {@link HikariDataSource} instance.
     * @author Pavlo Poliak
     * @see com.zaxxer.hikari.HikariConfig
     * @see com.zaxxer.hikari.HikariDataSource
     * @since 1.0
     */
    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dataSourceUrl);
        hikariConfig.setUsername(dataSourceUsername);
        hikariConfig.setPassword(dataSourcePassword);
        hikariConfig.setDriverClassName(dataSourceDriver);
        return new HikariDataSource(hikariConfig);
    }

    /**
     * Configures the EntityManagerFactory bean for JPA integration.
     *
     * <p>This method sets up a {@link LocalContainerEntityManagerFactoryBean} using the provided
     * {@link DataSource}. It specifies the package to scan for JPA entities and configures the
     * JPA vendor adapter as {@link HibernateJpaVendorAdapter}.</p>
     *
     * <p>The EntityManagerFactory is crucial for JPA operations, managing the persistence context
     * and handling interactions with the database through JPA entities.</p>
     *
     * @param dataSource the configured {@link DataSource} for database connections.
     * @return a configured {@link LocalContainerEntityManagerFactoryBean} instance.
     * @author Pavlo Poliak
     * @see org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
     * @see org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
     * @since 1.0
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.epam.esm.gym");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return factoryBean;
    }

    /**
     * Configures the SessionFactory bean for Hibernate integration.
     *
     * <p>This method sets up a {@link LocalSessionFactoryBean} using the provided {@link DataSource}.
     * It specifies the package to scan for Hibernate entities, enabling the management of the session
     * context within the application.</p>
     *
     * <p>The SessionFactory is a central component in Hibernate, managing the creation of sessions
     * and providing access to database operations through ORM.</p>
     *
     * @param dataSource the configured {@link DataSource} for database connections.
     * @return a configured {@link LocalSessionFactoryBean} instance.
     * @author Pavlo Poliak
     * @see org.springframework.orm.hibernate5.LocalSessionFactoryBean
     * @see javax.sql.DataSource
     * @since 1.0
     */
    @Bean
    @Primary
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.epam.esm.gym");
        return sessionFactory;
    }

    /**
     * Configures the TransactionManager bean for managing JPA transactions.
     *
     * <p>This method sets up a {@link JpaTransactionManager} using the provided
     * {@link EntityManagerFactory}. The transaction manager is responsible for managing
     * transaction boundaries in the application, ensuring consistency and handling commit
     * and rollback operations.</p>
     *
     * <p>The TransactionManager is essential for coordinating JPA transactions, managing
     * interactions between the persistence layer and the database.</p>
     *
     * @param entityManagerFactory the configured {@link EntityManagerFactory} for managing persistence.
     * @return a configured {@link JpaTransactionManager} instance.
     * @author Pavlo Poliak
     * @see org.springframework.orm.jpa.JpaTransactionManager
     * @see jakarta.persistence.EntityManagerFactory
     * @since 1.0
     */
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
