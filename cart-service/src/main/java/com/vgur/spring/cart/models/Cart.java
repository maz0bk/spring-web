package com.vgur.spring.cart.models;

import com.vgur.spring.api.core.ProductDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
public class Cart {
    private List<CartItem> items;
    private int totalPrice;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addProduct(ProductDto productDto) {
        if (addProduct(productDto.getId())) {
            return;
        }
        items.add(new CartItem(productDto));
        recalculate();
    }

    public boolean addProduct(Long id) {
        for (CartItem o : items) {
            if (o.getProductId().equals(id)) {
                o.setQuantity(o.getQuantity()+1);
                recalculate();
                return true;
            }
        }
        return false;
    }

    public void decreaseProduct(Long id) {
        Iterator<CartItem> iter = items.iterator();
        while (iter.hasNext()) {
            CartItem o = iter.next();
            if (o.getProductId().equals(id)) {
                o.setQuantity(o.getQuantity()-1);
                if (o.getQuantity() <= 0) {
                    iter.remove();
                }
                recalculate();
                return;
            }
        }
    }

    public void removeProduct(Long id) {
        items.removeIf(o -> o.getProductId().equals(id));
        recalculate();
    }

    public void clear() {
        items.clear();
        totalPrice = 0;
    }

    private void recalculate() {
        totalPrice = 0;
        for (CartItem o : items) {
            totalPrice += o.getPrice();
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
        recalculate();
        guestCart.clear();
    }
}
