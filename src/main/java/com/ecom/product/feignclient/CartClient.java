package com.ecom.product.feignclient;

import com.ecom.product.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "cart", url = "http://localhost:8083")
public interface CartClient {


    @GetMapping("api/carts")
    List<CartDTO> getAllCarts();

    @DeleteMapping("api/users/{userid}/carts/products/{productid}")
    String deleteProductInCarts(@PathVariable("userid") Long userid,
                                @PathVariable("productid") Long productid);
}
