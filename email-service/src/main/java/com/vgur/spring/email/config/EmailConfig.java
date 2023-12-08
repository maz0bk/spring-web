package com.vgur.spring.email.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class EmailConfig {
    @Bean
    public Queue emailSendingQueue(){
        return new Queue("EmailSending");
    }
}
