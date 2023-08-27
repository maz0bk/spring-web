package com.geekbrains.spring.web.dto;

import com.geekbrains.spring.web.entities.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
public class Cart {
    private List<OrderItemDto> items;
    private int totalPrice;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addProduct(Product product) {
        if (addProduct(product.getId())) {
            return;
        }
        items.add(new OrderItemDto(product));
        recalculateTotalPrice();
    }

    public boolean addProduct(Long id) {
        for (OrderItemDto o : items) {
            if (o.getProductId().equals(id)) {
                o.changeQuantity(1);
                recalculateTotalPrice();
                return true;
            }
        }
        return false;
    }

    public void decreaseProduct(Long id) {
        Iterator<OrderItemDto> iter = items.iterator();
        while (iter.hasNext()) {
            OrderItemDto o = iter.next();
            if (o.getProductId().equals(id)) {
                o.changeQuantity(-1);
                if (o.getQuantity() <= 0) {
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
        totalPrice = 0;
    }

    private void recalculateTotalPrice() {
        totalPrice = 0;
        for (OrderItemDto o : items) {
            totalPrice += o.getPrice();
        }
    }

    public void merge(Cart guestCart) {
        for (OrderItemDto guestItem :
                guestCart.items) {
            boolean merged = false;
            for (OrderItemDto myItem :
                    items) {
                if (myItem.getProductId().equals(guestItem.getProductId())) {
                    myItem.changeQuantity(guestItem.getQuantity());
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
