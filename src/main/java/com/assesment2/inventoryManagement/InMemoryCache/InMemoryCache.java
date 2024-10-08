package com.assesment2.inventoryManagement.InMemoryCache;

import com.assesment2.inventoryManagement.model.Category;
import com.assesment2.inventoryManagement.model.Product;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCache {
    private static final int MAX_CACHE_SIZE = 15;
    private static final ConcurrentHashMap<Integer, Product> productCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Long> productTimestamps = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Category> categoryCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Long> categoryTimestamps = new ConcurrentHashMap<>();

    public static void putProduct(Product product) {

        if(productCache.containsKey(product.getProductId())){
            productCache.remove(product.getProductId());
        } else if (productCache.size() >= MAX_CACHE_SIZE) {
            Integer oldestProductId = getOldestProductId();
            productCache.remove(oldestProductId);
            productTimestamps.remove(oldestProductId);
        }
        productCache.put(product.getProductId(), product);
        productTimestamps.put(product.getProductId(),System.currentTimeMillis());

    }

    public static void updateProduct(Product product) {
        putProduct(product);
    }

    public static Product getProductById(Integer productId) {
        Product product = productCache.get(productId);
        if(product != null){
            productTimestamps.put(productId, System.currentTimeMillis());
        }
        return product;
    }

    public static void deleteProduct(Integer productId) {
        Product product = productCache.get(productId);
        if (product != null) {
            productCache.remove(productId);
        }
    }


    public static void putCategory(Category category) {

        if(categoryCache.containsKey(category.getCategoryId())){
            categoryCache.remove(category.getCategoryId());
        } else if (categoryCache.size() >= MAX_CACHE_SIZE) {
            Integer oldestCategoryId = getOldestCategoryId();
            categoryCache.remove(oldestCategoryId);
            categoryTimestamps.remove(oldestCategoryId);
        }
        categoryCache.put(category.getCategoryId(), category);
        categoryTimestamps.put(category.getCategoryId(),System.currentTimeMillis());

    }

    public static Category getCategory(Integer categoryId) {
        Category category = categoryCache.get(categoryId);
        if(category != null){
            categoryTimestamps.put(categoryId, System.currentTimeMillis());
        }
        return category;
    }

    public static void updateCategory(Category category) {
        putCategory(category);
    }

    public static void deleteCategory(Integer categoryId) {
        Category category = categoryCache.get(categoryId);
        if (category != null) {
            categoryCache.remove(categoryId);
        }
    }

    public static void updateProductQuantity(Product product) {
        putProduct(product);
    }

    private static Integer getOldestProductId() {
        Integer oldestProductId = null;
        Long oldestTimeStamp = Long.MAX_VALUE;

        for(Map.Entry<Integer,Long> entry : productTimestamps.entrySet())
        {
            if(entry.getValue()< oldestTimeStamp){
                oldestTimeStamp = entry.getValue();
                oldestProductId = entry.getKey();
            }
        }
        return oldestProductId;
    }

    private static Integer getOldestCategoryId() {
        Integer oldestCategoryId = null;
        Long oldestTimeStamp = Long.MAX_VALUE;

        for(Map.Entry<Integer,Long> entry : categoryTimestamps.entrySet())
        {
            if(entry.getValue()< oldestTimeStamp){
                oldestTimeStamp = entry.getValue();
                oldestCategoryId = entry.getKey();
            }
        }
        return oldestCategoryId;
    }
}