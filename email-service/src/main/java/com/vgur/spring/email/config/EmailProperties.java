package com.vgur.spring.email.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "spring.mail")
@Data
@Configuration
public class EmailProperties {
    private String username;
    private Integer sendingDelay;
    private Integer checkQuantityDelay;
}
