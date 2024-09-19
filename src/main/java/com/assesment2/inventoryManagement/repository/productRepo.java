package com.assesment2.inventoryManagement.repository;

import com.assesment2.inventoryManagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {

    public List<Product> findAllByName(String productName);
    public List<Product> findAllByProductId(int productId);

    List<Product> findAllByCategoryId(int categoryId);

    List<Product> findAllByProductIdAndCategoryId(int productId, int categoryId);

    void deleteByProductId(int productId);
}
