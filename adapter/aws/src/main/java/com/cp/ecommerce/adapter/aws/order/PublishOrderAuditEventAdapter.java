package com.cp.ecommerce.adapter.aws.order;

import java.time.Instant;

import com.cp.ecommerce.adapter.aws.order.dto.OrderAuditEvent;
import com.cp.ecommerce.adapter.common.annotation.WebAdapter;
import com.cp.ecommerce.adapter.common.resilience.ResilientExecutor;
import com.cp.ecommerce.domain.order.Order;
import com.cp.ecommerce.domain.order.port.outgoing.PublishOrderAuditEventOutPort;
import com.google.gson.Gson;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

/**
 * Implementation of {@link PublishOrderAuditEventOutPort} that sends a JSON audit event to an SQS queue, wrapped behind a
 * circuit-breaker and retry.
 */
@Slf4j
@WebAdapter
@RequiredArgsConstructor
@ConditionalOnProperty(name = "service.aws.sqs.enabled", havingValue = "true")
public class PublishOrderAuditEventAdapter implements PublishOrderAuditEventOutPort {

    private static final String RESILIENCE_INSTANCE_NAME = "publishOrderAuditEvent";

    private static final String EVENT_TYPE = "ORDER_PLACED";

    private final transient SqsClient sqsClient;

    private final transient String queueUrl;

    private final transient ResilientExecutor resilientExecutor;

    @Override
    public void publish(final Order order) {

        final OrderAuditEvent event = OrderAuditEvent.builder()
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomer().getId())
                .eventType(EVENT_TYPE)
                .timestamp(Instant.now().toString())
                .build();
        final String json = new Gson().toJson(event);
        log.info("Publishing order audit event to SQS: queueUrl={}, orderNumber={}", queueUrl, order.getOrderNumber());
        resilientExecutor.runResilient(RESILIENCE_INSTANCE_NAME, () -> {
            final SendMessageRequest request = SendMessageRequest.builder().queueUrl(queueUrl).messageBody(json).build();
            sqsClient.sendMessage(request);
        });
    }

}
