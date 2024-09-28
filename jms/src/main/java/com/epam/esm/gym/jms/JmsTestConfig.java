package com.epam.esm.gym.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

/**
 * Configuration class for setting up JMS components for testing purposes.
 */
@TestConfiguration
public class JmsTestConfig {

    /**
     * Creates and configures an ActiveMQConnectionFactory.
     *
     * @return an instance of ActiveMQConnectionFactory
     */
    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
    }

    /**
     * Creates and configures a JmsTemplate.
     *
     * @param connectionFactory the connection factory to be used by the JmsTemplate
     * @return an instance of JmsTemplate
     */
    @Bean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter());
        return jmsTemplate;
    }

    /**
     * Creates and configures a MessageConverter for JMS messages.
     *
     * @return an instance of MappingJackson2MessageConverter
     */
    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    /**
     * Creates and configures a DefaultJmsListenerContainerFactory for standard JMS listeners.
     *
     * @param connectionFactory the connection factory to be used by the listener container factory
     * @return an instance of DefaultJmsListenerContainerFactory
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    /**
     * Creates and configures a DefaultJmsListenerContainerFactory for warehouse-specific JMS listeners.
     *
     * @param connectionFactory the connection factory to be used by the warehouse listener container factory
     * @return an instance of DefaultJmsListenerContainerFactory configured for warehouse use
     */
    @Bean
    public DefaultJmsListenerContainerFactory warehouseFactory(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("2-5");
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}
