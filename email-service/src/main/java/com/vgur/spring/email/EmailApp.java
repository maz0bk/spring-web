package com.vgur.spring.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class EmailApp {
    public static void main(String[] args) {
        SpringApplication.run(EmailApp.class, args);
    }
}
