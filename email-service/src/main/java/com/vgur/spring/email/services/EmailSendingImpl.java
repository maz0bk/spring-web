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
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendingImpl implements EmailSending{
    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private List<OrderDto> ordersToSend = new CopyOnWriteArrayList<>();

    @RabbitListener(queues = "EmailSending")
    public void listenEmailSendingQueue(OrderDto orderDto) {
        ordersToSend.add(orderDto);
    }

//    @Scheduled(fixedDelayString = "${spring.mail.sending_delay}")
   // @Scheduled(fixedDelayString = "1000")
    public void sendEmails() {

        var copyOrders = ordersToSend.toArray();
        ordersToSend.clear();

        try {
            Thread.sleep( new Random().nextInt(10000) +1);
        } catch (InterruptedException e) {
            log.warn(e.toString());
        }

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);
        ArrayList<Runnable> tasks = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(availableProcessors);
        int portion = copyOrders.length / availableProcessors + 1;

        for (int taskNumber = 0; taskNumber < availableProcessors; taskNumber++) {
            int finalTaskNumber = taskNumber;
            tasks.add(() -> {
                for (int i = finalTaskNumber * portion; i < (finalTaskNumber + 1) * portion; i++) {
                    OrderDto orderDto = (OrderDto) copyOrders[i];
                    sendSimpleMessage(orderDto.getEmail(), "new order", "You make new order");
                }
//             latch.countDown(60, TimeUnit.SECONDS);
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

    //@Scheduled(fixedDelayString = "10000")
    @Scheduled(fixedDelayString = "${spring.mail.check_quantity_delay}")
    private void checkQuantityEmails() {
        if (ordersToSend.size() > 20) sendEmails();
    }

    private void sendSimpleMessage(
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
