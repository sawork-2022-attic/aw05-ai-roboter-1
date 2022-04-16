package com.micropos.cart.rest;

import com.micropos.cart.api.ItemApi;
import com.micropos.cart.dto.ItemDto;
import com.micropos.cart.dto.ItemFiledDto;
import com.micropos.cart.mapper.ItemMapper;
import com.micropos.cart.model.Item;
import com.micropos.cart.model.Product;
import com.micropos.cart.service.PosService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;


@RestController
@RequestMapping("api")
@CrossOrigin
public class CartController implements ItemApi {



    private final PosService posService;

    private final ItemMapper itemMapper;


    public CartController(PosService service, ItemMapper itemMapper) {
        this.posService = service;
        this.itemMapper = itemMapper;
    }


    @Override
    public ResponseEntity<List<ItemDto>> showCartItems() {
        var items = posService.getItems();

        var itemDtos = itemMapper.toItemDtos(items);
        return new ResponseEntity<>(itemDtos, HttpStatus.OK);
    }

    @Override
    @HystrixCommand(fallbackMethod = "productsError")
    public ResponseEntity<ItemDto> addItem(String productId, ItemFiledDto itemFiledDto) {
        int quantity = itemFiledDto.getQuantity();
        return ResponseEntity.ok(itemMapper.toItemDto(posService.add(productId, quantity)));
    }

    @Override
    @HystrixCommand(fallbackMethod = "productsError")
    public ResponseEntity<ItemDto> deleteItem(String productId) {
        return ResponseEntity.ok(itemMapper.toItemDto(posService.removeItem(productId)));
    }

    @Override
    @HystrixCommand(fallbackMethod = "productsError")
    public ResponseEntity<ItemDto> updateItem(String productId, ItemFiledDto itemFiledDto) {
        return ResponseEntity.ok(itemMapper.toItemDto(posService.modifyItem(productId, itemFiledDto.getQuantity())));
    }

    public ResponseEntity<ItemDto> productsError(String productId, ItemFiledDto itemFiledDto) {
        return ResponseEntity.ok(itemMapper.toItemDto(new Item(new Product("00", "发生错误,触发熔断器", 0, ""), 0)));
    }
}
