package com.cp.ecommerce.adapter.aws.order.dto;

import lombok.Builder;
import lombok.Value;

/**
 * Adapter-local DTO representing a lightweight order audit event sent to SQS. This is intentionally kept separate from the
 * domain {@code Order} object.
 */
@Value
@Builder
public class OrderAuditEvent {

    String orderNumber;

    Long customerId;

    String eventType;

    /** ISO-8601 timestamp string, e.g. {@code 2024-01-01T00:00:00Z}. */
    String timestamp;

}
