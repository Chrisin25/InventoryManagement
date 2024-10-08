package com.assesment2.inventoryManagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "User")
public class User {
    @Id
    Integer userId;
    String name;
    String role;


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
