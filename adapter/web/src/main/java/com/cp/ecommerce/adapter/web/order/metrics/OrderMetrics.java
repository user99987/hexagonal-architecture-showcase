package com.cp.ecommerce.adapter.web.order.metrics;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * Component recording business metrics related to order placement, exposed via Micrometer to the configured registries (e.g.
 * Prometheus).
 */
@Component
public class OrderMetrics {

    private static final String ORDERS_PLACED_METRIC_NAME = "orders.placed";

    private final transient Counter ordersPlacedCounter;

    public OrderMetrics(final MeterRegistry meterRegistry) {

        this.ordersPlacedCounter = Counter.builder(ORDERS_PLACED_METRIC_NAME)
                .description("Number of orders successfully placed")
                .register(meterRegistry);
    }

    public void recordOrderPlaced() {

        ordersPlacedCounter.increment();
    }

}
