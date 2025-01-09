package com.cp.ecommerce.adapter.security.configuration;

import com.cp.ecommerce.adapter.security.utils.ManagementUserProperties;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

/**
 * Initial configuration class serving web security.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private static final String CONTEXT_PATH_MATCHER = "/home/**";

    private static final String API_PATH_MATCHER = "/api/**";

    private static final String H2_PATH_MATCHER = "/h2-console/**";

    private static final String ACTUATOR = "ACTUATOR";

    private final ManagementUserProperties properties;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring().requestMatchers(H2_PATH_MATCHER);
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(
                                        EndpointRequest.to(
                                                HealthEndpoint.class,
                                                InfoEndpoint.class,
                                                MetricsEndpoint.class,
                                                PrometheusScrapeEndpoint.class))
                                .permitAll()
                                .requestMatchers(CONTEXT_PATH_MATCHER)
                                .permitAll()
                                .requestMatchers(API_PATH_MATCHER)
                                .permitAll()
                                .requestMatchers(EndpointRequest.toAnyEndpoint())
                                .hasRole(ACTUATOR))
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername(properties.getName()).password(properties.getPassword()).roles(ACTUATOR).build());
        return manager;
    }

}
