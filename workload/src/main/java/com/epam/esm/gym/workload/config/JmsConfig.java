package com.epam.esm.gym.workload.config;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import static com.epam.esm.gym.jms.ActiveMQConfig.ERROR_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.TRAINERS_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.TRAINER_QUEUE;
import static com.epam.esm.gym.jms.ActiveMQConfig.USERNAME_QUEUE;

/**
 * JmsConfig sets up the configuration for JmsTemplate, including the connection factory,
 * message converter, and listener container factory.
 */
@Slf4j
@EnableJms
@Configuration
@EnableTransactionManagement
public class JmsConfig {

    /**
     * Creates the ActiveMQ connection factory with the specified credentials and URL.
     *
     * @return the ActiveMQConnectionFactory instance
     */
    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin", "admin", "tcp://localhost:61616");
        factory.setTrustAllPackages(true);
        factory.setTrustedPackages(new ArrayList(Arrays.asList(
                "com.epam.esm.gym.jms.dto",
                "com.epam.esm.gym")));
        return factory;
    }

    /**
     * Configures the message converter to convert messages to JSON format using Jackson.
     *
     * @return the MessageConverter instance
     */
    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    /**
     * Creates and configures the JmsTemplate with the custom message converter and connection factory.
     *
     * @param messageConverter the MessageConverter to be used
     * @return the JmsTemplate instance
     */
    @Bean
    public JmsTemplate jmsTemplate(MessageConverter messageConverter) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setMessageConverter(messageConverter);
        return jmsTemplate;
    }

    /**
     * Handles errors that occur during message processing.
     *
     * @param throwable the Throwable error that occurred
     */
    private void handleError(Throwable throwable) {
        log.error(throwable.getMessage());
    }

    /**
     * Creates a default JMS listener container factory.
     *
     * @return the DefaultJmsListenerContainerFactory instance
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(messageConverter());
        factory.setTransactionManager(jmsTransactionManager(connectionFactory()));
        factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
        factory.setErrorHandler(this::handleError);
        factory.setConcurrency("3-10");
        return factory;
    }

    /**
     * Creates a DefaultJmsListenerContainerFactoryConfigurer bean for customizing JMS listener containers.
     *
     * @return the DefaultJmsListenerContainerFactoryConfigurer instance
     */
    @Bean
    public DefaultJmsListenerContainerFactoryConfigurer jmsListenerContainerFactoryConfigurer() {
        return new DefaultJmsListenerContainerFactoryConfigurer();
    }

    /**
     * Creates a PlatformTransactionManager bean for factory.
     *
     * @return the PlatformTransactionManager instance
     */
    @Bean
    public PlatformTransactionManager jmsTransactionManager(ConnectionFactory connectionFactory) {
        return new JmsTransactionManager(connectionFactory);
    }

    /**
     * Creates a queue for sending list of trainer List.
     *
     * @return the queue for username requests
     */
    @Bean
    public Queue trainerListQueue() {
        return new ActiveMQQueue(TRAINERS_QUEUE);
    }

    /**
     * Creates a queue for sending username requests.
     *
     * @return the queue for username requests
     */
    @Bean
    public Queue requestQueue() {
        return new ActiveMQQueue(USERNAME_QUEUE);
    }

    /**
     * Creates a queue for receiving trainer profile responses.
     *
     * @return the queue for trainer profile responses
     */
    @Bean
    public Queue responseQueue() {
        return new ActiveMQQueue(TRAINER_QUEUE);
    }

    /**
     * Creates a dead letter queue for handling invalid messages.
     *
     * @return the dead letter queue for error handling
     */
    @Bean
    public Queue deadLetterQueue() {
        return new ActiveMQQueue(ERROR_QUEUE);
    }


    /**
     * Creates a RestTemplate bean.
     *
     * @return a new instance of RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
