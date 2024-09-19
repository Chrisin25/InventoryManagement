package com.assesment2.inventoryManagement.service;

import com.assesment2.inventoryManagement.model.Product;
import com.assesment2.inventoryManagement.repository.CategoryRepo;
import com.assesment2.inventoryManagement.repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class InventoryManagementService {
    @Autowired
    ProductRepo productRepo;
    @Autowired
    CategoryRepo categoryRepo;
    public int addProduct(Product product) {
        //unique product name
        if (!productRepo.findAllByProductName(product.getProductName()).isEmpty()) {
            System.out.println("product already exist");
        }
        //valid category
        else if (categoryRepo.findAllByCategoryId(product.getCategoryId()).isEmpty()) {
            System.out.println("invalid category id");
        }
        // check price and qty is valid
        else if (product.getPrice() <= 0) {
            System.out.println("enter valid price");
        }
        else if (product.getQuantity() <= 0) {
            System.out.println("enter valid quantity");
        }
        else{
            productRepo.save(product);
        }
        return product.getProductId();
    }
    public List<Product> getProducts(Integer productId,Integer categoryId){
        List<Product> products;
        if(productId==null && categoryId==null){
            products=productRepo.findAll();
        }
        else if(categoryId==null){
            products=productRepo.findAllByProductId(productId);
        }
        else if(productId==null){
            products=productRepo.findAllByCategoryId(categoryId);
        }
        else{
            products=productRepo.findAllByProductIdAndCategoryId(productId,categoryId);
        }
        return products;
    }
    @Transactional
    public void deleteProduct(int productId){
        try{
            productRepo.deleteByProductId(productId);
        }
        catch(Exception E){
            System.out.println("cannot delete product:"+E.getLocalizedMessage());
        }
    }
    public void updateProduct(Integer productId,String productName,Integer categoryId,Double price,Integer quantity){
        if(productId==null || productRepo.findAllByProductId(productId).isEmpty()){
            System.out.println("cannot update");
        }
        else{
            Product product=productRepo.findAllByProductId(productId).get(0);
            if(productName!=null && productRepo.findAllByProductName(productName).isEmpty()){
                product.setProductName(productName);
            }
            if(categoryId!=null && categoryRepo.existsById(categoryId)){
                product.setCategoryId(categoryId);
            }
            if(price!=null && price>0){
                product.setPrice(price);
            }
            if(quantity!=null){
                if(quantity>0){
                    product.setQuantity(product.getQuantity()+quantity);
                }
                else if(product.getQuantity()+quantity>=0){
                    product.setQuantity(product.getQuantity()+quantity);
                }
                else{
                    System.out.println("product quantity cannot be updated");
                }
            }
            productRepo.save(product);
        }

    }
}
