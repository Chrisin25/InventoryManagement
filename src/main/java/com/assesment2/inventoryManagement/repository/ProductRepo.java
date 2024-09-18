package com.assesment2.inventoryManagement.repository;

import com.assesment2.inventoryManagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,String> {

}
