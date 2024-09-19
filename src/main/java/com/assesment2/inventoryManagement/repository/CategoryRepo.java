package com.assesment2.inventoryManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assesment2.inventoryManagement.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer>{

    List<Category> findAllByCategoryId(int categoryId);
}
