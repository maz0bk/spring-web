package com.vgur.spring.core.services;

import com.vgur.spring.api.carts.CartDto;
import com.vgur.spring.api.core.OrderDetailsDto;
import com.vgur.spring.api.exceptions.ResourceNotFoundException;
import com.vgur.spring.core.converters.OrderConverter;
import com.vgur.spring.core.entities.Order;
import com.vgur.spring.core.entities.OrderItem;
import com.vgur.spring.core.integrations.CartServiceIntegration;
import com.vgur.spring.core.repositories.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
   private final OrdersRepository ordersRepository;
   private final CartServiceIntegration cartServiceIntegration;
   private final ProductsService productsService;
   private final RabbitTemplate rabbitTemplate;
   private final OrderConverter orderConverter;

   @Transactional
   public void createOrder(String userName, OrderDetailsDto orderDetailsDto) {
      CartDto currentCart = cartServiceIntegration.getUserCart(userName);
      Order order = Order.builder()
              .address(orderDetailsDto.getAddress())
              .phone(orderDetailsDto.getPhone())
              .email(orderDetailsDto.getEmail())
              .userName(userName)
              .totalPrice(currentCart.getTotalPrice())
              .build();

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
      try {
          rabbitTemplate.convertAndSend("OrdersExchanger","newOrder", orderConverter.entityToDto(order) );
      }
      catch (Exception e){
          System.out.println(e);
          log.warn("Exception!!! "+ e);
      }


   }

   public List<Order> findOrdersByUsername(String username) {
       return ordersRepository.findAllByUsername(username);
   }
}
