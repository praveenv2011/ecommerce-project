package com.ecom.order.feignClient;

import com.ecom.order.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product",url = "http://localhost:8081")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);

    @PutMapping("/api/products/{productId}")
    ProductDTO updateProductById(@RequestBody ProductDTO productDTO,
                                 @PathVariable(name = "productId") Long productId);

}
