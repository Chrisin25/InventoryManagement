package com.assesment2.inventoryManagement.service;

import com.assesment2.inventoryManagement.InMemoryCache.InMemoryCache;
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

        if (!productRepo.findAllByProductName(product.getProductName()).isEmpty()) {
            throw new IllegalArgumentException("Product name already exists");
        }
        //valid category
        else if (categoryRepo.findAllByCategoryId(product.getCategoryId()).isEmpty()) {
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
            List<Product> products = InMemoryCache.getAllProducts();
            if(!products.isEmpty())
            {
                productRepo.save(product);
                InMemoryCache.putProduct(product);
                return product.getProductId();
            }
            else {
                productRepo.save(product);
                return product.getProductId();
            }
        }

    }

    public List<Product> getProducts(Integer productId,Integer categoryId){
        List<Product> products = new ArrayList<>();
        Product product;
        if(productId==null && categoryId==null){
            products = InMemoryCache.getAllProducts();
            if(products.isEmpty()) {
                products = productRepo.findAll();
                InMemoryCache.putAllProducts(products);
            }
        }
        else if(categoryId==null){
            product =InMemoryCache.getProduct(productId);
            if(product==null) {
                System.out.println("product empty in cache");
                product = productRepo.findByProductId(productId);
                if(product==null) {
                    throw new IllegalArgumentException("Invalid Product ID");
                }
                else {
                    InMemoryCache.putProduct(product);
                    products.add(product);
                }
            }
            else {
                products.add(product);
            }
        }
        else if(productId==null){
            if(!categoryRepo.existsById(categoryId))
                throw new IllegalArgumentException("Invalid category ID");
            products=InMemoryCache.getProductsByCategoryId(categoryId);
            if(products==null) {
                products = productRepo.findAllByCategoryId(categoryId);
                if(products.isEmpty())
                    throw new IllegalArgumentException("No products found");
                InMemoryCache.putAllProducts(products);
            }

        }
        else{
            products=productRepo.findAllByProductIdAndCategoryId(productId,categoryId);
        }
        return products;
    }

    @Transactional
    public void deleteProduct(Integer productId){
        if(productRepo.findAllByProductId(productId).isEmpty()) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        else {
            try {
                List<Product> products = InMemoryCache.getAllProducts();
                if(!products.isEmpty())
                {
                    productRepo.deleteByProductId(productId);
                    InMemoryCache.deleteProduct(productId);
                }
                else {
                    productRepo.deleteByProductId(productId);
                }


            } catch (Exception E) {
                System.out.println("cannot delete product:" + E.getLocalizedMessage());
            }
        }
    }

    @Transactional
    public void updateProduct(Integer productId,String productName,Integer categoryId,Double price,Integer quantity){
        if(productId==null || productRepo.findAllByProductId(productId).isEmpty()){
            throw new IllegalArgumentException("Invalid Product ID");

        }
        else{
            String flag="no";
            if(productName==null && categoryId==null && price==null && quantity==null)
            {
                throw new IllegalArgumentException("Enter the details to be updated");
            }
            Product product=productRepo.findAllByProductId(productId).get(0);
            if(productName!=null) {
                if(productRepo.findAllByProductName(productName).isEmpty()) {
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
                        flag = "yes";
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
            InMemoryCache.updateProduct(product,flag);
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
                List<Category> categories = InMemoryCache.getAllCategories();
                if(!categories.isEmpty()) {
                    InMemoryCache.putCategory(category);
                }
            }
            return category.getCategoryId();
        }
    }

    public  List<Category> getCategory(Integer categoryId) {
        List<Category> categories;
        if(categoryId == null) {
            categories = InMemoryCache.getAllCategories();
            if(categories.isEmpty()) {
                categories = categoryRepo.findAll();
                InMemoryCache.putAllCategories(categories);
            }
        } else {
            Category category = InMemoryCache.getCategory(categoryId);
            if(category != null) {
                categories = new ArrayList<>();
                categories.add(category);
            } else {
                if(categoryRepo.existsById(categoryId)) {
                    categories = categoryRepo.findAllByCategoryId(categoryId);
                    InMemoryCache.putAllCategories(categories);
                } else {
                    throw new IllegalArgumentException("Invalid Category ID");
                }
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
                Category categoryCache = InMemoryCache.getCategory(categoryId);
                if (categoryCache != null) {
                    InMemoryCache.updateCategory(newCategory);
                }
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
                    InMemoryCache.deleteCategory(categoryId);
                }
        }
    }

    @Transactional
    public void orderProduct(Integer productId, Integer quantity, Integer userId) {
        if(productId==null || productRepo.findAllByProductId(productId).isEmpty()){
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
                         Product product = productRepo.findByProductId(productId);
                         Integer productQuantity = product.getQuantity();
                         if(productQuantity - quantity >= 0) {
                             order.setProductId(productId);
                             order.setUserId(userId);
                             order.setOrderDate(Instant.now());

                             product.setQuantity(productQuantity - quantity);

                             orderRepo.save(order);
                             productRepo.save(product);
                             Product productCache = InMemoryCache.getProductById(productId);
                             if(productCache!=null)
                             {
                                 InMemoryCache.updateProductQuantity(product);
                             }
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
        if(productId==null || productRepo.findAllByProductId(productId).isEmpty()){
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
                    Product product = productRepo.findByProductId(productId);
                    if(userRole.equalsIgnoreCase("seller"))
                    {
                        product.setQuantity(product.getQuantity() + quantity);
                        productRepo.save(product);
                        Product productCache = InMemoryCache.getProductById(productId);
                        if(productCache!=null)
                        {
                            InMemoryCache.updateProductQuantity(product);
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
}
