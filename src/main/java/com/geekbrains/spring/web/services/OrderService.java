package com.geekbrains.spring.web.services;

import com.geekbrains.spring.web.dto.Cart;
import com.geekbrains.spring.web.dto.OrderItemDto;
import com.geekbrains.spring.web.entities.Order;
import com.geekbrains.spring.web.entities.OrderItem;
import com.geekbrains.spring.web.entities.User;
import com.geekbrains.spring.web.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductsService productsService;

    public Order save(Order order){
        return orderRepository.save(order);
    }

    public void createOrderFromCart(Cart cart, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(cart.getTotalPrice());
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDto i: cart.getItems()) {
            OrderItem orderItem = new OrderItem(productsService.findById(i.getProductId()).get(),i.getQuantity(),i.getPricePerProduct(),i.getPrice());
            orderItemList.add(orderItem);
        }
        order.setItems(orderItemList);
        save(order);
    }
}
