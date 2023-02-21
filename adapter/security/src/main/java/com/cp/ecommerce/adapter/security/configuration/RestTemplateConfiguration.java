package com.cp.ecommerce.adapter.security.configuration;

import java.time.Duration;

import com.cp.ecommerce.adapter.security.utils.OutgoingHttpRequestInterceptor;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Class that creates the RestTemplate bean for the application context.
 */
@Component
class RestTemplateConfiguration {

    private static final Duration TIMEOUT = Duration.ofMillis(10000);

    @Bean
    public RestTemplate restTemplate(final OutgoingHttpRequestInterceptor outgoingHttpRequestInterceptor) {

        return new RestTemplateBuilder().setConnectTimeout(TIMEOUT)
                .setReadTimeout(TIMEOUT)
                .interceptors(outgoingHttpRequestInterceptor)
                .build();
    }
}
