package org.sid.billingservice;

import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.entities.ProductItems;
import org.sid.billingservice.feign.CustomerRestClient;
import org.sid.billingservice.feign.ProductitemsrestClient;
import org.sid.billingservice.model.Custumer;
import org.sid.billingservice.model.Product;
import org.sid.billingservice.repositories.Billrepository;
import org.sid.billingservice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(BillingServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner start(
            Billrepository billrepository,
            ProductItemRepository productItemRepository,
            CustomerRestClient customerRestClient,
            ProductitemsrestClient productitemsrestClient
    ){
        return args -> {
            Custumer custumer=customerRestClient.getCustomerByID(1L);
            Bill bill1 =billrepository.save(new Bill(null,new Date(),null,custumer.getId(),null));
            PagedModel<Product> productPagedModel=productitemsrestClient.pageproduct(0,5);
            productPagedModel.forEach(p->{
                ProductItems productItems=new ProductItems();
                productItems.setPrice((p.getPrice()));
                productItems.setQuantity(1+new Random().nextInt(100));
                productItems.setBill(bill1);
                productItems.setProductid(p.getId());
                productItemRepository.save(productItems);
            });

        };
    }

}
