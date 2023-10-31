package com.vgur.spring.email.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {
    @Bean
    public Queue emailSendingQueue(){
        return new Queue("EmailSending");
    }
}
