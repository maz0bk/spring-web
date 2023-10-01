package com.vgur.spring.cart.services;


import com.vgur.spring.api.core.ProductDto;
import com.vgur.spring.api.exceptions.ResourceNotFoundException;
import com.vgur.spring.cart.integrations.ProductServiceIntegration;
import com.vgur.spring.cart.models.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductServiceIntegration productServiceIntegration;
    private final RedisTemplate<String,Object> redisTemplate;
    private Cart cart;
    @Value("${utils.cart.prefix}")
    private String cartPrefix;

    @PostConstruct
    public void init() {
        cart = new Cart();
    }

    public String getCardUuidFromSuffix(String suffix){
        return cartPrefix + suffix;
    }

    public String generateCartUuid(){
        return UUID.randomUUID().toString();
    }

    public Cart getCurrentCart(String cartKey) {
        if(!redisTemplate.hasKey(cartKey)){
            redisTemplate.opsForValue().set(cartKey, new Cart());
        }
        return (Cart) redisTemplate.opsForValue().get(cartKey);
    }

    public void addToCart(String cardKey, Long productId) {
        var restTemplate = new RestTemplate();

        ProductDto productDto = productServiceIntegration.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Невозможно добавить продукт в корзину. Продукт не найдет, id: " + productId));
                restTemplate.getForObject("http://localhost:5555/core/api/v1/products/"+productId, ProductDto.class);
        execute(cardKey, c -> c.addProduct(productDto));
        }
    public void execute(String cartKey, Consumer<Cart> action){
        Cart cart = getCurrentCart(cartKey);
        action.accept(cart);
        redisTemplate.opsForValue().set(cartKey, cart);
    }

    public void clear(String cartKey) {
        execute(cartKey, Cart::clear);
    }

    public void removeItemFromCart(String cartKey, Long productId) {
        execute(cartKey, c-> c.removeProduct(productId));
    }

    public void merge(String userCartKey, String guestCartKey) {
        Cart guestCart =getCurrentCart(guestCartKey);
        Cart userCart =getCurrentCart(userCartKey);
        userCart.merge(guestCart);
        updateCart(guestCartKey,guestCart);
        updateCart(userCartKey,userCart);
    }

    private void updateCart(String cartKey, Cart cart) {
        redisTemplate.opsForValue().set(cartKey, cart);
    }
}
