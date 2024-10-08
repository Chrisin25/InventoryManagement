package com.assesment2.inventoryManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class InventoryManagementApplication {

	public static void main(String[] args) {

		SpringApplication.run(InventoryManagementApplication.class, args);
	}

}
