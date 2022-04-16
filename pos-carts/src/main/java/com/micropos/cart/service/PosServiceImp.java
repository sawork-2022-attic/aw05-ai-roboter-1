package com.micropos.cart.service;


import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.cart.model.Product;
import com.micropos.cart.repository.JDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class PosServiceImp implements PosService, Serializable {

    private JDRepository db;

    private Cart cart;

    @Autowired
    public void setDb(JDRepository db) {
        this.db = db;
    }

    @Autowired
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public Product randomProduct() {
        return products().get(ThreadLocalRandom.current().nextInt(0, products().size()));
    }

    @Override
    public Item getItem(String productId) {
        return getItems().stream()
                            .filter(item -> item.getProduct().getId().equals(productId))
                            .findFirst().orElse(null);
    }

    @Override
    public void checkout(Cart cart) {

    }

    @Override
    public Item add(Product product, int amount) {
        return add(product.getId(), amount);
    }

    @Override
    public Item add(String productId, int amount) {

        var  product = db.getProduct(productId);
        if (product == null) return null;
        return cart.addItem(new Item(product, amount));

    }


    @Override
    public List<Product> products() {
        System.out.println("get products");
        return db.allProducts();
    }

    @Override
    public List<Item> getItems() {
        if (cart == null)
            cart = new Cart();
        return cart.getItems();
    }

    @Override
    public Item removeItem(String productId) {
        var items = getItems();
        var target = items.stream().filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(null);
        if (target == null)
            return null;

        items.remove(target);
        return target;
    }

    @Override
    public Item modifyItem(String productId, int quantity) {
        var target = getItem(productId);
        if (target == null) {
            return null;
        }
        target.setQuantity(quantity);
        return target;
    }
}
