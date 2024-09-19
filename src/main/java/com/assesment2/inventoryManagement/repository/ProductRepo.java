package com.assesment2.inventoryManagement.repository;

import com.assesment2.inventoryManagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {


    public List<Product> findAllByProductName(String productName);
    public List<Product> findAllByProductId(int productId);

    List<Product> findAllByCategoryId(int categoryId);

    List<Product> findAllByProductIdAndCategoryId(int productId, int categoryId);

    void deleteByProductId(int productId);
}
