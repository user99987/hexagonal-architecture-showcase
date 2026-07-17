package com.cp.ecommerce.adapter.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Initial configuration class serving web security.
 */
@Configuration
@EnableWebSecurity
@Order(WebSecurityConfiguration.WEB_SECURITY_CONFIGURER_ADAPTER_ORDER)
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    public static final int WEB_SECURITY_CONFIGURER_ADAPTER_ORDER = 100;

    private static final String CONTEXT_PATH_MATCHER = "/home/**";

    private static final String ORDER_API_PATH_MATCHER = "/api/order/**";

    private static final String H2_PATH_MATCHER = "/h2-console/**";

    private static final String[] OPEN_API_PATH_MATCHERS = { "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**" };

    private static final String ORDER_READ_ROLE = "ORDER_READ";

    private static final String ORDER_WRITE_ROLE = "ORDER_WRITE";

    private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring().requestMatchers(H2_PATH_MATCHER);
    }

    @Bean
    public SecurityFilterChain webSecurityFilterChain(final HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                authorize -> authorize.requestMatchers(CONTEXT_PATH_MATCHER)
                        .permitAll()
                        .requestMatchers(OPEN_API_PATH_MATCHERS)
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, ORDER_API_PATH_MATCHER)
                        .hasRole(ORDER_READ_ROLE)
                        .requestMatchers(HttpMethod.POST, ORDER_API_PATH_MATCHER)
                        .hasRole(ORDER_WRITE_ROLE)
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)))
                .exceptionHandling(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
