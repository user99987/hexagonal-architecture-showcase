package com.cp.ecommerce.adapter.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

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

    private static final String API_PATH_MATCHER = "/api/**";

    private static final String H2_PATH_MATCHER = "/h2-console/**";

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring().requestMatchers(H2_PATH_MATCHER);
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .requestMatchers(CONTEXT_PATH_MATCHER)
                .permitAll()
                .requestMatchers(API_PATH_MATCHER)
                .permitAll()
                .and()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable();

        return http.build();
    }

}
