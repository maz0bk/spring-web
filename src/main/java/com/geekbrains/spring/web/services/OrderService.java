package com.geekbrains.spring.web.services;

import com.geekbrains.spring.web.dto.Cart;
import com.geekbrains.spring.web.dto.OrderDetailsDto;
import com.geekbrains.spring.web.dto.ProductDto;
import com.geekbrains.spring.web.entities.Order;
import com.geekbrains.spring.web.entities.OrderItem;
import com.geekbrains.spring.web.entities.Product;
import com.geekbrains.spring.web.entities.User;
import com.geekbrains.spring.web.exceptions.ResourceNotFoundException;
import com.geekbrains.spring.web.repositories.OrdersRepository;
import com.geekbrains.spring.web.repositories.ProductsRepository;
import com.geekbrains.spring.web.repositories.specifications.ProductsSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
   private final OrdersRepository ordersRepository;
   private final CartService cartService;
   private final ProductsService productsService;

   @Transactional
   public void createOrder(User user, OrderDetailsDto orderDetailsDto) {
       String cartKey = cartService.getCardUuidFromSuffix(user.getUsername());
      Cart currentCart = cartService.getCurrentCart(cartKey);
      Order order = new Order();
      order.setAddress(orderDetailsDto.getAddress());
      order.setPhone(orderDetailsDto.getPhone());
      order.setUser(user);
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
      currentCart.clear();
   }

   public List<Order> findOrdersByUsername(String username) {
       return ordersRepository.findAllByUsername(username);
   }
}
