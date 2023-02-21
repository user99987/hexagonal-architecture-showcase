package com.cp.ecommerce.domain.order;

import java.util.Date;

import com.cp.ecommerce.adapter.common.annotation.DomainObject;

import lombok.Builder;
import lombok.Value;

/**
 * Representation of order message domain object.
 */
@Value
@Builder
@DomainObject
public class OrderMessage {

    public static final String SCHEMA_VERSION = "1.0";

    @Builder.Default
    String schemaVersion = SCHEMA_VERSION;

    Date created;

    Long customerId;

    String orderNumber;

}
