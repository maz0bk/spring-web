package com.geekbrains.spring.web.controllers;

import com.geekbrains.spring.web.dto.Cart;
import com.geekbrains.spring.web.dto.StringResponse;
import com.geekbrains.spring.web.services.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Slf4j
public class CartsController {
    private final CartService cartService;

    @GetMapping("/{uuid}")
    public Cart getCurrentCart(Principal principal, @PathVariable String uuid) {
        String username = null;
        if(principal != null){
            username = principal.getName();
        }
        log.warn("GET current cart "+ username +" uuid: "+uuid);
        return cartService.getCurrentCart(getCurrentCartUuid(username,uuid));
    }
    @GetMapping("/generate")
    public StringResponse getCart(){
        return new StringResponse(cartService.generateCartUuid());
    }
    private String getCurrentCartUuid(String username, String uuid) {
        if(username != null){
            return cartService.getCardUuidFromSuffix(username);
        }
        return cartService.getCardUuidFromSuffix(uuid);
    }

    @GetMapping("{uuid}/add/{productId}")
    public void addProductToCart(Principal principal, @PathVariable  String uuid, @PathVariable Long productId) {
        String userName = null;
        if(principal != null){
            userName = principal.getName();
        }
        cartService.addToCart(getCurrentCartUuid(userName,uuid), productId);
        log.warn("Add product "+productId +" user: "+ userName +" uuid: "+uuid);
    }
    @GetMapping("{uuid}/remove/{productId}")
    public void remove(Principal principal, @PathVariable  String uuid, @PathVariable Long productId) {
        String userName = null;
        if(principal != null){
            userName = principal.getName();
        }
        cartService.removeItemFromCart(getCurrentCartUuid(userName,uuid), productId);
    }

    @GetMapping("{uuid}/clear")
    public void clearCart(Principal principal, @PathVariable String uuid)
    {
        String userName = null;
        if(principal != null){
            userName = principal.getName();
        }
        cartService.clear(getCurrentCartUuid(userName,null));
    }
    @GetMapping("{uuid}/merge")
    public void merge(Principal principal, @PathVariable String uuid){
        String userName = null;
        if(principal != null){
            userName = principal.getName();
        }
        cartService.merge(getCurrentCartUuid(userName,null),
                getCurrentCartUuid(null, uuid));

    }
}
