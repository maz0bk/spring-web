package com.vgur.spring.email.services;

import com.vgur.spring.api.core.OrderDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSending {
    private final JavaMailSender mailSender;
    private ArrayList<OrderDto> ordersToSend;

    @PostConstruct
    public void init(){
        ordersToSend = new ArrayList<>();
    }

    @Bean
    public Queue emailSendingQueue(){
        return new Queue("EmailSending");
    }

    @RabbitListener(queues = "EmailSending")
    public void listen(OrderDto orderDto){
        ordersToSend.add(orderDto);
    }
    @Scheduled(fixedDelay = 60000)
    private synchronized void sendEmails(){
        var copyOrders = (OrderDto[]) ordersToSend.toArray();
        ordersToSend.clear();
        for (OrderDto orderDto: copyOrders) {
            sendSimpleMessage(orderDto.getEmail(),"new order","You make new order");
        }

    }
    @Scheduled(fixedDelay = 10000)
    private void checkQuantityEmails(){
        if (ordersToSend.size()>20) sendEmails();
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("eva@hubertm.ru");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
            log.info("Email has been successfully send!");
        } catch (Exception e){
            log.warn("Can't send email! "+ e);
        }

    }



}
