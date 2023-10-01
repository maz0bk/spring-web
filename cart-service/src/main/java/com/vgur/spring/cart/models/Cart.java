package com.vgur.spring.cart.models;

import com.vgur.spring.api.core.ProductDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
public class Cart {
    private List<CartItem> items;
    private BigDecimal totalPrice;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addProduct(ProductDto productDto) {
        if (addProduct(productDto.getId())) {
            return;
        }
        items.add(new CartItem(productDto));
        recalculateTotalPrice();
    }

    public boolean addProduct(Long id) {
        for (CartItem o : items) {
            if (o.getProductId().equals(id)) {
                o.setQuantity(o.getQuantity()+1);
                o.setPrice(o.getPricePerProduct().multiply(BigDecimal.valueOf(o.getQuantity())));
                recalculateTotalPrice();
                return true;
            }
        }
        return false;
    }

    public void decreaseProductQuantity(Long id) {
        Iterator<CartItem> iter = items.iterator();
        while (iter.hasNext()) {
            CartItem cartItem = iter.next();
            if (cartItem.getProductId().equals(id)) {
                cartItem.setQuantity(cartItem.getQuantity()-1);
                if (cartItem.getQuantity() <= 0) {
                    iter.remove();
                }
                recalculateTotalPrice();
                return;
            }
        }
    }

    public void removeProduct(Long id) {
        items.removeIf(o -> o.getProductId().equals(id));
        recalculateTotalPrice();
    }

    public void clear() {
        items.clear();
        totalPrice = BigDecimal.ZERO;
    }

    private void recalculateTotalPrice() {
        totalPrice = BigDecimal.ZERO;
        for (CartItem o : items) {
            totalPrice = totalPrice.add(o.getPrice());
        }
    }

    public void merge(Cart guestCart) {
        for (CartItem guestItem :
                guestCart.items) {
            boolean merged = false;
            for (CartItem myItem :
                    items) {
                if (myItem.getProductId().equals(guestItem.getProductId())) {
                    myItem.setQuantity(myItem.getQuantity()+guestItem.getQuantity());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                items.add(guestItem);
            }
        }
        recalculateTotalPrice();
        guestCart.clear();
    }
}
