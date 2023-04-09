package com.app.orderservice.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order,Long>{
    
}
