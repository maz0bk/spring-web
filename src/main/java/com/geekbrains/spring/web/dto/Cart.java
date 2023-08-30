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
        recalculate();
    }

    public boolean addProduct(Long id) {
        for (OrderItemDto o : items) {
            if (o.getProductId().equals(id)) {
                o.setQuantity(o.getQuantity() + 1);
                o.setPrice(o.getPricePerProduct() * o.getQuantity());
                recalculate();
                return true;
            }
        }
        return false;
    }

    public void decreaseProductQuantity(Long id) {
        Iterator<OrderItemDto> iter = items.iterator();
        while (iter.hasNext()) {
            OrderItemDto o = iter.next();
            if (o.getProductId().equals(id)) {
                o.setQuantity(o.getQuantity() - 1);
                o.setPrice(o.getPricePerProduct() * o.getQuantity());
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
        for (OrderItemDto o : items) {
            totalPrice += o.getPrice();
        }
    }

    public void merge(Cart another) {
        for (OrderItemDto anotherItem :
                another.items) {
            boolean merged = false;
            for (OrderItemDto myItem :
                    items) {
                if (myItem.getProductId().equals(anotherItem.getProductId())) {
                    myItem.setQuantity(myItem.getQuantity() + anotherItem.getQuantity());
                    myItem.setPrice(myItem.getPricePerProduct() * myItem.getQuantity());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                items.add(anotherItem);
            }
        }
        recalculate();
        another.clear();
    }
}
