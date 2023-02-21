package com.cp.ecommerce.adapter.amqp.order;

import com.cp.ecommerce.adapter.amqp.order.mapper.OrderMessageMapper;
import com.cp.ecommerce.adapter.common.annotation.WebAdapter;
import com.cp.ecommerce.domain.order.Order;
import com.cp.ecommerce.domain.order.OrderMessage;
import com.cp.ecommerce.domain.order.port.outgoing.SendOrderMessageOutPort;
import com.google.gson.Gson;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.cp.ecommerce.adapter.amqp.configuration.MessagingConfiguration.ROUTING_KEY;
import static com.cp.ecommerce.adapter.amqp.configuration.MessagingConfiguration.TOPIC_EXCHANGE_NAME;

/**
 * Implementation of {@link SendOrderMessageOutPort} functionality.
 */
@Slf4j
@WebAdapter
@RequiredArgsConstructor
@ConditionalOnProperty(name = "service.rabbitmq.enabled", havingValue = "true")
public class SendOrderMessageAdapter implements SendOrderMessageOutPort {

    private final transient OrderMessageMapper mapper;

    private final transient RabbitTemplate rabbitTemplate;

    public void send(final Order order) {

        final OrderMessage orderMessage = mapper.mapToMessage(order).orElse(null);
        log.info("Message: {}", orderMessage);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, ROUTING_KEY, new Gson().toJson(orderMessage));
    }

}
