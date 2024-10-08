package com.assesment2.inventoryManagement.model;

import com.assesment2.inventoryManagement.repository.CategoryRepo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Entity
@Table(name="Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer productId;
    String productName;
    Integer categoryId;
    @ManyToOne
    @JoinColumn(name = "categoryId",insertable=false, updatable=false)
    @JsonIgnore
    private Category category;
    Double price;
    Integer quantity;
    @OneToMany(mappedBy = "productId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<OrderTable> orderTables;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }



}
