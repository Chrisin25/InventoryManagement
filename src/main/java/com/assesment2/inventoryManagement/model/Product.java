package com.assesment2.inventoryManagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {
    @Id
    int productId;
    String productName;
    int categoryId;
    Double price;
    int quantity;
}
