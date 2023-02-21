package com.cp.ecommerce.adapter;

import com.cp.ecommerce.adapter.persistence.configuration.PersistenceConfiguration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Spring boot application needed for properly loading spring boot context in test configuration.
 */
@SpringBootApplication
@ComponentScan(
        basePackages = {
                "com.cp.ecommerce.adapter.web",
                "com.cp.ecommerce.adapter.mail",
                "com.cp.ecommerce.adapter.persistence",
                "com.cp.ecommerce.adapter.amqp",
                "com.cp.ecommerce.domain" })
@Import(PersistenceConfiguration.class)
public class SpringBootSecurityTestApplication {

}
