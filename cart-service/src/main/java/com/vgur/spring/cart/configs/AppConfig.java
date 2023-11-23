package com.vgur.spring.cart.configs;

import com.vgur.spring.cart.services.PaymentService;
import com.vgur.spring.cart.services.PaymentType;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.List;
import java.util.Map;

//import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {
    @Value("${integrations.core-service.url}")
    private String coreServiceUrl;
    @Autowired
    private List<PaymentService> paymentServices;

    @Bean
    public WebClient coreServiceWebClient() {
        TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(10));
                    connection.addHandlerLast(new WriteTimeoutHandler(2));
                });

        return WebClient
                .builder()
                .baseUrl(coreServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }

    public List<PaymentService> getPaymentServices() {
        return paymentServices;
    }
}
