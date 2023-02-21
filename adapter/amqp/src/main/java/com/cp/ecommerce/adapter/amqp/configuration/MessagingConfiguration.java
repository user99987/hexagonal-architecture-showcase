package com.cp.ecommerce.adapter.amqp.configuration;

import com.cp.ecommerce.adapter.amqp.order.MessageListener;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for AMQP messaging.
 */
@Configuration
@EnableRabbit
@ConditionalOnProperty(name = "service.rabbitmq.enabled", havingValue = "true")
public class MessagingConfiguration {

    public static final String TOPIC_EXCHANGE_NAME = "com.cp.e.topic.order";

    public static final String QUEUE_NAME = "com.cp.q.order.v1";

    public static final String ROUTING_KEY = "order.v1";

    @Bean
    Queue queue() {

        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    TopicExchange exchange() {

        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    Binding binding(final Queue queue, final TopicExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    SimpleMessageListenerContainer container(
            final ConnectionFactory connectionFactory,
            final MessageListenerAdapter listenerAdapter) {

        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(final MessageListener listener) {

        return new MessageListenerAdapter(listener, "receiveMessage");
    }

}
