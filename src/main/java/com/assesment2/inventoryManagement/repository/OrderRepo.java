package com.assesment2.inventoryManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assesment2.inventoryManagement.model.Order;

public interface OrderRepo extends JpaRepository<Order,Integer>{
    
}
