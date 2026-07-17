package com.cp.ecommerce.adapter.common.resilience;

import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

/**
 * Provides the default {@link CircuitBreakerRegistry} and {@link RetryRegistry} used to make outgoing adapter calls (e.g. AMQP,
 * SMTP) resilient to transient failures.
 *
 * <p>
 * The registries are built by hand instead of relying on resilience4j's Spring Boot autoconfiguration starter, keeping this
 * module free of Spring Boot autoconfigure coupling.
 * </p>
 */
@Configuration
public class ResilienceConfiguration {

    private static final int SLIDING_WINDOW_SIZE = 10;
    private static final float FAILURE_RATE_THRESHOLD = 50f;
    private static final Duration WAIT_DURATION_IN_OPEN_STATE = Duration.ofSeconds(10);
    private static final int PERMITTED_CALLS_IN_HALF_OPEN_STATE = 3;

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final Duration RETRY_WAIT_DURATION = Duration.ofMillis(500);

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {

        final CircuitBreakerConfig defaultConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(FAILURE_RATE_THRESHOLD)
                .slidingWindowSize(SLIDING_WINDOW_SIZE)
                .waitDurationInOpenState(WAIT_DURATION_IN_OPEN_STATE)
                .permittedNumberOfCallsInHalfOpenState(PERMITTED_CALLS_IN_HALF_OPEN_STATE)
                .build();
        return CircuitBreakerRegistry.of(defaultConfig);
    }

    @Bean
    public RetryRegistry retryRegistry() {

        final RetryConfig defaultConfig = RetryConfig.custom()
                .maxAttempts(MAX_RETRY_ATTEMPTS)
                .waitDuration(RETRY_WAIT_DURATION)
                .build();
        return RetryRegistry.of(defaultConfig);
    }

    /**
     * Binds circuit breaker and retry metrics to Micrometer only when a {@link MeterRegistry} bean is present in the context
     * (e.g. the assembled application), so that modules whose isolated tests don't expose metrics infrastructure aren't forced
     * to provide one.
     */
    @Bean
    @ConditionalOnBean(MeterRegistry.class)
    public MeterBinder resilience4jMeterBinder(
            final CircuitBreakerRegistry circuitBreakerRegistry,
            final RetryRegistry retryRegistry) {

        return meterRegistry -> {
            TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(circuitBreakerRegistry).bindTo(meterRegistry);
            TaggedRetryMetrics.ofRetryRegistry(retryRegistry).bindTo(meterRegistry);
        };
    }

}
