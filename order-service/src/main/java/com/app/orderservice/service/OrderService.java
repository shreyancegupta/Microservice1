package com.app.orderservice.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.orderservice.Repo.OrderRepository;
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

  
    public void   placeOrder(OrderRequest orderRequest)
    {

        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
       List<OrderLineItem> orderLineItemList =orderRequest.getOrderLineItemDtoList()
        .stream()
        .map(this::maptoDto)
        .collect(Collectors.toList());
        
        order.setOrderLineItemList(orderLineItemList);
        orderRepository.save(order);
        log.info("Order {} has been saved ",order.getId());
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
