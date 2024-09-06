package com.ecom.cart.feignclient;

import com.ecom.cart.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "product",url = "http://localhost:8081")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    Optional<ProductDTO> getProductByid(@PathVariable("id") Long id);

    @PutMapping("/api/products/{productId}")
    ProductDTO updateProductById(@RequestBody ProductDTO productDTO,@PathVariable("id") Long id);
}
