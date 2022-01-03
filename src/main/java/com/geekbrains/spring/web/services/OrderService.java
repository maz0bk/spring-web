package com.geekbrains.spring.web.services;

import com.geekbrains.spring.web.dto.Cart;
import com.geekbrains.spring.web.dto.OrderItemDto;
import com.geekbrains.spring.web.entities.Order;
import com.geekbrains.spring.web.entities.OrderItem;
import com.geekbrains.spring.web.entities.User;
import com.geekbrains.spring.web.exceptions.ResourceNotFoundException;
import com.geekbrains.spring.web.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductsService productsService;
    private final CartService cartService;

    public Order save(Order order){
        return orderRepository.save(order);
    }

    @Transactional
    public void createOrderFromCart(String address, String phone, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPhone(phone);
        Cart cart = cartService.getCurrentCart();
        order.setTotalPrice(cart.getTotalPrice());
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDto i: cart.getItems()) {
            OrderItem orderItem = new OrderItem(order,
                    productsService.findById(i.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Can't find product by ID")),
                    i.getQuantity(),
                    i.getPricePerProduct(),
                    i.getPrice());
            orderItemList.add(orderItem);
        }
        order.setItems(orderItemList);
        save(order);
        cart.clear();
    }
}
