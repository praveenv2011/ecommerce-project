package com.ecom.order.feignClient;

import com.ecom.order.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart",url = "http://localhost:8083")
public interface CartClient {

    @GetMapping("/api/users/{userid}/cart")
    CartDTO getCartByUserId(@PathVariable("userid") Long userId);

    @DeleteMapping("/api/users/{userid}/carts/products/{productid}")
    String deleteProductInCartById(@PathVariable("userid") Long userid,
                                   @PathVariable("productid") Long productid);

}
