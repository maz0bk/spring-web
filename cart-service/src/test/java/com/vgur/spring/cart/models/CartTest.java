package com.vgur.spring.cart.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class CartTest {

    @Test
    void merge() {
        var cartItem  = new CartItem();
        cartItem.setProductId(1l);
        cartItem.setProductTitle("Milk");
        cartItem.setQuantity(2);
        cartItem.setPricePerProduct(100);
        cartItem.setPrice(200);

        var cartItem2  = new CartItem();
        cartItem2.setProductId(2l);
        cartItem2.setProductTitle("Bread");
        cartItem2.setQuantity(1);
        cartItem2.setPricePerProduct(50);
        cartItem2.setPrice(50);

        List<CartItem> itemList = new ArrayList<>();
        itemList.add(cartItem);
        itemList.add(cartItem2);

        Cart cartUser = new Cart();
        cartUser.setItems(itemList);
        cartUser.setTotalPrice(250);

        var cartItem3  = new CartItem();
        cartItem3.setProductId(3l);
        cartItem3.setProductTitle("Egg");
        cartItem3.setQuantity(10);
        cartItem3.setPricePerProduct(20);
        cartItem3.setPrice(200);

        var cartItem4  = new CartItem();
        cartItem4.setProductId(4l);
        cartItem4.setProductTitle("Honey");
        cartItem4.setQuantity(1);
        cartItem4.setPricePerProduct(150);
        cartItem4.setPrice(150);

        List<CartItem> itemList2 = new ArrayList<>();
        itemList2.add(cartItem3);
        itemList2.add(cartItem4);
        itemList2.add(cartItem);

        Cart cartGuest = new Cart();
        cartGuest.setItems(itemList);
        cartGuest.setTotalPrice(550);

        cartUser.merge(cartGuest);
        Assertions.assertEquals(800,cartUser.getTotalPrice());


    }
}