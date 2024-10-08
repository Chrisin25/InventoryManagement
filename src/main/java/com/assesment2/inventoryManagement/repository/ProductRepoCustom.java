//package com.assesment2.inventoryManagement.repository;
//
//import com.assesment2.inventoryManagement.model.Product;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ProductRepoCustom {
//    Page<Product> findAll(Pageable pageable);
//
//    List<Product> findAllByProductId(Integer productId);
//
//
//    Page<Product> findAllByCategoryId(Integer categoryId,Pageable pageable);
//
//    List<Product> findAllByProductIdAndCategoryId(Integer productId, Integer categoryId);
//
//    void deleteByProductId(Integer productId);
//
//    Product findByProductId(Integer productId);
//
//    boolean existsByProductName(String productName);
//
//    boolean existsAllByCategoryId(Integer categoryId);
//
//    boolean existsByProductId(Integer productId);
//
//    void save(Product product);
//}
