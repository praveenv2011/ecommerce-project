package com.ecom.product.feignclient;


import com.ecom.product.dto.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;


@FeignClient(name = "category",url = "http://localhost:8080")
public interface CategoryClient {

    @GetMapping("/api/categories/{id}")
    Optional<CategoryDTO> getCategoryById(@PathVariable("id") Long id);
}
