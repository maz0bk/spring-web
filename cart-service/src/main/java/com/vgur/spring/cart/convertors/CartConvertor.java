package com.vgur.spring.cart.convertors;

import com.vgur.spring.api.carts.CartDto;
import com.vgur.spring.api.carts.CartItemDto;
import com.vgur.spring.cart.models.Cart;

import java.util.stream.Collectors;

public class CartConvertor {
    public CartDto modelToDto(Cart cart){
        var cartItemDtos = cart.getItems().stream().map( itemDto ->
                new CartItemDto(itemDto.getProductId(),
                        itemDto.getProductTitle(),
                        itemDto.getQuantity(),
                        itemDto.getPricePerProduct(),
                        itemDto.getPrice())).collect(Collectors.toList());

        return new CartDto(cartItemDtos,cart.getTotalPrice());
    }
}
