package com.vgur.spring.api.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Order model")
@Data
@Builder
public class OrderDto {
    @Schema(description = "Order ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;
    @Schema(description = "User name", requiredMode = Schema.RequiredMode.REQUIRED, example = "bob")
    private String username;
    @Schema(description = "List of items", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<OrderItemDto> items;
    @Schema(description = "Total price", requiredMode = Schema.RequiredMode.REQUIRED, example = "150.05")
    private BigDecimal totalPrice;
    @Schema(description = "Address", example = "Germany, Dusseldorf, Kaiserswerther Str. 380, 40474")
    private String address;
    @Schema(description = "Phone number", example = "+491121165588")
    private String phone;
}
