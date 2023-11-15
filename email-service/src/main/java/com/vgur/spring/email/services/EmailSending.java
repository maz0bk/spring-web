package com.vgur.spring.email.services;

import com.vgur.spring.api.core.OrderDto;
import com.vgur.spring.email.config.EmailProperties;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSending {
    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private ArrayList<OrderDto> ordersToSend = new ArrayList<>();

    @RabbitListener(queues = "EmailSending")
    public void listenEmailSendingQueue(OrderDto orderDto) {
        ordersToSend.add(orderDto);
    }

    @Scheduled(fixedDelayString = "${spring.mail.sending_delay}")
    private void sendEmails() {
        var copyOrders = (OrderDto[]) ordersToSend.toArray();
        ordersToSend.clear();

        int threadQuantity = 4;
        ExecutorService executor = Executors.newFixedThreadPool(threadQuantity);
        ArrayList<Runnable> tasks = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(threadQuantity);
        int portion = copyOrders.length / threadQuantity;
        for (int p = 0; p < copyOrders.length; p += portion) {
            int finalP = p;
            tasks.add(() -> {
                for (int i = 0; i < finalP; i++) {
                    sendSimpleMessage(copyOrders[i].getEmail(), "new order", "You make new order");
                }
                latch.countDown();
            });
        }
        for(Runnable task: tasks) {
            executor.submit(task);
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Scheduled(fixedDelayString = "${spring.mail.check_quantity_delay}")
    private void checkQuantityEmails() {
        System.out.println(emailProperties.getUsername());
        if (ordersToSend.size() > 20) sendEmails();
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
        } catch (Exception e) {
            log.warn("Can't send email! " + e);
        }

    }


}
