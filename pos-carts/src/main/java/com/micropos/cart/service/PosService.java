package com.micropos.cart.service;



import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.cart.model.Product;

import java.util.List;

public interface PosService {

    void checkout(Cart cart);

    Item add(Product product, int amount);

    Item add(String productId, int amount);

    List<Product> products();

    Product randomProduct();

    Item getItem(String productId);

    List<Item> getItems();

    Item removeItem(String ProductId);

    Item modifyItem(String productId, int quantity);
}
