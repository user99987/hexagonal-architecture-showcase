package com.cp.ecommerce.adapter.security.configuration;

import com.cp.ecommerce.adapter.security.utils.ManagementUserProperties;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

/**
 * Configuration class representing actuator's endpoints configuration.
 */
@Configuration
@ComponentScan("com.cp.ecommerce.adapter.security")
@Order(ActuatorSecurityConfiguration.BEFORE_DEFAULT_WEB_SECURITY_CONFIGURER_ADAPTER)
@RequiredArgsConstructor
public class ActuatorSecurityConfiguration {

    public static final int BEFORE_DEFAULT_WEB_SECURITY_CONFIGURER_ADAPTER = WebSecurityConfiguration.WEB_SECURITY_CONFIGURER_ADAPTER_ORDER
            - 1;

    private static final String ACTUATOR = "ACTUATOR";

    private final ManagementUserProperties properties;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .requestMatchers(
                        EndpointRequest.to(
                                HealthEndpoint.class,
                                InfoEndpoint.class,
                                MetricsEndpoint.class,
                                PrometheusScrapeEndpoint.class))
                .permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint())
                .hasRole(ACTUATOR)
                .and()
                .httpBasic();

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername(properties.getName()).password(properties.getPassword()).roles(ACTUATOR).build());
        return manager;
    }

}
