package org.sid.billingservice.feign;

import org.sid.billingservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductitemsrestClient {
    @GetMapping(path = "/products")
    PagedModel<Product> pageproduct(@RequestParam(name = "page") int page,
                                    @RequestParam(name = "size")int size);
    @GetMapping(path ="/products/{id}")
    Product  getproductByID(@PathVariable Long id);


}
