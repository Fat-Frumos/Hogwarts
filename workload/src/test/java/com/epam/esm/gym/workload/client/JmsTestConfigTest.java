package com.epam.esm.gym.workload.client;

import com.epam.esm.gym.jms.JmsTestConfig;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class JmsTestConfigTest {

    @Mock
    private ActiveMQConnectionFactory connectionFactory;

    @InjectMocks
    private JmsTestConfig jmsTestConfig;

    @Test
    void testConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = jmsTestConfig.connectionFactory();
        assertNotNull(connectionFactory);
        assertEquals("vm://localhost?broker.persistent=false", connectionFactory.getBrokerURL());
    }

    @Test
    void testJmsTemplate() {
        JmsTemplate jmsTemplate = jmsTestConfig.jmsTemplate(connectionFactory);
        assertNotNull(jmsTemplate);
        assertEquals(connectionFactory, jmsTemplate.getConnectionFactory());
        assertNotNull(jmsTemplate.getMessageConverter());
        assertInstanceOf(MappingJackson2MessageConverter.class, jmsTemplate.getMessageConverter());
    }

    @Test
    void testMessageConverter() {
        MessageConverter converter = jmsTestConfig.messageConverter();
        assertNotNull(converter);
    }

    @Test
    void testJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = jmsTestConfig.jmsListenerContainerFactory(connectionFactory);
        assertNotNull(factory);
    }

    @Test
    void testWarehouseFactory() {
        DefaultJmsListenerContainerFactory factory = jmsTestConfig.warehouseFactory(connectionFactory);
        assertNotNull(factory);
    }
}
