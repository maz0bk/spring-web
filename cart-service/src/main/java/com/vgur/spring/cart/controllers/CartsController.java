package com.vgur.spring.cart.controllers;

import com.vgur.spring.api.dto.StringResponse;
import com.vgur.spring.cart.models.Cart;
import com.vgur.spring.cart.services.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Slf4j
public class CartsController {
    private final CartService cartService;

    @GetMapping("/{uuid}")
    public Cart getCurrentCart(@RequestHeader(required = false) String username, @PathVariable String uuid) {
        return cartService.getCurrentCart(getCurrentCartUuid(username, uuid));
    }

    @GetMapping("/generate")
    public StringResponse getCart() {
        return new StringResponse(cartService.generateCartUuid());
    }

    private String getCurrentCartUuid(String username, String uuid) {
        if (username != null) {
            return cartService.getCardUuidFromSuffix(username);
        }
        return cartService.getCardUuidFromSuffix(uuid);
    }

    @GetMapping("{uuid}/add/{productId}")
    public void addProductToCart(@RequestHeader(required = false) String username, @PathVariable String uuid, @PathVariable Long productId) {
        cartService.addToCart(getCurrentCartUuid(username, uuid), productId);
    }

    @GetMapping("{uuid}/remove/{productId}")
    public void remove(@RequestHeader(required = false) String username, @PathVariable String uuid, @PathVariable Long productId) {
        cartService.removeItemFromCart(getCurrentCartUuid(username, uuid), productId);
    }

    @GetMapping("{uuid}/clear")
    public void clearCart(@RequestHeader(required = false) String username, @PathVariable String uuid) {
        cartService.clear(getCurrentCartUuid(username, null));
    }

    @GetMapping("{uuid}/merge")
    public void merge(@RequestHeader(required = false) String username, @PathVariable String uuid) {
        cartService.merge(getCurrentCartUuid(username, null),
                getCurrentCartUuid(null, uuid));
    }
}
