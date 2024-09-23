package com.assesment2.inventoryManagement.InMemoryCache;

import com.assesment2.inventoryManagement.model.Category;
import com.assesment2.inventoryManagement.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryCache {
    private static final Map<Integer,Product> productCache = new HashMap<>();
    private static final Map<Integer, Category> categoryCache = new HashMap<>();
    private static final Map<Integer, List<Product>> categoryIdCache = new HashMap<>();

    public static void putProduct(Product product)
    {
        productCache.put(product.getProductId(),product);
        if (categoryIdCache.containsKey(product.getCategoryId())) {
            categoryIdCache.get(product.getCategoryId()).add(product);
        } else {
            List<Product> productList = new ArrayList<>();
            productList.add(product);
            categoryIdCache.put(product.getCategoryId(), productList);
        }
    }

    public static void updateProduct(Product product,String choice)
    {
        productCache.put(product.getProductId(),product);
        if(choice.equalsIgnoreCase("no")) {
            categoryIdCache.get(product.getCategoryId()).remove(product);
            categoryIdCache.get(product.getCategoryId()).add(product);
        }
        else
        {
            Integer categoryId = product.getCategoryId();

            categoryIdCache.get(categoryId).remove(product);
            categoryIdCache.get(product.getCategoryId()).add(product);
        }
    }

    public static Product getProduct(Integer productId)
    {
        return productCache.get(productId);
    }

    public static List<Product> getAllProducts() {
        return new ArrayList<>(productCache.values());
    }

    public static void putCategory(Category category) {
        categoryCache.put(category.getCategoryId(), category);
        categoryIdCache.putIfAbsent(category.getCategoryId(), new ArrayList<>());
    }

    public static Category getCategory(Integer categoryId)
    {
        return categoryCache.get(categoryId);
    }

    public static void putAllProducts(List<Product> products) {
        for (Product product : products) {
            productCache.put(product.getProductId(), product);
            if (categoryIdCache.containsKey(product.getCategoryId())) {
                categoryIdCache.get(product.getCategoryId()).add(product);
            } else {
                List<Product> productList = new ArrayList<>();
                productList.add(product);
                categoryIdCache.put(product.getCategoryId(), productList);
            }
        }
    }

    public static Product getProductById(Integer productId) {
        return productCache.get(productId);
    }

    public static List<Product> getProductsByCategoryId(Integer categoryId) {
        return categoryIdCache.get(categoryId);
    }

    public static void deleteProduct(Integer productId) {
        Product product = productCache.get(productId);
        if (product != null) {
            productCache.remove(productId);
            if (categoryIdCache.containsKey(product.getCategoryId())) {
                categoryIdCache.get(product.getCategoryId()).remove(product);
            }
        }
    }


    public static List<Category> getAllCategories() {
        return new ArrayList<>(categoryCache.values());
    }

    public static void putAllCategories(List<Category> categories) {
        for (Category category : categories) {
            categoryCache.put(category.getCategoryId(), category);
        }
    }

    public static void updateCategory(Category category) {
        categoryCache.put(category.getCategoryId(), category);
    }

    public static void deleteCategory(Integer categoryId) {
        Category category = categoryCache.get(categoryId);
        if (category != null) {
            categoryCache.remove(categoryId);
            categoryIdCache.remove(categoryId);
        }
    }


    public static void updateProductQuantity(Product product) {

        productCache.put(product.getProductId(), product);


        if (categoryIdCache.containsKey(product.getCategoryId())) {
            List<Product> productList = categoryIdCache.get(product.getCategoryId());
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getProductId().equals(product.getProductId())) {
                    productList.set(i, product);
                    break;
                }
            }
        }
    }
}