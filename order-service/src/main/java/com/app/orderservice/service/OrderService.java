package com.app.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;


import com.app.orderservice.Repo.OrderRepository;
import com.app.orderservice.dto.InventoryResponse;
import com.app.orderservice.dto.OrderLineItemDto;
import com.app.orderservice.dto.OrderRequest;
import com.app.orderservice.model.Order;
import com.app.orderservice.model.OrderLineItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

 
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {
    
   private final OrderRepository orderRepository;
   private final WebClient.Builder webClientBuilder;
  
    public String   placeOrder(OrderRequest orderRequest)
    {

        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
       List<OrderLineItem> orderLineItemList =orderRequest.getOrderLineItemDtoList()
        .stream()
        .map(this::maptoDto)
        .collect(Collectors.toList());
        
         
     order.setOrderLineItemList(orderLineItemList);
        
    List<String> skuCodes=   order.getOrderLineItemList()
     .stream()
     .map(OrderLineItem::getSkuCode)
     .collect(Collectors.toList());

     /**  Boolean isInStock=  webClient.get()
        .uri("http://localhost:8082/api/inventory")
        .retrieve()
        .bodyToMono(Boolean.class)
        .block();
       
       if(isInStock)
       {
        orderRepository.save(order);
       }else
       {
         throw new IllegalArgumentException("Product is not in stock");
       }

       */


     
/*        InventoryResponse[] inventoryResponsesArray=  webClient.get()
       .uri("http://inventory-service/api/inventory",uriBuilder->uriBuilder.queryParam("skuCode",skuCodes).build())
       .retrieve()
       .bodyToMono(InventoryResponse[].class)
       .block();
   */   
  InventoryResponse[] inventoryResponsesArray=  webClientBuilder.build().get()
  .uri("http://inventory-service/api/inventory",uriBuilder->uriBuilder.queryParam("skuCode",skuCodes).build())
  .retrieve()
  .bodyToMono(InventoryResponse[].class)
  .block();
    boolean allProductInStock= Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::isInStock);
      if(allProductInStock)
      {
       orderRepository.save(order);
       log.info("Order {} has been saved ",order.getId());

       return "order placed successfully";
      }else
      {
        throw new IllegalArgumentException("Product is not in stock");
      }
        
    }

    private OrderLineItem maptoDto(OrderLineItemDto orderLineItemDto)
    {
        OrderLineItem orderLineItem=new OrderLineItem();
        orderLineItem.setPrice(orderLineItemDto.getPrice());
        orderLineItem.setQuantity(orderLineItemDto.getQuantity());
        orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());
        return orderLineItem;
    }
}
