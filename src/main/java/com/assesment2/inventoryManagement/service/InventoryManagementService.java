package com.assesment2.inventoryManagement.service;

import com.assesment2.inventoryManagement.model.Category;
import com.assesment2.inventoryManagement.model.OrderTable;
import com.assesment2.inventoryManagement.model.Product;
import com.assesment2.inventoryManagement.model.User;
import com.assesment2.inventoryManagement.repository.CategoryRepo;
import com.assesment2.inventoryManagement.repository.OrderRepo;
import com.assesment2.inventoryManagement.repository.ProductRepo;
import com.assesment2.inventoryManagement.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class InventoryManagementService {
    @Autowired
    ProductRepo productRepo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    OrderRepo orderRepo;

    @Transactional
    public int addProduct(Product product) {
        //unique product name

        if (productRepo.existsByProductName(product.getProductName())) {
            throw new IllegalArgumentException("Product name already exists");
        }
        //valid category
        else if (!categoryRepo.existsByCategoryId(product.getCategoryId())) {
            throw new IllegalArgumentException("Invalid category id");
        }
        // check price and qty is valid
        else if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Enter a valid price");
        }
        else if (product.getQuantity() <= 0) {
            throw new IllegalArgumentException("Enter valid quantity");
        }
        else{
            productRepo.save(product);
            return product.getProductId();
        }

    }

    public List<Product> getProducts(Integer productId,Integer categoryId){
        List<Product> products;
        if(productId==null && categoryId==null){
             products = productRepo.findAll();
        }
        else if(categoryId==null){

            products =productRepo.findByProductId(productId);

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
    public void deleteProduct(Integer productId){
        if(!productRepo.existsByProductId(productId)) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        else {
            try {
                productRepo.deleteByProductId(productId);

            } catch (Exception E) {
                System.out.println("cannot delete product:" + E.getLocalizedMessage());
            }
        }
    }

    @Transactional
    public void updateProduct(Integer productId,String productName,Integer categoryId,Double price,Integer quantity){
        if(productId==null || !productRepo.existsByProductId(productId)){
            throw new IllegalArgumentException("Invalid Product ID");

        }
        else{
            if(productName==null && categoryId==null && price==null && quantity==null)
            {
                throw new IllegalArgumentException("Enter the details to be updated");
            }
            Product product=productRepo.findAllByProductId(productId).get(0);    //
            if(productName!=null) {
                if(!productRepo.existsByProductName(productName)) {
                    product.setProductName(productName);
                }
                else
                {
                    throw new IllegalArgumentException("Product with name " + productName + " exists" );
                }
            }

            if(categoryId!=null) {
                if (categoryRepo.existsById(categoryId)) {
                    if(!Objects.equals(product.getCategoryId(), categoryId)) {
                        product.setCategoryId(categoryId);
                    }
                    else
                    {
                        throw new IllegalArgumentException("Product is under the same category");
                    }
                }
                else
                {
                    throw new IllegalArgumentException("Invalid Category ID");
                }
            }

            if(price!=null) {
                if (price > 0) {
                    product.setPrice(price);
                }
                else {
                    throw new IllegalArgumentException("Price should be a valid positive number");
                }
            }

            if(quantity!=null){
                if(quantity>0){
                    product.setQuantity(product.getQuantity()+quantity);
                }
                else if(product.getQuantity()+quantity>=0){
                    product.setQuantity(product.getQuantity()+quantity);
                }
                else{
                    throw new IllegalArgumentException("Product quantity cannot be updated");
                }
            }
            productRepo.save(product);
        }

    }

    @Transactional
    public int addCategory(Category category) {
        if(category.getCategoryName()==null || category.getCategoryName().isEmpty())
        {
            throw new IllegalArgumentException("Category name must be valid and not null");
        }
        else
        {
            Category categoryName = categoryRepo.findByCategoryName(category.getCategoryName());
            if (categoryName != null) {
                throw new IllegalArgumentException("Category already exists");
            } else {
                categoryRepo.save(category);
            }
            return category.getCategoryId();
        }
    }

    public List<Category> getCategory(Integer categoryId) {
        List<Category> categories;
        if(categoryId==null)
        {
            categories = categoryRepo.findAll();
        }
        else
        {
            if(categoryRepo.existsById(categoryId)) {
                categories = categoryRepo.findAllByCategoryId(categoryId);
            }
            else
            {
                throw new IllegalArgumentException("Invalid Category ID");
            }
        }
        return categories;
    }

    public void updateCategory(Integer categoryId, String name) {
        if (categoryId==null || !categoryRepo.existsById(categoryId))
        {
            throw new IllegalArgumentException("Invalid category ID");
        }
        else {
            Category category = categoryRepo.findByCategoryName(name);
            if (category != null) {
                throw new IllegalArgumentException("Category already exists");
            } else {
                Category newCategory = categoryRepo.findAllByCategoryId(categoryId).get(0);
                newCategory.setCategoryName(name);
                categoryRepo.save(newCategory);
            }
        }
    }

    @Transactional
    public void deleteCategory(Integer categoryId) {
        if (categoryId==null || !categoryRepo.existsById(categoryId))
        {
            throw new IllegalArgumentException("Invalid category ID");
        }
        else
        {
                List<Product> products = productRepo.findAllByCategoryId(categoryId);
                if(!products.isEmpty())
                {
                    throw new IllegalArgumentException("Cannot delete category as there are products associated to this category");
                }
                else
                {
                    categoryRepo.deleteById(categoryId);
                }
        }
    }

    @Transactional
    public void orderProduct(Integer productId, Integer quantity, Integer userId) {
        if(productId==null || !productRepo.existsByProductId(productId)){
            throw new IllegalArgumentException("Invalid Product ID");
        }
        else
        {
            if(quantity<0)
            {
                throw new IllegalArgumentException("Quantity should be greater than 0");
            }
            else
            {
                Optional<User> users = userRepo.findById(userId);
                if(users.isPresent())
                {
                     User user = users.get();
                     String userRole = user.getRole();

                     if(userRole.equalsIgnoreCase("buyer"))
                     {
                         OrderTable order = new OrderTable();
                         Product product = productRepo.findByProductId(productId).get(0);
                         Integer productQuantity = product.getQuantity();
                         if(productQuantity - quantity >= 0) {
                             order.setProductId(productId);
                             order.setUserId(userId);
                             order.setOrderDate(Instant.now());

                             product.setQuantity(productQuantity - quantity);

                             orderRepo.save(order);
                         }
                         else {
                             throw new IllegalArgumentException("Insufficient product quantity. Available quantity: " + productQuantity);
                         }
                     }
                     else
                     {
                         throw new IllegalArgumentException("Not a valid user");
                     }
                }
                else
                {
                    throw new IllegalArgumentException("Invalid User ID");
                }
            }
        }
    }

    public void reStockProduct(Integer productId, Integer quantity, Integer userId) {
        if(productId==null || !productRepo.existsByProductId(productId)){
            throw new IllegalArgumentException("Invalid Product ID");
        }
        else
        {
            if(quantity<0)
            {
                throw new IllegalArgumentException("Quantity should be greater than 0");
            }
            else
            {
                Optional<User> users = userRepo.findById(userId);
                if(users.isPresent())
                {
                    User user = users.get();
                    String userRole = user.getRole();
                    Product product = productRepo.findByProductId(productId).get(0);
                    if(userRole.equalsIgnoreCase("seller"))
                    {
                        product.setQuantity(product.getQuantity() + quantity);
                        productRepo.save(product);
                    }
                    else
                    {
                        throw new IllegalArgumentException("Not a valid user");
                    }
                }
                else
                {
                    throw new IllegalArgumentException("Invalid User ID");
                }
            }
        }
    }
}
