package com.geekbrains.spring.web.services;

import com.geekbrains.spring.web.dto.Cart;
import com.geekbrains.spring.web.entities.Product;
import com.geekbrains.spring.web.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductsService productsService;
    private final RedisTemplate<String,Object> redisTemplate;
    private Cart cart;
    @Value("SPRING_WEB_")
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
            Product product = productsService.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Невозможно добавить продукт в корзину. Продукт не найдет, id: " + productId));
            execute(cardKey, c -> c.addProduct(product));
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
