package com.assesment2.inventoryManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assesment2.inventoryManagement.model.User;

public interface UserRepo extends JpaRepository<User,Integer>{
    
}
