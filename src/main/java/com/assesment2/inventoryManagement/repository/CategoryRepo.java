package com.assesment2.inventoryManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assesment2.inventoryManagement.model.Category;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category,Integer>{

    List<Category> findAllByCategoryId(int categoryId);

    Category findByCategoryName(String categoryName);

    boolean existsByCategoryId(Integer categoryId);


    boolean existsByCategoryName(String categoryName);

    Category findByCategoryId(Integer categoryId);

}
