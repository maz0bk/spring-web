package com.vgur.spring.cart.services;

import org.springframework.stereotype.Service;

@Service
public class CardPaymentServiceImpl implements PaymentService {
    @Override
    public void pay() {

    }

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.CARD;
    }
}
