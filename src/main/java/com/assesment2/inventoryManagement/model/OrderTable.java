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
    @ManyToOne
    @JoinColumn(name = "productId", insertable = false, updatable = false)
    private Product product;
    Integer userId;
    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
    Instant orderDate;


    public void setProductId(int productId) {
        this.productId = productId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }


    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }
}
