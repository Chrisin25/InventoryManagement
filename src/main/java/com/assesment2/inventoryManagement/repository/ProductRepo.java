package com.assesment2.inventoryManagement.repository;

import com.assesment2.inventoryManagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {


    public List<Product> findAllByProductName(String productName);
    public List<Product> findAllByProductId(Integer productId);


    List<Product> findAllByCategoryId(Integer categoryId);

    List<Product> findAllByProductIdAndCategoryId(Integer productId, Integer categoryId);

    void deleteByProductId(Integer productId);




    List<Product> findByProductId(Integer productId);
}
