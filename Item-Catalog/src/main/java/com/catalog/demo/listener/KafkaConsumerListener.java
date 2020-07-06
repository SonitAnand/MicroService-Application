package com.catalog.demo.listener;

import com.catalog.demo.model.Item;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerListener {

    @KafkaListener(topics = "Kafka_Example",groupId = "group_id")
    public void consume(String message) {
        System.out.println("Consumed messages : " + message);
    }

    @KafkaListener(topics = "Kafka_Example_Json" ,groupId = "group_json",containerFactory = "itemkafkaListenerContainerFactory")
    public void consumeJson(Item item) {
        System.out.println("Consumed JSON messages : " + item);
    }
}
