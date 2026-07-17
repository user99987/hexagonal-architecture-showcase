package com.cp.ecommerce.adapter.web.order.metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class checking that {@link OrderMetrics} records the "orders.placed" counter correctly.
 */
class OrderMetricsTest {

    private transient MeterRegistry meterRegistry;

    private transient OrderMetrics orderMetrics;

    @BeforeEach
    void setUp() {

        meterRegistry = new SimpleMeterRegistry();
        orderMetrics = new OrderMetrics(meterRegistry);
    }

    @Test
    void shouldRegisterOrdersPlacedCounterWithZeroInitialValue() {

        assertThat(meterRegistry.get("orders.placed").counter().count()).isZero();
    }

    @Test
    void shouldIncrementOrdersPlacedCounterOnEachRecordedOrder() {

        orderMetrics.recordOrderPlaced();
        orderMetrics.recordOrderPlaced();

        assertThat(meterRegistry.get("orders.placed").counter().count()).isEqualTo(2);
    }

}
