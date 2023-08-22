package com.geekbrains.spring.web.converters;

import com.geekbrains.spring.web.dto.OrderItemDto;
import com.geekbrains.spring.web.dto.ProductDto;
import com.geekbrains.spring.web.entities.OrderItem;
import com.geekbrains.spring.web.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class OrderItemConverter {
    public OrderItem dtoToEntity(OrderItemDto orderItemDto) {
        throw new UnsupportedOperationException();
    }

    public OrderItemDto entityToDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getProduct().getId(), orderItem.getProduct().getTitle(), orderItem.getQuantity(), orderItem.getPricePerProduct(), orderItem.getPrice());
    }
}
