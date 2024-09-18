package com.assesment2.inventoryManagement.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class Order {
    @Id
    int orderId;
    int productId;
    int userId;
    Date orderDate;
}
