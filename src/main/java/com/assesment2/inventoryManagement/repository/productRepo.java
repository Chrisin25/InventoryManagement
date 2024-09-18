package com.assesment2.inventoryManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assesment2.inventoryManagement.model.Product;

public interface ProductRepo extends JpaRepository<Product,Integer>{

}
