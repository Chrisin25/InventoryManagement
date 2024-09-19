package com.assesment2.inventoryManagement.service;

import com.assesment2.inventoryManagement.model.Category;
import com.assesment2.inventoryManagement.model.Product;
import com.assesment2.inventoryManagement.repository.CategoryRepo;
import com.assesment2.inventoryManagement.repository.ProductRepo;
import com.assesment2.inventoryManagement.response.ResponseMessageForUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryManagementService {

    @Autowired
    private  ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;


    public int addProduct(Product product) {
        if (product.getCategoryId() != null) {
            Category category = categoryRepo.findById(product.getCategoryId()).orElse(null);
            product.setCategory(category);
        }
        productRepo.save(product);
        return product.getProductId();
    }

    public int addCategory(Category category) {
        categoryRepo.save(category);
        return category.getCategoryId();
    }

    public List<Product> getProducts(int productId, int categoryId) {
        return null;
    }

    public List<Category> getCategory(int categoryId) {
        return  null;
    }

    public void updateProduct(int productId, String productName, int categoryId, double price, int quantity) {
    }

    public void updateCategory(int categoryId, String name) {
    }

    public void deleteProduct(int productId) {
        
    }

    public void deleteCategory(int categoryId) {

    }

    public void orderProduct(int productId, int quantity, int userId) {
    }

    public void reStockProduct(int productId, int quantity, int userId) {
        
    }
}
