package com.assesment2.inventoryManagement.service;

import com.assesment2.inventoryManagement.InMemoryCache.InMemoryCache;
import com.assesment2.inventoryManagement.model.Category;
import com.assesment2.inventoryManagement.model.OrderTable;
import com.assesment2.inventoryManagement.model.Product;
import com.assesment2.inventoryManagement.model.User;
import com.assesment2.inventoryManagement.repository.*;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.*;

@Service
public class InventoryManagementService {

    private final ProductRepo productRepo;

    private final CategoryRepo categoryRepo;

    private final UserRepo userRepo;

    private final OrderRepo orderRepo;

    public InventoryManagementService(ProductRepo productRepo, CategoryRepo categoryRepo, UserRepo userRepo, OrderRepo orderRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
    }

    private final Logger logger = Logger.getLogger(InventoryManagementService.class);

    @Transactional
    public int addProduct(Product product) {
        // Validate product name
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }

        // Validate category ID
        if (product.getCategoryId() == null) {
            throw new IllegalArgumentException("Category ID is required");
        }

        // Validate price
        if (product.getPrice() == null) {
            throw new IllegalArgumentException("Price is required");
        }

        // Validate quantity
        if (product.getQuantity() == null) {
            throw new IllegalArgumentException("Quantity is required");
        }


        // Check if product name is unique
        if (productRepo.existsByProductName(product.getProductName())) {
            throw new IllegalArgumentException("Product name already exists");
        }

        // Validate category existence
        if (!categoryRepo.existsByCategoryId(product.getCategoryId())) {
            throw new IllegalArgumentException("Invalid category ID");

        }

