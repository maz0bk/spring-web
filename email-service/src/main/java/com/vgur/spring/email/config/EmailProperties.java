package com.vgur.spring.email.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail")
@Data
public class EmailProperties {
    private String username;
    private Integer sendingDelay;
    private Integer checkQuantityDelay;
}
