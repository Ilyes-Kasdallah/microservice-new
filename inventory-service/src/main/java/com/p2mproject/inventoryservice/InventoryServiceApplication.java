package com.p2mproject.inventoryservice;

import com.p2mproject.inventoryservice.model.Inventory;
import com.p2mproject.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import org.springframework.context.annotation.Bean;
@EnableDiscoveryClient
@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	// this will load the data when the application is starting
    @Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		 return args -> {
			 Inventory inventory = new Inventory();
			 inventory.setSkuCode("Iphone_13");
			 inventory.setQuantity(100);

			 Inventory inventory1 = new Inventory();
			 inventory1.setSkuCode("Iphone_13_red");
			 inventory1.setQuantity(0);

			 inventoryRepository.save(inventory);
			 inventoryRepository.save(inventory1);

		 };
	}

}
