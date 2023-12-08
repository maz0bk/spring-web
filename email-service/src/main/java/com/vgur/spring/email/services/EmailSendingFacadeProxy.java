package com.vgur.spring.email.services;

import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class EmailSendingFacadeProxy {
    private final EmailSending emailSending;
    MeterRegistry registry = new SimpleMeterRegistry();
    Timer timer = Timer.builder("email.sending").register(registry);

    @Scheduled(fixedDelayString = "${spring.mail.sending_delay}")
    public void sendEmails() {
        timer.record(() -> emailSending.sendEmails());
    }
}
