package com.geekbrains.spring.web.controllers;

import com.geekbrains.spring.web.entities.User;
import com.geekbrains.spring.web.exceptions.ResourceNotFoundException;
import com.geekbrains.spring.web.services.OrderService;
import com.geekbrains.spring.web.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping
    @ResponseBody
    public void createOrder (@RequestParam(value = "address", required = false) String address, @RequestParam(value = "phone",required = false) String phone, Principal principal){
        User user = userService.findByUsername(principal.getName()).orElseThrow(() ->new ResourceNotFoundException("Can't find user"));
        orderService.createOrderFromCart(address, phone, user);
    }


}
