package com.geekbrains.spring.web.controllers;

import com.geekbrains.spring.web.dto.Cart;
import com.geekbrains.spring.web.entities.User;
import com.geekbrains.spring.web.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public void createOrder(@RequestBody Cart cart, @RequestBody User user){
        orderService.createOrderFromCart(cart, user);
    }

}
