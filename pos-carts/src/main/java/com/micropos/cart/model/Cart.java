package com.micropos.cart.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Component
@SessionScope
public class Cart implements Serializable {

    private List<Item> items = new ArrayList<>();

    public Item addItem(Item item) {
        var target = getItem(item.getProduct().getId());
        if (target != null) {
            target.setQuantity(target.getQuantity() + item.getQuantity());
            return target;
        }
        items.add(item);
        return item;
    }

    public Item getItem(String productId) {
        for (Item currentItem : items) {
            if (currentItem.getProduct().getId().equals(productId)) {
                return currentItem;
            }
        }
        return null;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

        public double getTotal() {
        double total = 0;
        for (Item item : items) {
            total += item.getQuantity() * item.getProduct().getPrice();
        }
        return total;
    }

}
