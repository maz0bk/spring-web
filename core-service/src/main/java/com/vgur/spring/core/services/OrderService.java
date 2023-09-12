package com.vgur.spring.core.services;

import com.vgur.spring.api.carts.CartDto;
import com.vgur.spring.api.core.OrderDetailsDto;
import com.vgur.spring.api.exceptions.ResourceNotFoundException;
import com.vgur.spring.core.entities.Order;
import com.vgur.spring.core.entities.OrderItem;
import com.vgur.spring.core.integrations.CartServiceIntegration;
import com.vgur.spring.core.repositories.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
   private final OrdersRepository ordersRepository;
   private final CartServiceIntegration cartServiceIntegration;
   private final ProductsService productsService;

   @Transactional
   public void createOrder(String userName, OrderDetailsDto orderDetailsDto) {
      CartDto currentCart = cartServiceIntegration.getUserCart(userName);
      Order order = new Order();
      order.setAddress(orderDetailsDto.getAddress());
      order.setPhone(orderDetailsDto.getPhone());
      order.setUserName(userName);
      order.setTotalPrice(currentCart.getTotalPrice());
      List<OrderItem> items = currentCart.getItems().stream()
              .map(o -> {
                 OrderItem item = new OrderItem();
                 item.setOrder(order);
                 item.setQuantity(o.getQuantity());
                 item.setPricePerProduct(o.getPricePerProduct());
                 item.setPrice(o.getPrice());
                 item.setProduct(productsService.findById(o.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found")));
                 return item;
              }).collect(Collectors.toList());
      order.setItems(items);
      ordersRepository.save(order);
      cartServiceIntegration.clearUserCart(userName);
   }

   public List<Order> findOrdersByUsername(String username) {
       return ordersRepository.findAllByUsername(username);
   }
}
