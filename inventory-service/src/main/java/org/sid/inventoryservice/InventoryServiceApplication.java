package org.sid.inventoryservice;

import org.sid.inventoryservice.entities.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner start(ProductRepository productRepository, RepositoryRestConfiguration restConfiguration){
		restConfiguration.exposeIdsFor(Product.class);
		return args -> {
			productRepository.save(new Product(null,"PC",5000,40));
			productRepository.save(new Product(null,"smartmobile",70,45));
			productRepository.save(new Product(null,"impri",20,15));
			productRepository.findAll().forEach(product -> System.out.println(product.toString()));
			System.out.println(productRepository.toString());
		};
	}

}
