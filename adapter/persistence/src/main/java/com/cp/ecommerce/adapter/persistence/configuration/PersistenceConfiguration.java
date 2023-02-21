package com.cp.ecommerce.adapter.persistence.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Persistence configuration class.
 */
@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "com.cp.ecommerce.adapter.persistence")
@EntityScan(basePackages = "com.cp.ecommerce.adapter.persistence")
@ComponentScan(basePackages = "com.cp.ecommerce.adapter.persistence")
public class PersistenceConfiguration {

}
