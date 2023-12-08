package com.vgur.spring.email.services;

import com.vgur.spring.email.config.EmailConfig;
import com.vgur.spring.email.config.EmailProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

//@SpringJUnitConfig(EmailSending.class)
@SpringJUnitConfig(EmailConfig.class)

@ComponentScan("com.vgur.spring.email")
class ScheduledEmailSendingTest {
    @SpyBean
    private EmailSendingImpl emailSending;

    @MockBean
    private JavaMailSender mailSender;

    @MockBean
    private EmailProperties emailProperties;

    @Test
    public void whenWaitThreeMin_thenScheduledIsCalledAtLeastThreeTimes(){
        await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> verify(emailSending, atLeast(3)).sendEmails());
    }
}