package com.vgur.spring.api.core;

import com.vgur.spring.api.dto.OrderItemDto;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class OrderDto {
    private Long id;
    private String username;
    private List<OrderItemDto> items;
    private Integer totalPrice;
    private String address;
    private String phone;
}
