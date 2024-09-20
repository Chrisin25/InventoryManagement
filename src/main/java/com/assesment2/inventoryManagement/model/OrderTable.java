package com.assesment2.inventoryManagement.model;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "OrderTable")
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer orderId;
    Integer productId;
    Integer userId;
    Instant orderDate;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }
}
