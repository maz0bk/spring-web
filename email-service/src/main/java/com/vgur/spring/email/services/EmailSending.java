package com.vgur.spring.email.services;

import com.vgur.spring.api.core.OrderDto;
import com.vgur.spring.email.config.EmailProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(
        EmailProperties.class
)
public class EmailSending {
    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private ArrayList<OrderDto> ordersToSend;

    @PostConstruct
    public void init(){
        ordersToSend = new ArrayList<>();
    }

    @RabbitListener(queues = "EmailSending")
    public void listenEmailSendingQueue(OrderDto orderDto){
        ordersToSend.add(orderDto);
    }

    @Scheduled(fixedDelayString ="${spring.mail.sending_delay}" )
    private synchronized void sendEmails(){
        var copyOrders = (OrderDto[]) ordersToSend.toArray();
        ordersToSend.clear();
        for (OrderDto orderDto: copyOrders) {
            sendSimpleMessage(orderDto.getEmail(),"new order","You make new order");
        }

    }
    @Scheduled(fixedDelayString ="${spring.mail.check_quantity_delay}")
    private void checkQuantityEmails(){
        if (ordersToSend.size()>20) sendEmails();
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailProperties.getUsername());
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
