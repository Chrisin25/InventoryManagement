package com.assesment2.inventoryManagement.repository;

import com.assesment2.inventoryManagement.model.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<OrderTable,Integer>{
    
}
