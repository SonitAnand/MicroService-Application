package com.cloud.stream.controller;

import com.cloud.stream.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableBinding(Source.class)
@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    private MessageChannel output;

    @PostMapping(value = "/publish")
    public Book publishEvent(@RequestBody Book book){
        output.send(MessageBuilder.withPayload(book).build());
        return book;
    }
}