        // Validate price and quantity
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Enter a valid price");
        }

        if (product.getQuantity() <= 0) {
            throw new IllegalArgumentException("Enter a valid quantity");
        }

        // Save the product and return its ID
        Product savedProduct= productRepo.save(product);
        return savedProduct.getProductId();
    }


    public Page<Product> getProducts(Integer productId, Integer categoryId, Integer pageNo) {
        if (pageNo < 1) {
            throw new IllegalArgumentException("Page number must be a positive integer");
        }

        pageNo -= 1;
        int pageSize = 25;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        if (productId == null && categoryId == null) {
            return productRepo.findAll(pageable);
        }

        if (categoryId == null) {
            Product product = InMemoryCache.getProductById(productId);
            if (product == null) {
                product = productRepo.findByProductId(productId);
                if (product == null) {
                    throw new NoSuchElementException("Invalid Product ID");
                }
                InMemoryCache.putProduct(product);
            }
            return new PageImpl<>(Collections.singletonList(product), pageable, 1);
        }

        if (productId == null) {
            if (!categoryRepo.existsById(categoryId)) {
                throw new NoSuchElementException("Invalid category ID");
            }

            boolean productPresent = productRepo.existsAllByCategoryId(categoryId);
            if (!productPresent) {
                throw new NoSuchElementException("No products in this category");
            }

            return productRepo.findAllByCategoryId(categoryId, pageable);
        }

        List<Product> products = productRepo.findAllByProductIdAndCategoryId(productId, categoryId);
        return new PageImpl<>(products, pageable, products.size());
    }


    @Transactional

    public void deleteProduct(Integer productId) {
        if (!productRepo.existsByProductId(productId)) {
            throw new NoSuchElementException("Invalid product ID");
        } else {

            try {
                Product product = InMemoryCache.getProductById(productId);
                productRepo.deleteByProductId(productId);
                if (product == null) {
                    InMemoryCache.deleteProduct(productId);
                }
            } catch (Exception E) {
                logger.error("Error deleting product: " + E.getLocalizedMessage());
            }
        }
    }

    @Transactional

    public void updateProduct(Integer productId, String productName, Integer categoryId, Double price, Integer quantity) {
        if (productId == null || !productRepo.existsByProductId(productId)) {
            throw new NoSuchElementException("Invalid Product ID");
        } else {
            if (productName == null && categoryId == null && price == null && quantity == null) {
                throw new NoSuchElementException("Enter the details to be updated");
            }
            Product product = productRepo.findByProductId(productId);
            if (productName != null) {
                if (productRepo.existsByProductName(productName)) {
                    throw new NoSuchElementException("Product with name " + productName + " exists");
                } else {
                    product.setProductName(productName);
                }
            }

            if (categoryId != null) {
                if (!categoryRepo.existsById(categoryId)) {
                    throw new NoSuchElementException("Invalid Category ID");
                } else {
                    if (!Objects.equals(product.getCategoryId(), categoryId)) {
                        product.setCategoryId(categoryId);
                    } else {
                        throw new NoSuchElementException("Product is under the same category");
                    }
                }
            }

            if (price != null) {
                product.setPrice(price);
            }

            if (quantity != null) {
                product.setQuantity(product.getQuantity() + quantity);
            }
            productRepo.save(product);
            InMemoryCache.updateProduct(product);
        }
    }

    @Transactional
    public int addCategory(Category category) {
        if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
            throw new NoSuchElementException("Category name must be valid and not null");
        } else {
            if (categoryRepo.existsByCategoryName(category.getCategoryName())||categoryRepo.existsByCategoryId(category.getCategoryId())) {
                throw new NoSuchElementException("Category already exists");
            } else {
                categoryRepo.save(category);
            }
        }
        return category.getCategoryId();
    }

    public List<Category> getCategory(Integer categoryId) {
        List<Category> categories = new ArrayList<>();

        if (categoryId == null) {
            categories = categoryRepo.findAll();
        } else {
            Category category = InMemoryCache.getCategory(categoryId);

            if (category != null) {
                categories.add(category);
            } else {
                if (categoryRepo.existsById(categoryId)) {
                    category = categoryRepo.findByCategoryId(categoryId);
                    InMemoryCache.putCategory(category);
                    categories.add(category);
                } else {
                    throw new NoSuchElementException("Invalid Category ID");
                }
            }
        }

        return categories;
    }


    public void updateCategory(Integer categoryId, String name) {
        if (categoryId == null || !categoryRepo.existsById(categoryId)) {
            throw new NoSuchElementException("Invalid category ID");
        } else {
            Category category = categoryRepo.findByCategoryName(name);
            if (category != null) {
                throw new NoSuchElementException("Category already exists");
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
        if (categoryId == null || !categoryRepo.existsById(categoryId)) {
            throw new NoSuchElementException("Invalid category ID");
        } else {
            if (productRepo.existsAllByCategoryId(categoryId)) {
                throw new NoSuchElementException("Cannot delete category as there are products associated to this category");
            } else {
                categoryRepo.deleteById(categoryId);
                InMemoryCache.deleteCategory(categoryId);
            }
        }
    }


    @Transactional
    public void orderProduct(Integer productId, Integer quantity, Integer userId) {


            if (productId == null || !productRepo.existsByProductId(productId)) {
                throw new NoSuchElementException("Invalid Product ID");
            } else {
                if (quantity < 0) {
                    throw new NoSuchElementException("Quantity should be greater than 0");
                } else {
                    Optional<User> users = userRepo.findById(userId);

                    if (users.isPresent()) {
                        User user = users.get();
                        String userRole = user.getRole();

                        if (userRole.equalsIgnoreCase("buyer")) {
                            OrderTable order = new OrderTable();
                            Product product = productRepo.findByProductId(productId);

                            Integer productQuantity = product.getQuantity();
                            if (productQuantity - quantity >= 0) {
                                order.setProductId(productId);
                                order.setUserId(userId);
                                order.setOrderDate(Instant.now());

                                product.setQuantity(productQuantity - quantity);

                                orderRepo.save(order);
                                productRepo.save(product);

                                Product productCache = InMemoryCache.getProductById(productId);
                                if (productCache != null) {
                                    InMemoryCache.updateProductQuantity(product);
                                }
                            } else {
                                throw new NoSuchElementException("Insufficient product quantity. Available quantity: " + productQuantity);
                            }
                        } else {
                            throw new NoSuchElementException("Not a valid user");
                        }
                    } else {
                        throw new NoSuchElementException("Invalid User ID");
                    }

                }
            }

    }

    @Transactional
    public void reStockProduct(Integer productId, Integer quantity, Integer userId) {

            if (productId == null || !productRepo.existsByProductId(productId)) {
                throw new NoSuchElementException("Invalid Product ID");
            } else {
                if (quantity < 0) {
                    throw new NoSuchElementException("Quantity should be greater than 0");
                } else {
                    Optional<User> users = userRepo.findById(userId);

                    if (users.isPresent()) {
                        User user = users.get();
                        String userRole = user.getRole();
                        Product product = productRepo.findByProductId(productId);

                        if (userRole.equalsIgnoreCase("seller")) {
                            product.setQuantity(product.getQuantity() + quantity);
                            productRepo.save(product);

                            Product productCache = InMemoryCache.getProductById(productId);
                            if (productCache != null) {
                                InMemoryCache.updateProductQuantity(product);
                            }
                        } else {
                            throw new NoSuchElementException("Not a valid user");

                        }
                    } else {
                        throw new NoSuchElementException("Invalid User ID");
                    }
                }
            }

    }
}
