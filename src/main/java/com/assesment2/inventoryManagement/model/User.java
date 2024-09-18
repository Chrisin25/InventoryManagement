package com.assesment2.inventoryManagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    int userId;
    String name;
    String role;
}
