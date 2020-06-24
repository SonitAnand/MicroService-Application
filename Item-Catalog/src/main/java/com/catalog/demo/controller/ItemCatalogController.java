package com.catalog.demo.controller;

import com.catalog.demo.hystrix.CommonHystrixCommand;
import com.catalog.demo.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "/catalog")
public class ItemCatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    // use this to get host IP of service
    private String getBaseURL(){
        ServiceInstance serviceInstance = loadBalancerClient.choose("item-info");
        return serviceInstance.getUri().toString();
    }

    // use this if want more control on load balancing and specific configurations
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/{itemId}")
    public List<Item> getCatalog(@PathVariable ("itemId") String itemId) throws ExecutionException, InterruptedException {

        System.out.println("DiscoveryClient Services : " + discoveryClient.getServices());
        System.out.println("DiscoveryClient Instances : " + discoveryClient.getInstances("item-info"));
        System.out.println("DiscoveryClient Instances : " + discoveryClient.getInstances("item-catalog"));
        System.out.println("Get Base URL : " + getBaseURL());

        CommonHystrixCommand<Item> itemCommonHystrixCommand = new CommonHystrixCommand<Item>("default",() ->
            {
                return restTemplate.getForObject(getBaseURL()+"89/items/"+itemId,Item.class);
            },() -> {
            return  new Item();
        });

        Future<Item> itemFuture = itemCommonHystrixCommand.queue();
        itemFuture.get();
        System.out.println("Items Info : " + itemFuture.get());
        //Item item = restTemplate.getForObject("http://localhost:8089/items/"+itemId,Item.class);
        //Item item =webClientBuilder.build().get().uri("http://localhost:8089/items/sonit").retrieve().bodyToMono(Item.class).block();
        List<Item> itemList= new ArrayList<>();
        itemList.add(itemFuture.get());
        return itemList;
    }

    @Autowired
    private KafkaTemplate<String,Item> kafkaTemplate;
    private static String topic = "Kafka_Example";

    @GetMapping(value = "/publish/{name}")
    public String post(@PathVariable("name") final String name){
        kafkaTemplate.send(topic,new Item("101",name,"20099"));
        return "Publish Successfully";
    }
}
