package org.sid.billingservice.web;

import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.feign.CustomerRestClient;
import org.sid.billingservice.feign.ProductitemsrestClient;
import org.sid.billingservice.model.Custumer;
import org.sid.billingservice.model.Product;
import org.sid.billingservice.repositories.Billrepository;
import org.sid.billingservice.repositories.ProductItemRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingRestController {
    private Billrepository billrepository;
    private ProductItemRepository productItemRepository;
    private CustomerRestClient customerRestClient;
    private ProductitemsrestClient productitemsrestClient;

    public BillingRestController(Billrepository billrepository, ProductItemRepository productItemRepository, CustomerRestClient customerRestClient, ProductitemsrestClient productitemsrestClient) {
        this.billrepository = billrepository;
        this.productItemRepository = productItemRepository;
        this.customerRestClient = customerRestClient;
        this.productitemsrestClient = productitemsrestClient;
    }

    @GetMapping(path = "/fullBill/{id}")
    public Bill getBill(@PathVariable(name ="id")Long id){
        Bill bill=billrepository.findById(id).get();
        Custumer custumer=customerRestClient.getCustomerByID(bill.getCustomerID());
        bill.setCustomer(custumer);
        bill.getProductItems().forEach(p->{
            Product product=productitemsrestClient.getproductByID(p.getProductid());
            System.out.println(product.toString());
            p.setProduct(product);
        });
        return bill;

            };
}
