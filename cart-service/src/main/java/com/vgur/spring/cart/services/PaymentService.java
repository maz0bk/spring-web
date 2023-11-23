package com.vgur.spring.cart.services;

public interface PaymentService {
    void pay();

    PaymentType getPaymentType();

}
