package com.assesment2.inventoryManagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Category {
    @Id
    int categoryId;
    String categoryName;
}
