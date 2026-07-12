package com.cp.ecommerce.adapter.security.configuration;

import java.net.http.HttpTimeoutException;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * Test class checking rest template's timeout behavior.
 */
@SpringBootTest
class RestTemplateTimeoutTest {

    private static final String TEST_URL = "http://10.255.255.255";

    @Autowired
    private transient RestTemplate restTemplate;

    @Test
    void shouldThrowTimeoutException() {

        // Spring Boot 4's RestTemplateBuilder defaults to the JDK HttpClient, which reports
        // connect timeouts as HttpTimeoutException rather than SocketException. Depending on the
        // network stack, the JDK HttpClient may additionally wrap a further root cause (e.g.
        // ConnectException) below the HttpTimeoutException, so assert on the immediate cause
        // rather than the ultimate root cause.
        assertThat(catchThrowable(() -> restTemplate.getForObject(TEST_URL, String.class)))
                .hasCauseInstanceOf(HttpTimeoutException.class);
    }
}
