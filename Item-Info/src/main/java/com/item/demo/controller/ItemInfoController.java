package com.item.demo.controller;

import com.item.demo.model.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping(value = "/items")
public class ItemInfoController {

    @RequestMapping("/{itemId}")
    public Item getItemInfo(@PathVariable("itemId") String itemId){

        return new Item(itemId,"Test Item TT","500");
    }

    @Value("${message:Hello default}")
    private String message;

    @RequestMapping("/message")
    String getMessage() {
        return this.message;
    }
}
