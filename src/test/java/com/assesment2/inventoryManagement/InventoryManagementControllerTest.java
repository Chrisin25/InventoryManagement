package com.assesment2.inventoryManagement;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.assesment2.inventoryManagement.controller.inventoryManagementController;
import com.assesment2.inventoryManagement.handlers.GlobalExceptionHandler;
import com.assesment2.inventoryManagement.model.Category;
import com.assesment2.inventoryManagement.model.Product;
import com.assesment2.inventoryManagement.response.ResponseMessage;
import com.assesment2.inventoryManagement.service.InventoryManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryManagementControllerTest {

    @InjectMocks
    private inventoryManagementController inventoryManagementController;

    @MockBean
    private InventoryManagementService inventoryManagementService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryManagementController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddProduct() throws Exception {
        Product product = new Product();
        product.setProductName("Test Product");
        product.setCategoryId(1);
        product.setPrice(100.0);
        product.setQuantity(10);

        when(inventoryManagementService.addProduct(any(Product.class))).thenReturn(1);

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Successfully created")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testAddCategory() throws Exception {
        Category category = new Category();
        category.setCategoryName("Test Category");

        when(inventoryManagementService.addCategory(any(Category.class))).thenReturn(1);

        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Successfully created")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void testGetProducts_NoParams_ReturnsAllProducts() throws Exception {
        Page<Product> mockPage = new PageImpl<>(Arrays.asList(new Product(), new Product()), PageRequest.of(0, 25), 2);
        when(inventoryManagementService.getProducts(null, null, 1)).thenReturn(mockPage);

        mockMvc.perform(get("/api/v1/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetProducts_ByProductId_ReturnsSingleProduct() throws Exception {
        Product mockProduct = new Product();
        when(inventoryManagementService.getProducts(1, null, 1)).thenReturn(new PageImpl<>(Collections.singletonList(mockProduct)));

        mockMvc.perform(get("/api/v1/product").param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetProducts_ByCategoryId_ReturnsProducts() throws Exception {
        Product mockProduct = new Product();
        Page<Product> mockPage = new PageImpl<>(Collections.singletonList(mockProduct), PageRequest.of(0, 25), 1);
        when(inventoryManagementService.getProducts(null, 1, 1)).thenReturn(mockPage);

        mockMvc.perform(get("/api/v1/product").param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetCategory_ByCategoryId_ReturnsCategory() throws Exception {
        Category mockCategory = new Category();
        when(inventoryManagementService.getCategory(1)).thenReturn(Collections.singletonList(mockCategory));

        mockMvc.perform(get("/api/v1/category").param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testUpdateProduct_Success() throws Exception {
        int productId = 1;
        String updatedProductName = "Updated Product";
        int updatedCategoryId = 2;
        double updatedPrice = 15.99;
        int updatedQuantity = 100;

        doNothing().when(inventoryManagementService).updateProduct(productId, updatedProductName, updatedCategoryId, updatedPrice, updatedQuantity);

        mockMvc.perform(put("/api/v1/product")
                        .param("productId", String.valueOf(productId))
                        .param("productName", updatedProductName)
                        .param("categoryId", String.valueOf(updatedCategoryId))
                        .param("price", String.valueOf(updatedPrice))
                        .param("quantity", String.valueOf(updatedQuantity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated product details"));
    }

    @Test
    public void testUpdateCategory_Success() throws Exception {
        int categoryId = 1;
        String updatedCategoryName = "Updated Category";

        doNothing().when(inventoryManagementService).updateCategory(categoryId, updatedCategoryName);

        mockMvc.perform(put("/api/v1/category")
                        .param("categoryId", String.valueOf(categoryId))
                        .param("name", updatedCategoryName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated category name"));
    }

    @Test
    public void testDeleteProduct_Success() throws Exception {
        int productId = 1;

        doNothing().when(inventoryManagementService).deleteProduct(productId);

        mockMvc.perform(delete("/api/v1/product")
                        .param("productId", String.valueOf(productId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted product"));
    }

    @Test
    public void testDeleteCategory_Success() throws Exception {
        int categoryId = 1;

        doNothing().when(inventoryManagementService).deleteCategory(categoryId);

        mockMvc.perform(delete("/api/v1/category")
                        .param("categoryId", String.valueOf(categoryId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted category"));
    }

    @Test
    public void testOrderProduct_Success() throws Exception {
        int productId = 1;
        int quantity = 5;
        int userId = 2;

        doNothing().when(inventoryManagementService).orderProduct(productId, quantity, userId);

        mockMvc.perform(put("/api/v1/orders")
                        .param("productId", String.valueOf(productId))
                        .param("quantity", String.valueOf(quantity))
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully ordered"));
    }

    @Test
    public void testReStockProduct_Success() throws Exception {
        int productId = 1;
        int quantity = 10;
        int userId = 2;

        doNothing().when(inventoryManagementService).reStockProduct(productId, quantity, userId);

        mockMvc.perform(put("/api/v1/restock")
                        .param("productId", String.valueOf(productId))
                        .param("quantity", String.valueOf(quantity))
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully restocked"));
    }





}
