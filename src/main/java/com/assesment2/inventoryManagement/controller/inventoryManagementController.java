package com.assesment2.inventoryManagement.controller;


import com.assesment2.inventoryManagement.model.Category;
import com.assesment2.inventoryManagement.model.Product;
import com.assesment2.inventoryManagement.response.ResponseMessage;
import com.assesment2.inventoryManagement.response.ResponseMessageForCreate;
import com.assesment2.inventoryManagement.response.ResponseMessageForUpdate;
import com.assesment2.inventoryManagement.service.InventoryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")

public class inventoryManagementController {

    @Autowired
    private  InventoryManagementService inventoryManagementService;

    //add a product
    @PostMapping("/product")
    public ResponseEntity<ResponseMessageForCreate> addProduct(@RequestBody Product product)
    {
        int id;
        id = inventoryManagementService.addProduct(product);
        return new ResponseEntity<>(new ResponseMessageForCreate("Successfully created",id), HttpStatus.CREATED);
    }

    //add a category
    @PostMapping("/category")
    public ResponseEntity<ResponseMessageForCreate> addCategory(@RequestBody Category category)
    {
        int id;
        id = inventoryManagementService.addCategory(category);
        return new ResponseEntity<>(new ResponseMessageForCreate("Successfully created",id), HttpStatus.CREATED);
    }

  //get product details
    @GetMapping("/products")
    public List<Product> getProducts(
            @RequestParam(required = false) int productId,
            @RequestParam(required = false) int categoryId) {
        return inventoryManagementService.getProducts(productId,categoryId);
    }

    //get category details
    @GetMapping("/category")
    public List<Category> getCategory(
            @RequestParam(required = false) int category_id){
        return inventoryManagementService.getCategory(category_id);
    }

    //change product details
    @PutMapping("/product")
    public ResponseEntity<ResponseMessageForUpdate> updateProduct(
            @RequestParam int productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) int categoryId,
            @RequestParam(required = false) double price,
            @RequestParam(required = false) int quantity) {

        inventoryManagementService.updateProduct(productId,productName,categoryId,price,quantity);
        return new ResponseEntity<>(new ResponseMessageForUpdate("Successfully updated product details"),HttpStatus.OK);
    }

    //change category name
    @PutMapping("/category")
    public ResponseEntity<ResponseMessageForUpdate> updateCategory(
            @RequestParam int categoryId,
            @RequestParam String name
    ){

        inventoryManagementService.updateCategory(categoryId,name);
        return new ResponseEntity<>(new ResponseMessageForUpdate("Successfully updated category name"),HttpStatus.OK);
    }

    //delete a product using its id
    @DeleteMapping("/product")
    public ResponseEntity<ResponseMessage> deleteProduct(
            @RequestParam int productId
    ){
        inventoryManagementService.deleteProduct(productId);
        return new ResponseEntity<>(new ResponseMessage("Successfully deleted product"),HttpStatus.OK);
    }

    //delete a category using its id
    @DeleteMapping("/product")
    public ResponseEntity<ResponseMessage> deleteCategory(
            @RequestParam int categoryId
    ){
        inventoryManagementService.deleteCategory(categoryId);
        return new ResponseEntity<>(new ResponseMessage("Successfully deleted category"),HttpStatus.OK);
    }

    //sell or buy a product
    @PutMapping("/orders")
    public ResponseEntity<ResponseMessage> orderProduct(
            @RequestParam int productId,
            @RequestParam int quantity,
            @RequestParam int userId ){
        inventoryManagementService.orderProduct(productId,quantity,userId);
        return new ResponseEntity<>(new ResponseMessage("Successfully ordered"),HttpStatus.OK);
    }

    //restock a product
    @PutMapping("/orders")
    public ResponseEntity<ResponseMessage> reStockProduct(
            @RequestParam int productId,
            @RequestParam int quantity,
            @RequestParam int userId ){
        inventoryManagementService.reStockProduct(productId,quantity,userId);
        return new ResponseEntity<>(new ResponseMessage("Successfully restocked"),HttpStatus.OK);
    }







}
