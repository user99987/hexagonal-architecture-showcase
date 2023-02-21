package com.cp.ecommerce.adapter.security.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Class that represents properties of management server's user.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "management.user")
@Component
public class ManagementUserProperties {

    private String name;

    private String password;

}
