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
    @GetMapping("/product")
    public List<Product> getProducts(
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) Integer categoryId) {
        return inventoryManagementService.getProducts(productId,categoryId);
    }

    //get category details
    @GetMapping("/category")
    public List<Category> getCategory(
            @RequestParam(required = false) Integer category_id){
        return inventoryManagementService.getCategory(category_id);
    }

    //change product details
    @PutMapping("/product")
    public ResponseEntity<ResponseMessageForUpdate> updateProduct(
            @RequestParam int productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer quantity) {

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
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Successfully deleted product");

        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    //delete a category using its id
    @DeleteMapping("/category")
    public ResponseEntity<ResponseMessage> deleteCategory(
            @RequestParam int categoryId
    ){
        inventoryManagementService.deleteCategory(categoryId);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Successfully deleted category");

        return new ResponseEntity<>(responseMessage,HttpStatus.OK);

    }

    //sell or buy a product
    @PutMapping("/orders")
    public ResponseEntity<ResponseMessage> orderProduct(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            @RequestParam Integer userId ){
        inventoryManagementService.orderProduct(productId,quantity,userId);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Successfully ordered");

        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    //restock a product
    @PutMapping("/restock")
    public ResponseEntity<ResponseMessage> reStockProduct(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            @RequestParam Integer userId ){
        inventoryManagementService.reStockProduct(productId,quantity,userId);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Successfully restocked");

        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }







}
