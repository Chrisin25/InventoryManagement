package com.assesment2.inventoryManagement.model;

import java.time.Instant;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class Order {
    @Id
    int orderId;
    int productId;
    int userId;
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
